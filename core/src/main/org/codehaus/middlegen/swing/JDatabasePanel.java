/*
 * Copyright (c) 2001, The RdbmsTableFromJdbcMetadataProvider Group
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of The RdbmsTableFromJdbcMetadataProvider Group nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

/*
 * Change log
 *
 */
package org.codehaus.middlegen.swing;

import org.codehaus.rdbms.Table;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This panel displays tables (Swing) and relations (2D API). Dragging and other
 * user interaction logic is also handled here.
 *
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Hellesøy</a>
 * @version $Id: JDatabasePanel.java,v 1.5 2003/07/09 18:24:24 rinkrank Exp $
 */
public class JDatabasePanel extends JPanel {
	private Map _table2JTableMap = new HashMap();
	private Collection _lines = new ArrayList();

	/**
	 * Keep a ref to all the JTablePanel objects so we can save the position in
	 * prefs
	 */
	private Collection _tablePanels = new ArrayList();
	private Map _ejbConfigurationMap = new HashMap();

	/**
	 * The listener that will be notified when a table's column is selected
	 */
	private final ListSelectionListener _columnSelectionListener;
	private RelationLine _selectedRelationLine = null;

	/**
	 * Creates new JDatabasePanel
	 *
	 * @param settingsTabPane Describe what the parameter does
	 * @param header Describe what the parameter does
	 */
	public JDatabasePanel(final JSettingsTabPane settingsTabPane, final JLabel header) {
		printHints();
		_columnSelectionListener = new SettingsPanelCommander(settingsTabPane, header);

		setLayout(new MiddlegenLayout());

		MouseInputAdapter mouse =
			new MouseInputAdapter() {
				private JTablePanel _pressed = null;
				private int _deltaX;
				private int _deltaY;


				public void mousePressed(MouseEvent evt) {
					_selectedRelationLine = null;
					Component c = getComponentAt(evt.getPoint());
					if (c != null && c instanceof JTablePanel) {
						_pressed = (JTablePanel)c;
						_deltaX = evt.getX() - _pressed.getX();
						_deltaY = evt.getY() - _pressed.getY();

						// loop over all tables and highlight the selected one
						Set tableSet = _table2JTableMap.entrySet();
						Iterator i = tableSet.iterator();
						while (i.hasNext()) {
							Map.Entry entry = (Map.Entry)i.next();
							JTablePanel tablePanel = (JTablePanel)entry.getValue();
							if (tablePanel == _pressed) {
								tablePanel.setSelected(true);
								// update bottom panel
								settingsTabPane.setTable(tablePanel.getTable());

								// update header
								header.setText(tablePanel.getTable().getSchemaPrefixedSqlName());
							}
							else {
								tablePanel.setSelected(false);
							}

						}
					}
					else {
						_pressed = null;
						selectRelationLine(evt);
					}
				}


				public void mouseDragged(MouseEvent evt) {
					if (_pressed != null) {
						Point p = evt.getPoint();
						p.translate(-_deltaX, -_deltaY);
						// don't let the user drag it too far up/left. she won't be able to grab it again
						if (p.x < 0) {
							p.x = 0;
						}
						if (p.y < 0) {
							p.y = 0;
						}

						_pressed.setLocation(p);
						updateRelationLines();
						setSize(getPreferredSize());
						repaint();
					}
				}
			};

		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}


	/**
	 * Gets the Size attribute of the JDatabasePanel object
	 *
	 * @return The Size value
	 */
	public Dimension getSize() {
		int maxX = 0;
		int maxY = 0;

		Rectangle r = new Rectangle();
		Iterator tables = _table2JTableMap.values().iterator();
		while (tables.hasNext()) {
			JTablePanel table = (JTablePanel)tables.next();
			r = table.getBounds(r);
			maxX = Math.max(maxX, r.x + r.width);
			maxY = Math.max(maxY, r.y + r.height);
		}

		Dimension d = new Dimension(maxX + 10, maxY + 10);
//		System.out.println("getSqlSize():" + d);
		return d;
	}


	/**
	 * Gets the MinimumSize attribute of the JDatabasePanel object
	 *
	 * @return The MinimumSize value
	 */
	public Dimension getMinimumSize() {
		return new Dimension(400, 600);
	}


	/**
	 * Gets the MaximumSize attribute of the JDatabasePanel object
	 *
	 * @return The MaximumSize value
	 */
	public Dimension getMaximumSize() {
//		System.out.println("getMaximumSize():" + getSqlSize());
		return getMinimumSize();
	}


