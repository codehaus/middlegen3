package org.codehaus.rdbms.impl;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public class DefaultDatabaseNameConverter implements DatabaseNameConverter {

    public String columnNameToVariableName(String columnName) {
        return dbNameToVariableName(columnName);
    }

    public String tableNameToVariableName(String tableName) {
        return dbNameToVariableName(tableName);
    }


    /**
     * Converts a database name (table or column) to a java name (first letter
     * capitalised). employee_name -> EmployeeName
     *
     * @param s the database name to convert
     * @return the converted database name
     */
    private String dbNameToVariableName(String s) {
        if ("".equals(s)) {
            return s;
        }
        StringBuffer result = new StringBuffer();
        boolean capitalize = true;
        boolean lastCapital = false;
        boolean lastDecapitalized = false;
        for (int i = 0; i < s.length(); i++) {
            String c = s.substring(i, i + 1);
            if ("_".equals(c) || " ".equals(c)) {
                capitalize = true;
                continue;
            }

            if (c.toUpperCase().equals(c)) {
                if (lastDecapitalized && !lastCapital) {
                    capitalize = true;
                }
                lastCapital = true;
            } else {
                lastCapital = false;
            }

            if (capitalize) {
                result.append(c.toUpperCase());
                capitalize = false;
            } else {
                result.append(c.toLowerCase());
                lastDecapitalized = true;
            }

        }
        String r = result.toString();
        return r;
    }
}
