package org.codehaus.rdbms.impl;

import org.codehaus.rdbms.MutableColumn;
import org.codehaus.rdbms.Table;
import org.codehaus.rdbms.Column;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public class ColumnImpl implements MutableColumn, Comparable {
    private final Table table;
    private int sqlType;
    private String sqlTypeName;
    private String sqlName;
    private boolean isPk;
    private boolean isFk;
    private int size;
    private int decimalDigits;
    private boolean isNullable;
    private boolean isIndexed;
    private boolean isUnique;
    private String defaultValue;
    private String indexName;

    public ColumnImpl(Table table) {
        this.table = table;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public String getSqlTypeName() {
        return sqlTypeName;
    }

    public void setSqlTypeName(String sqlTypeName) {
        this.sqlTypeName = sqlTypeName;
    }

    public String getSqlName() {
        return sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public boolean isPrimaryKey() {
        return isPk;
    }

    public void setPk(boolean pk) {
        isPk = pk;
    }

    public boolean isForeignKey() {
        return isFk;
    }

    public void setFk(boolean fk) {
        isFk = fk;
    }

    public int getSqlSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean nullable) {
        isNullable = nullable;
    }

    public boolean isIndexed() {
        return isIndexed;
    }

    public void setIndexed(boolean indexed) {
        isIndexed = indexed;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexName() {
        return indexName;
    }

    public Table getTable() {
        return table;
    }

    public int compareTo(Object o) {
        return getSqlName().compareTo(((Column) o).getSqlName());
    }

    public String toString() {
        return getSqlName();
    }
}

