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

import org.codehaus.middlegen.MiddlegenPlugin;
import org.picocontainer.Startable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A tabbed pane which displays settings for each active class type
 *
 * @author Aslak Hellesøy
 */
public class JSettingsTabPane extends JTabbedPane {
	private final Collection _settingsPanels = new ArrayList();
	public JSettingsTabPane(Collection plugins) {
		Iterator pluginIterator = plugins.iterator();
		while (pluginIterator.hasNext()) {

			Startable plugin = (Startable) pluginIterator.next();

            if (plugin instanceof MiddlegenPlugin) {
                MiddlegenPlugin middlegenPlugin = (MiddlegenPlugin) plugin;
                JColumnSettingsPanel columnSettingsPanel = middlegenPlugin.getColumnSettingsPanel();
                JTableSettingsPanel tableSettingsPanel = middlegenPlugin.getTableSettingsPanel();
                if (columnSettingsPanel != null && tableSettingsPanel != null) {
                    JSettingsPanel settingsPanel = new JSettingsPanel(columnSettingsPanel, tableSettingsPanel);
                    _settingsPanels.add(settingsPanel);
                    addTab(plugin.getClass().getName(), settingsPanel);
                }
            }
        }
	}

	public void setColumns(org.codehaus.rdbms.Column[] columns) {
		Iterator i = _settingsPanels.iterator();
		while (i.hasNext()) {
			JSettingsPanel settingsPanel = (JSettingsPanel)i.next();
			settingsPanel.setColumns(columns);
		}
	}

	public void setTable(org.codehaus.rdbms.Table table) {
		Iterator i = _settingsPanels.iterator();
		while (i.hasNext()) {
			JSettingsPanel settingsPanel = (JSettingsPanel)i.next();
			settingsPanel.setTable(table);
		}
	}
}
