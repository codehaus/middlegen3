package org.codehaus.rdbms;



/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public interface MutableTable extends Table {
    void addForeignKey( ForeignKey foreignKey );
    void setSqlName( String string );
    void setSchemaName( String string );
    void addColumn(MutableColumn column);
}
