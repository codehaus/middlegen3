package org.codehaus.middlegen.model;

import org.codehaus.rdbms.Column;
import org.codehaus.rdbms.impl.DatabaseNameConverter;
import org.codehaus.rdbms.impl.Util;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.2 $
 */
public class GenericFieldImpl implements GenericField {
    private final Column column;
    private final DatabaseNameConverter databaseNameConverter;
    private String variableName;


    public GenericFieldImpl(Column column, DatabaseNameConverter databaseNameConverter) {
        this.column = column;
        this.databaseNameConverter = databaseNameConverter;
    }

    public String getVariableName() {
        if (variableName == null) {
            String sqlName = column.getSqlName();
            this.variableName = databaseNameConverter.columnNameToVariableName(sqlName);
        }
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getCapitalisedVariableName() {
        return Util.capitalise(getVariableName());
    }

    public boolean isPrimitiveOrComparable() {
        // TODO
        return false;
    }

    public boolean isPrimitive() {
        // TODO
        return false;
    }

    public boolean isNumericClass() {
        // TODO
        return false;
    }

    public String getClassForPrimitive() {
        // TODO
        return null;
    }
}