	/**
	 * Gets the PreferredSize attribute of the JDatabasePanel object
	 *
	 * @return The PreferredSize value
	 */
	public Dimension getPreferredSize() {
//		System.out.println("getPreferredSize():" + getSqlSize());
		return getSize();
	}


	public void paint(Graphics g) {
		super.paint(g);
		paintRelationLines(g);
	}

	public void reset(Collection tables) {
		_table2JTableMap.clear();
		_lines.clear();
		_ejbConfigurationMap.clear();

		removeAll();

		int i = 10;
		for (Iterator tableIterator = tables.iterator(); tableIterator.hasNext();) {
			org.codehaus.rdbms.Table table = (org.codehaus.rdbms.Table) tableIterator.next();
			if (table.isEnabled()) {
                // Instantiate a JTablePanel for each database table
				JTablePanel jTablePanel = new JTablePanel(table);
				_tablePanels.add(jTablePanel);
				// Add the listener that will populate the column panels
				jTablePanel.getList().addListSelectionListener(_columnSelectionListener);

				add(jTablePanel);
				_table2JTableMap.put(table, jTablePanel);

				// position the table. See if there is a value in prefs.
/*
				int x = table.getPrefsX();
				int y = table.getPrefsY();
				if (x == Integer.MIN_VALUE) {
					// Nothing in prefs
					x = i;
					y = i;
				}
*/
                int x, y;
                x = y = i;
				jTablePanel.setLocation(x, y);
				i += 20;
			}
			else {
				// The table has generate="false". Don't display it
			}
		}

		validate();
		repaint();

		// prepare and add relation lines
        for (Iterator tableIterator = tables.iterator(); tableIterator.hasNext();) {
            org.codehaus.rdbms.Table table = (org.codehaus.rdbms.Table) tableIterator.next();
            for (Iterator fkIterator = table.getForeignKeys().iterator(); fkIterator.hasNext();) {
    			addRelationLineMaybe((org.codehaus.rdbms.ForeignKey)fkIterator.next());
            }
		}
		updateRelationLines();
		setSize(getPreferredSize());
		validate();
		repaint();
		//	System.out.println(getPreferredSize());
	}


	/**
	 * Updates prefs with table's positions
	 */
	void setPrefs() {
		for (Iterator i = _tablePanels.iterator(); i.hasNext(); ) {
			JTablePanel tablePanel = (JTablePanel)i.next();
			Table table = tablePanel.getTable();
			int x = tablePanel.getX();
			int y = tablePanel.getY();
//			table.setPosition(x, y);
		}
	}

	private void printHints() {
		System.out.println("********************************************************");
		System.out.println("* CTRL-Click relations to modify their cardinality     *");
		System.out.println("* SHIFT-Click relations to modify their directionality *");
		System.out.println("********************************************************");
	}

	private void selectRelationLine(MouseEvent evt) {
		for (Iterator i = _lines.iterator(); i.hasNext(); ) {
			RelationLine relationLine = (RelationLine)i.next();
			if (relationLine.selectMaybe(evt)) {
				// hit a line
				_selectedRelationLine = relationLine;
				repaint();
				break;
			}
		}
	}


	/**
	 * Adds a foreignKey line if generation is enabled for both extremities
	 *
	 * @param foreignKey Describe the method parameter
	 */
	private void addRelationLineMaybe(org.codehaus.rdbms.ForeignKey foreignKey) {
        JTablePanel originTable = (JTablePanel)_table2JTableMap.get(foreignKey.getTableA());
        JTablePanel targetTable = (JTablePanel)_table2JTableMap.get(foreignKey.getTableB());
        RelationLine relationLine = new RelationLine(foreignKey, originTable, targetTable);
        _lines.add(relationLine);
	}

	private void updateRelationLines() {
		for (Iterator i = _lines.iterator(); i.hasNext(); ) {
			RelationLine relationLine = (RelationLine)i.next();
			relationLine.update();
		}
	}

	private void paintRelationLines(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		for (Iterator i = _lines.iterator(); i.hasNext(); ) {
			RelationLine relationLine = (RelationLine)i.next();
			if (relationLine != _selectedRelationLine) {
				// Draw the selected line last in a different color
				relationLine.paint(g2d);
			}
		}
		if (_selectedRelationLine != null) {
			Color old = g.getColor();
			g.setColor(Color.white);
			_selectedRelationLine.paint(g2d);
			g.setColor(old);
		}
	}
}

