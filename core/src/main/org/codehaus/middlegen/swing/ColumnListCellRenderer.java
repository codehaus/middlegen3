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

import java.awt.Component;
import java.awt.Font;

import javax.swing.JList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.DefaultListCellRenderer;

import org.codehaus.rdbms.Column;

/**
 * Renderer for the tree in the tree table
 *
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Hellesøy</a>
 * @created 21. mars 2002
 */
class ColumnListCellRenderer extends DefaultListCellRenderer {

	/**
	 * @todo-javadoc Describe the column
	 */
	private final Icon _pkIcon = new ImageIcon(ColumnListCellRenderer.class.getResource("pk.gif"));
	/**
	 * @todo-javadoc Describe the column
	 */
	private final Icon _transparentIcon = new ImageIcon(ColumnListCellRenderer.class.getResource("transparent.gif"));

	/**
	 * @todo-javadoc Describe the column
	 */
	private final Font _normalFont = getFont();
	/**
	 * @todo-javadoc Describe the column
	 */
	private final Font _italicFont = getFont().deriveFont(Font.ITALIC | Font.BOLD);


	/**
	 * Gets the ListCellRendererComponent attribute of the ColumnListCellRenderer
	 * object
	 *
	 * @param list Describe what the parameter does
	 * @param value Describe what the parameter does
	 * @param index Describe what the parameter does
	 * @param isSelected Describe what the parameter does
	 * @param cellHasFocus Describe what the parameter does
	 * @return The ListCellRendererComponent value
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for method parameter
	 */
	public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus
			) {
		DefaultListCellRenderer renderer = (DefaultListCellRenderer)super.getListCellRendererComponent(
				list,
				value,
				index,
				isSelected,
				cellHasFocus
				);

		org.codehaus.rdbms.Column column = (org.codehaus.rdbms.Column)value;

		renderer.setIcon(column.isPrimaryKey() ? _pkIcon : _transparentIcon);
		renderer.setFont(column.isNullable() ? _italicFont : _normalFont);
		renderer.setText(column.getSqlName());

		return renderer;
	}
}
