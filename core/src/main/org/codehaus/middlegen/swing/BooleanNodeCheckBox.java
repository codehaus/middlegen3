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

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Observer;
import java.util.Observable;
import org.codehaus.middlegen.util.BooleanNode;
import org.codehaus.middlegen.swing.BooleanNodeButtonModel;

/**
 * Describe what this class does
 *
 * @author Aslak Hellesøy
 * @created 26. mars 2002
 * @todo-javadoc Write javadocs
 */
public class BooleanNodeCheckBox extends JCheckBox {
	/**
	 * @todo-javadoc Describe the column
	 */
	private BooleanNode _booleanNode;

	/**
	 * @todo-javadoc Describe the column
	 */
	private final Icon _true = new ImageIcon(getClass().getResource("images/true.gif"));
	/**
	 * @todo-javadoc Describe the column
	 */
	private final Icon _fuzzy = new ImageIcon(getClass().getResource("images/fuzzy.gif"));
	/**
	 * @todo-javadoc Describe the column
	 */
	private final Icon _false = new ImageIcon(getClass().getResource("images/false.gif"));


	/**
	 * Describe what the BooleanNodeCheckBox constructor does
	 *
	 * @param booleanNode Describe what the parameter does
	 * @param title Describe what the parameter does
	 * @todo-javadoc Write javadocs for method parameter
	 * @todo-javadoc Write javadocs for constructor
	 * @todo-javadoc Write javadocs for method parameter
	 */
	public BooleanNodeCheckBox(String title, BooleanNode booleanNode) {
		super(title);
		setIcon(_false);
		setSelectedIcon(_true);
		if (booleanNode != null) {
			setBooleanNode(booleanNode);
		}
	}


	/**
	 * Sets the BooleanNode attribute of the BooleanNodeCheckBox object
	 *
	 * @param booleanNode The new BooleanNode value
	 */
	public void setBooleanNode(BooleanNode booleanNode) {
		_booleanNode = booleanNode;
		ButtonModel oldModel = getModel();
		if (oldModel != null && oldModel instanceof Observer) {
			booleanNode.deleteObserver((Observer)oldModel);
		}
		setModel(new BooleanNodeButtonModel(booleanNode));
	}


	/**
	 * Describe what the method does
	 *
	 * @param g Describe what the parameter does
	 * @todo-javadoc Write javadocs for method
	 * @todo-javadoc Write javadocs for method parameter
	 */
	public void paint(Graphics g) {
		if (_booleanNode != null) {
			if (_booleanNode.isPartiallyTrue() && !_booleanNode.isCompletelyTrue()) {
				setSelectedIcon(_fuzzy);
			}
			else {
				setSelectedIcon(_true);
			}
		}
		super.paint(g);
	}
}
