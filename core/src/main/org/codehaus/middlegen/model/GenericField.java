package org.codehaus.middlegen.model;

/**
 *
 * @author <a href="mailto:aslak.hellesoy at bekk.no">Aslak Helles&oslash;y</a>
 * @version $Revision: 1.1 $
 */
public interface GenericField {
    String getVariableName();

    /** @deprecated */
    String getCapitalisedVariableName();

    /** TODO move to JavaField? */
    boolean isPrimitiveOrComparable();

    boolean isPrimitive();

    boolean isNumericClass();

    String getClassForPrimitive();
}
