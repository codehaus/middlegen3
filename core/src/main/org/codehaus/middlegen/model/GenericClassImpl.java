package org.codehaus.middlegen.model;

import org.codehaus.rdbms.Table;
import org.codehaus.rdbms.impl.DatabaseNameConverter;
import org.codehaus.rdbms.impl.Util;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.2 $
 */
public class GenericClassImpl implements GenericClass {
    private final Table table;
    private final DatabaseNameConverter databaseNameConverter;

    private String plural;
    private String singular;

    private String variableName;
    private String className;

    public GenericClassImpl( Table table, DatabaseNameConverter databaseNameConverter ) {
        this.table = table;
        this.databaseNameConverter = databaseNameConverter;
    }

    public String getClassName() {
        if( className == null ) {
            String className = databaseNameConverter.tableNameToVariableName(table.getSqlName());
            className = Util.singularise(className);
            setClassName( className );
        }
        return className;
    }

    public String getVariableName() {
        if( variableName == null ) {
            setVariableName( Util.decapitalise( getClassName() ) );
        }
        return variableName;
    }

    public String getCapitalisedVariableName() {
        return Util.capitalise( getVariableName() );
    }

    public String getPlural() {
        if( plural == null ) {
            setPlural( Util.pluralise( getVariableName() ) );
        }
        return plural;
    }

    public void setPlural( String plural ) {
        this.plural = plural;
    }

    public String getSingular() {
        if( singular == null ) {
            setSingular( Util.singularise( getVariableName() ) );
        }
        return singular;
    }

    public void setSingular( String singular ) {
        this.singular = singular;
    }

    public void setVariableName( String variableName ) {
        this.variableName = variableName;
    }

    public void setClassName( String className ) {
        this.className = className;
    }
}
