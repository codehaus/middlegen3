package org.codehaus.rdbms;

import org.codehaus.rdbms.Column;

/**
 *
 * @author <a href="mailto:aslak.hellesoy at bekk.no">Aslak Helles&oslash;y</a>
 * @version $Revision: 1.1 $
 */
public interface MutableColumn extends Column {
    void setSqlType( int sqlType );
    void setFk( boolean fk );
    void setSqlTypeName( String sqlTypeName );
    void setSqlName( String sqlName );
    void setSize( int size );
    void setDecimalDigits( int decimalDigits );
    void setPk( boolean pk );
    void setNullable( boolean nullable );
    void setIndexed( boolean indexed );
    void setUnique( boolean unique );
    void setDefaultValue( String defaultValue );
    void setIndexName(String indexName);

    String getIndexName();
}
