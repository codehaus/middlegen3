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

import java.util.Collection;
import org.codehaus.rdbms.Column;
import org.codehaus.middlegen.swing.JTableList;
import org.codehaus.middlegen.swing.JSettingsTabPane;

import javax.swing.JLabel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
 * @author Aslak Hellesøy
 * @created 21. mars 2002
 * @todo reference a card paneled JGenerationSettingsPanel which contains a
 *      JColumnSettingsPanel and a JTableSettingsPanel
 */
public class SettingsPanelCommander implements ListSelectionListener {

	/**
	 * @todo-javadoc Describe the column
	 */
	private final JSettingsTabPane _settingsTabPane;
	/**
	 * @todo-javadoc Describe the field
	 */
	private final JLabel _header;


	/**
	 * Describe what the SettingsPanelCommander constructor does
	 *
	 * @param settingsTabPane Describe what the parameter does
	 * @param header Describe what the parameter does
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for constructor
	 * @todo-javadoc Write javadocs for method parameter
	 */
	public SettingsPanelCommander(JSettingsTabPane settingsTabPane, JLabel header) {
		_settingsTabPane = settingsTabPane;
		_header = header;
	}


	/**
	 * Describe what the method does
	 *
	 * @param evt Describe what the parameter does
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method
	 * @todo-javadoc Write javadocs for method parameter
	 */
	public void valueChanged(ListSelectionEvent evt) {
		if (!evt.getValueIsAdjusting()) {
			// only want to capture when mouse is released

			JTableList tableList = (JTableList)evt.getSource();

			Collection columnsCollection = tableList.getTable().getColumns();
			// Due to a bug in commons collections CollectionUtils.index doesn't work!!
			java.util.List columnsList = java.util.Arrays.asList(columnsCollection.toArray());

			int[] selectedIndices = tableList.getSelectedIndices();
			String headerText = "";
			if (selectedIndices.length > 0) {
				org.codehaus.rdbms.Column[] columns = new org.codehaus.rdbms.Column[selectedIndices.length];
				for (int i = 0; i < columns.length; i++) {

					//		columns[i] = (Column)CollectionUtils.index(columnsCollection, selectedIndices[i]);
					columns[i] = (org.codehaus.rdbms.Column)columnsList.get(selectedIndices[i]);
					if (columns.length == 1) {
						// Be more verbose if there is only one column
						headerText += columns[i].getSqlTypeName();
					}
					else {
						headerText += columns[i].getSqlName();
					}
					if (i < columns.length - 1) {
						headerText += ", ";
					}
				}
				_settingsTabPane.setColumns(columns);
				_header.setText(headerText);
			}
		}
	}
}
