package org.codehaus.rdbms.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.codehaus.rdbms.Column;
import org.codehaus.rdbms.ForeignKey;
import org.codehaus.rdbms.MutableColumn;
import org.codehaus.rdbms.MutableTable;
import org.codehaus.rdbms.PrimaryKey;
import org.codehaus.rdbms.Table;
import org.codehaus.rdbms.TableFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class TableImpl implements MutableTable, Comparable {

    private final Collection foreignKeyList = new TreeSet();
    private Map foreignKeys = new HashMap();

    private String sqlName;
    private String schemaName;

    private final List columns = new ArrayList();
    private final Map columnSqlName2ColumnMap = new HashMap();
    private PrimaryKeyImpl primaryKey;
    private TableFactory tableFactory;

    public TableImpl(TableFactory tableFactory) {
        this.tableFactory = tableFactory;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSqlName(boolean withSchemaPrefix) {
        return withSchemaPrefix ? getSchemaPrefixedSqlName() : getSqlName();
    }

    public String getSchemaPrefixedSqlName() {
        boolean noschema = schemaName == null || schemaName.trim().equals("");
        String result;
        if (noschema) {
            result = getSqlName();
        } else {
            result = schemaName + "." + getSqlName();
        }
        return result;
    }

    public String getSqlName() {
        return sqlName;
    }

    public Collection getForeignKeys() {
        return Collections.unmodifiableCollection(foreignKeyList);
    }

    public Collection getForeignKeys(Predicate predicate) {
        return CollectionUtils.select(getForeignKeys(), predicate);
    }

    public List getColumns() {
        Collections.sort(columns);
        return Collections.unmodifiableList(columns);
    }

    public Collection getColumns(Predicate predicate) {
        return CollectionUtils.select(getColumns(), predicate);
    }

    public ForeignKey getForeignKey(String name) {
        return (ForeignKey) foreignKeys.get(name);
    }

    public PrimaryKey getPrimaryKey() {
        if (primaryKey == null) {
            // get a ref to ourself as a mixin
            Table table = tableFactory.getTable(getSqlName());
            primaryKey = new PrimaryKeyImpl(table);
        }
        return primaryKey;
    }

    public Column getPrimaryKeyColumn() {
        return getPrimaryKey().getColumns().size() == 1 ? (Column) getPrimaryKey().getColumns().get(0) : null;
    }

    public boolean isEnabled() {
        return true;
    }

    public Column getColumn(String sqlName) {
        Column result = (Column) columnSqlName2ColumnMap.get(sqlName);
        if (result == null) {
            throw new IllegalArgumentException("There is no column named " + sqlName + " in the table named " + getSqlName());
        }
        return result;
    }

    public void addForeignKey(ForeignKey foreignKey) {
        foreignKeys.put(foreignKey.getName(), foreignKey);
        foreignKeyList.add(foreignKey);
    }

    public void addColumn(MutableColumn column) {
        columns.add(column);
        columnSqlName2ColumnMap.put(column.getSqlName(), column);

        if (column.isPrimaryKey()) {
            PrimaryKeyImpl primaryKey = (PrimaryKeyImpl) getPrimaryKey();
            primaryKey.addColumn(column);
        }
    }

    public int compareTo(Object o) {
        return getSqlName(true).compareTo(((Table) o).getSqlName(true));
    }
}
