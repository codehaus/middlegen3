package org.codehaus.rdbms.impl;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public interface DatabaseNameConverter {
    String columnNameToVariableName(String columnName);

    String tableNameToVariableName(String tableName);
}