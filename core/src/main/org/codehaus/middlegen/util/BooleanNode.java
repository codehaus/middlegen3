package org.codehaus.middlegen.util;

import java.util.Observable;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A BooleanNode can have the value true or false, giving additional information
 * on whether or not the true value is "uniform", that is if all sub nodes are
 * also true.
 *
 * @author Aslak Hellesøy
 */
public class BooleanNode extends Observable {
	private boolean _value;
	private final BooleanNode _parent;
	private Collection _children;

	private BooleanNode(BooleanNode parent, boolean initialValue) {
		_parent = parent;
		_value = initialValue;
	}

	public void setValue(boolean value) {
		if (!haveChildren()) {
			_value = value;
		}
		else {
			Iterator i = _children.iterator();
			while (i.hasNext()) {
				BooleanNode child = (BooleanNode)i.next();
				child.setValue(value);
				child.notifyChanged();
			}
		}
		if (_parent != null) {
			// Tell parent that we changed
			_parent.notifyChanged();
		}
		notifyChanged();
	}

	public boolean isCompletelyTrue() {
		return isTrue(true);
	}

	public boolean isPartiallyTrue() {
		return isTrue(false);
	}

    public BooleanNode createChild() {
        return createChild(isCompletelyTrue());
    }

	public BooleanNode createChild(boolean initialValue) {
		BooleanNode child = new BooleanNode(this, initialValue);
		if (_children == null) {
			_children = new ArrayList();
		}
		_children.add(child);
		notifyChanged();
		return child;
	}

	public String toString() {
		if (isCompletelyTrue()) {
			return "true";
		}
		else {
			if (isPartiallyTrue()) {
				return "fuzzy";
			}
			else {
				return "false";
			}
		}
	}

	private boolean isTrue(boolean andAllChildren) {
		boolean result = andAllChildren;
		if (!haveChildren()) {
			result = _value;
		}
		else {
			Iterator i = _children.iterator();
			boolean doLoop = true;
			while (i.hasNext() && doLoop) {
				BooleanNode child = (BooleanNode)i.next();
				boolean childValue = child.isTrue(andAllChildren);
				if (andAllChildren && !childValue) {
					result = false;
					doLoop = false;
				}
				else if (!andAllChildren && childValue) {
					result = true;
					doLoop = false;
				}
			}
		}
		return result;
	}

	private boolean haveChildren() {
		return _children != null && !_children.isEmpty();
	}

	private void notifyChanged() {
		setChanged();
		notifyObservers();
		if (_parent != null) {
			_parent.notifyChanged();
		}
	}

	public static BooleanNode createRoot(boolean initialValue) {
		return new BooleanNode(null, initialValue);
	}
}
