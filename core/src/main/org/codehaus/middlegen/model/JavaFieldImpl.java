package org.codehaus.middlegen.model;

import org.codehaus.rdbms.Column;
import org.codehaus.rdbms.jdbc.Sql2JavaHelper;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author <a href="mailto:aslak.hellesoy at bekk.no">Aslak Helles&oslash;y</a>
 * @version $Revision: 1.3 $
 */
public class JavaFieldImpl implements JavaField {
    private Column column;
    private String javaType;
    private GenericField genericField;

    public JavaFieldImpl(Column column, GenericField middlegenField ) {
        this.column = column;
        this.genericField = middlegenField;
    }

    public void setJavaType( String javaType ) {
        List allowedTypes = Arrays.asList( Sql2JavaHelper.getJavaTypes( column.getSqlType() ) );
        if( !allowedTypes.contains(javaType) ) {
            throw new IllegalArgumentException( "Bad java type: " + javaType );
        }
        this.javaType = javaType;
    }

    public String getJavaType() {
        if (javaType == null ) {
            javaType = Sql2JavaHelper.getPreferredJavaType(column.getSqlType(),column.getSqlSize(), column.getDecimalDigits());
        }
        return javaType;
    }

    public String getGetterName() {
        return "get" + genericField.getCapitalisedVariableName();
    }

    public String getSetterName() {
        return "set" + genericField.getCapitalisedVariableName();
    }

    public GenericField getGenericField() {
        return genericField;
    }
}
