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

import org.codehaus.rdbms.Column;
import org.codehaus.rdbms.Table;
import org.codehaus.middlegen.swing.JColumnSettingsPanel;
import org.codehaus.middlegen.swing.JTableSettingsPanel;

import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.FlowLayout;

/**
 * This panel renders and modifies columns and tables
 *
 * @author Aslak Hellesøy
 * @version
 */
public class JSettingsPanel extends JPanel {
	private final JColumnSettingsPanel _columnSettingsPanel;
	private final JTableSettingsPanel _tableSettingsPanel;
	private final CardLayout _cards = new CardLayout();
	private final static String NOTHING = "NOTHING";
	private final static String TABLE = "TABLE";
	private final static String FIELD = "FIELD";

	public JSettingsPanel(JColumnSettingsPanel columnSettingsPanel, JTableSettingsPanel tableSettingsPanel) {
		super();
		_columnSettingsPanel = columnSettingsPanel;
		_tableSettingsPanel = tableSettingsPanel;

		setLayout(_cards);
		add(new JPanel(), NOTHING);
		add(decorate(_columnSettingsPanel), FIELD);
		add(decorate(_tableSettingsPanel), TABLE);
	}


	/**
	 * Sets the Table attribute of the JSettingsPanel object
	 *
	 * @param table The new Table value
	 */
	public void setTable(org.codehaus.rdbms.Table table) {
		_cards.show(this, TABLE);
		_tableSettingsPanel.setTable(table);
	}

	public void setColumns(org.codehaus.rdbms.Column[] columns) {
		_cards.show(this, FIELD);
		_columnSettingsPanel.setColumns(columns);
	}


	private JPanel decorate(JPanel panel) {
		JPanel decorator = new JPanel();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		decorator.setLayout(flowLayout);
		decorator.add(panel);
		return decorator;
	}
}
