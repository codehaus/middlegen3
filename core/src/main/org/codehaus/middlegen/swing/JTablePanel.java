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

package org.codehaus.middlegen.swing;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;

/**
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Hellesøy</a>
 * @version $Id: JTablePanel.java,v 1.3 2003/05/11 13:52:38 rinkrank Exp $
 */
public class JTablePanel extends JPanel {

	private final org.codehaus.rdbms.Table _table;

	private final JLabel _title;
	private final JTableList _jList;
	private final int _cellHeight;
	private static Border _unSelectedBorder = BorderFactory.createLineBorder(Color.black, 1);
	private static Border _selectedBorder = BorderFactory.createLineBorder(Color.black, 2);

	public JTablePanel(org.codehaus.rdbms.Table table) {
		setLayout(new BorderLayout());
		_table = table;
		_title = new JLabel(" " + table.getSqlName() + " ", SwingConstants.CENTER);
		add(_title, BorderLayout.NORTH);

		_jList = new JTableList(table);

		// Set the height of the table component
		Rectangle r = _jList.getCellBounds(0, 0);
		_cellHeight = r.height;
		add(_jList, BorderLayout.CENTER);
		setSelected(false);
	}

	public void setSelected(boolean selected) {
		if (selected) {
			if (getBorder() == _unSelectedBorder) {
				setLocation(getLocation().x - 1, getLocation().y - 1);
			}
			setBorder(_selectedBorder);
		}
		else {
			if (getBorder() == _selectedBorder) {
				setLocation(getLocation().x + 1, getLocation().y + 1);
			}
			setBorder(_unSelectedBorder);
			_jList.clearSelection();
		}
	}

	public JList getList() {
		return _jList;
	}

	public org.codehaus.rdbms.Table getTable() {
		return _table;
	}


	/**
	 * Gets the y coordinate of the given column.
	 *
	 * @param column Describe what the parameter does
	 * @return The ColumnY value
	 */
	public int getColumnY(org.codehaus.rdbms.Column column) {
		int h = getHeight();

		int rowCount = _jList.getModel().getSize();
		int index = _table.getColumns().indexOf(column);
		if (index == -1) {
			// throw new IllegalStateException("There is no column named " + columnName + " in the table named " + _table.getSqlName() + org.codehaus.middlegen.RdbmsTableFromJdbcMetadataProvider.BUGREPORT);
			System.err.println("There is no column named " + column.getSqlName() + " in the table named " + _table.getSqlName());
		}
		System.err.println(_table.getSqlName() + "." + column.getSqlName() + " " + index);
		int rowsUpFromBottom = rowCount - index;

		int y = h - (rowsUpFromBottom * _cellHeight) + (_cellHeight / 2);
		return y;
	}
}

