package org.codehaus.middlegen.swing;

import org.codehaus.middlegen.util.BooleanNode;

import javax.swing.JToggleButton;
import java.util.Observable;
import java.util.Observer;

public class BooleanNodeButtonModel extends JToggleButton.ToggleButtonModel implements Observer {
	private final BooleanNode booleanNode;

	public BooleanNodeButtonModel(BooleanNode booleanNode) {
		this.booleanNode = booleanNode;
		this.booleanNode.addObserver(this);
	}

	public void setSelected(boolean b) {
		booleanNode.setValue(b);
	}

	public boolean isSelected() {
		return booleanNode.isPartiallyTrue();
	}

	public void update(Observable o, Object arg) {
		fireStateChanged();
	}
}
