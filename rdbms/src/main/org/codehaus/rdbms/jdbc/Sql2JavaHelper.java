package org.codehaus.rdbms.jdbc;

import java.sql.Types;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class has static methods for mapping SQL types to Java types. Both
 * supported and preferred types are supported.
 *
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Hellesøy</a>
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 * @todo mark non-serializable types with a warning. they will throw an exception
 * in cmr (and maybe other cases too) (at least on wls 6.1). This is
 * InputStream, Reader, Clob, Blob, Array, Ref (at least I think it's a
 * problem. Have to test it)
 */
public class Sql2JavaHelper {


    // see resultset.gif (25 SQL types)
    // see java.sql.Types (28 SQL types in JDK1.3, 30 SQL types in JDK1.4)
    // JDK 1.3 java.sql.Types missing from table: DISTINCT, NULL, OTHER
    // JDK 1.4 java.sql.Types missing from table: DISTINCT, NULL, OTHER, DATALINK, BOOLEAN

    private final static IntStringMap javaTypesForSqlType = new IntStringMap();
    private final static IntStringMap preferredJavaTypeForSqlType = new IntStringMap();

    private final static Comparator typeComparator =
            new Comparator() {
                public int compare(Object o1, Object o2) {
                    String s1 = (String) o1;
                    String s2 = (String) o2;
                    boolean isS1Class = s1.indexOf('.') != -1;
                    boolean isS2Class = s2.indexOf('.') != -1;
                    if ((isS1Class && isS2Class) || (!isS1Class && !isS2Class)) {
                        // Both are class or both are primitive. Compare normally
                        return s1.compareTo(s2);
                    } else {
                        // One is primitive and one is class. Primitive always first
                        return isS1Class ? 1 : -1;
                    }
                }

            };

    private final static HashMap primitiveToClassMap = new HashMap();

    private final static String[] allJavaTypes = new String[]{
        "boolean",
        "byte",
        "byte[]",
        "char",
        "double",
        "float",
        "int",
        "long",
        "short",
        "java.io.InputStream",
        "java.io.Reader",
        "java.lang.Boolean",
        "java.lang.Byte",
        "java.lang.Double",
        "java.lang.Float",
        "java.lang.Integer",
        "java.lang.Long",
        "java.lang.Short",
        "java.lang.String",
        "java.lang.Object",
        "java.math.BigDecimal",
        "java.math.BigInteger",
        "java.sql.Array",
        "java.sql.Blob",
        "java.sql.Clob",
        "java.sql.Date",
        "java.sql.Ref",
        "java.sql.Time",
        "java.sql.Timestamp",
        "java.util.Date"
    };

    private final static Set numericClasses = new HashSet();

    public static Comparator getTypeComparator() {
        return typeComparator;
    }


    /**
     * Gets the preferred Java Type for an SQL type. It has special logic for
     * handling DECIMAL and NUMERICs with zero decimal places to return the most
     * apppropriate type.
     *
     * @param sqlType       Describe what the parameter does
     * @param size          Describe what the parameter does
     * @param decimalDigits Describe what the parameter does
     * @return The PreferredJavaType value
     */
    public static String getPreferredJavaType(int sqlType, int size, int decimalDigits) {
        if(size == 1) {
            return "boolean";
        }
        if(size <= 2) {
            return "byte";
        }
        if(size <= 4) {
            return "short";
        }
        if(size <= 9) {
            return "int";
        }
        if(size <= 18) {
            return "long";
        }
        if(size >= 19) {
            return "java.math.BigDecimal";
        }
        String result = preferredJavaTypeForSqlType.getString(sqlType);
        if (result == null) {
            result = "java.lang.Object";
        }
        return result;
    }


    /**
     * Gets the preferred Java Type for non-key columns. It has special coding for
     * handling DECIMAL and NUMERICs with zero decimal places.
     *
     * @param sqlType       The SQL type code. Must be one of java.sql.Types
     * @param size          Describe what the parameter does
     * @param decimalDigits Describe what the parameter does
     * @return The Java Type for this non-key column
     */
    public static String getPreferredJavaTypeNoPrimitives(int sqlType, int size, int decimalDigits) {
        String pjt = getPreferredJavaType(sqlType, size, decimalDigits);
        String pjtc = getClassForPrimitive(pjt);
        if (pjtc != null) {
            return pjtc;
        } else {
            return pjt;
        }
    }


    /**
     * Gets the JavaTypes attribute of the Sql2JavaHelper class
     *
     * @param sqlType Describe what the parameter does
     * @return The JavaTypes value
     */
    public static String[] getJavaTypes(int sqlType) {
        String[] result = javaTypesForSqlType.getStrings(sqlType);
        if (result == null) {
            // we're dealing with non SQL'92 types, like for example MSSQL NVARCHAR
            // return all types so the user can select.
            result = allJavaTypes;
        }
        return result;
    }


    public static String getClassForPrimitive(String primitive) {
        return (String) primitiveToClassMap.get(primitive);
    }


    public static boolean isPrimitive(String type) {
        return getClassForPrimitive(type) != null;
    }

    public static boolean isNumericClass(String type) {
        return numericClasses.contains(type);
    }


    public static void overridePreferredJavaTypeForSqlType(int sqlType, String javaType) {
        preferredJavaTypeForSqlType.put(sqlType, javaType);
    }


    public static void overrideAllowedJavaTypesForSqlType(int sqlType, String[] javaTypes) {
        javaTypesForSqlType.put(sqlType, javaTypes);
    }


    private static class IntStringMap extends HashMap {

        public String getString(int i) {
            return (String) get(new Integer(i));
        }

        public String[] getStrings(int i) {
            return (String[]) get(new Integer(i));
        }

        public void put(int i, String s) {
            put(new Integer(i), s);
        }

        public void put(int i, String[] sa) {
            put(new Integer(i), sa);
        }
    }

    static {
        primitiveToClassMap.put("byte", "java.lang.Byte");
        primitiveToClassMap.put("short", "java.lang.Short");
        primitiveToClassMap.put("int", "java.lang.Integer");
        primitiveToClassMap.put("long", "java.lang.Long");
        primitiveToClassMap.put("float", "java.lang.Float");
        primitiveToClassMap.put("boolean", "java.lang.Boolean");
        primitiveToClassMap.put("double", "java.lang.Double");
    }

    static {
        numericClasses.add("java.lang.Byte");
        numericClasses.add("java.lang.Short");
        numericClasses.add("java.lang.Integer");
        numericClasses.add("java.lang.Long");
        numericClasses.add("java.lang.Float");
        numericClasses.add("java.lang.Boolean");
        numericClasses.add("java.lang.Double");
    }

    static {
        javaTypesForSqlType.put(Types.TINYINT, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.String",
            "java.lang.Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
        });
        javaTypesForSqlType.put(Types.SMALLINT, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.String",
            "java.lang.Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
        });
        javaTypesForSqlType.put(Types.INTEGER, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.String",
            "java.lang.Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
        });
        javaTypesForSqlType.put(Types.BIGINT, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.String",
            "java.lang.Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
        });
        javaTypesForSqlType.put(Types.REAL, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.String",
            "java.lang.Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
        });
        javaTypesForSqlType.put(Types.FLOAT, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.String",
            "java.lang.Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
        });
        javaTypesForSqlType.put(Types.DOUBLE, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.String",
            "java.lang.Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
        });
        javaTypesForSqlType.put(Types.DECIMAL, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.String",
            "java.lang.Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
        });
        javaTypesForSqlType.put(Types.NUMERIC, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.String",
            "java.lang.Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
        });
        javaTypesForSqlType.put(Types.BIT, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.String",
            "java.lang.Object",
            "java.math.BigDecimal",
            "java.math.BigInteger"
        });
        javaTypesForSqlType.put(Types.CHAR, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.io.InputStream",
            "java.io.Reader",
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Object",
            "java.lang.Short",
            "java.lang.String",
            "java.math.BigDecimal",
            "java.math.BigInteger",
            "java.sql.Date",
            "java.sql.Time",
            "java.sql.Timestamp",
            "java.util.Date"
        });
        javaTypesForSqlType.put(Types.VARCHAR, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.io.InputStream",
            "java.io.Reader",
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Object",
            "java.lang.Short",
            "java.lang.String",
            "java.math.BigDecimal",
            "java.math.BigInteger",
            "java.sql.Date",
            "java.sql.Time",
            "java.sql.Timestamp",
            "java.util.Date"
        });
        javaTypesForSqlType.put(Types.LONGVARCHAR, new String[]{
            "boolean",
            "byte",
            "double",
            "float",
            "int",
            "long",
            "short",
            "java.io.InputStream",
            "java.io.Reader",
            "java.lang.Boolean",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "java.lang.Integer",
            "java.lang.Long",
            "java.lang.Object",
            "java.lang.Short",
            "java.lang.String",
            "java.math.BigDecimal",
            "java.math.BigInteger",
            "java.sql.Date",
            "java.sql.Time",
            "java.sql.Timestamp",
            "java.util.Date"
        });
        javaTypesForSqlType.put(Types.BINARY, new String[]{
            "byte[]",
            "java.lang.String",
            "java.lang.Object",
            "java.io.InputStream",
            "java.io.Reader"
        });
        javaTypesForSqlType.put(Types.VARBINARY, new String[]{
            "byte[]",
            "java.lang.String",
            "java.lang.Object",
            "java.io.InputStream",
            "java.io.Reader"
        });
        javaTypesForSqlType.put(Types.LONGVARBINARY, new String[]{
            "byte[]",
            "java.lang.String",
            "java.lang.Object",
            "java.io.InputStream",
            "java.io.Reader",
        });
        javaTypesForSqlType.put(Types.DATE, new String[]{
            "java.lang.String",
            "java.lang.Object",
            "java.sql.Date",
            "java.sql.Timestamp",
            "java.util.Date",
        });
        javaTypesForSqlType.put(Types.TIME, new String[]{
            "java.lang.String",
            "java.lang.Object",
            "java.sql.Time",
            "java.sql.Timestamp",
            "java.util.Date",
        });
        javaTypesForSqlType.put(Types.TIMESTAMP, new String[]{
            "java.lang.String",
            "java.lang.Object",
            "java.sql.Date",
            "java.sql.Time",
            "java.sql.Timestamp",
            "java.util.Date",
        });
        javaTypesForSqlType.put(Types.CLOB, new String[]{
            "java.lang.Object",
            "java.sql.Clob"
        });
        javaTypesForSqlType.put(Types.BLOB, new String[]{
            "java.lang.Object",
            "java.sql.Blob"
        });
        javaTypesForSqlType.put(Types.ARRAY, new String[]{
            "java.lang.Object",
            "java.sql.Array"
        });
        javaTypesForSqlType.put(Types.REF, new String[]{
            "java.lang.Object",
            "java.sql.Ref"
        });
        javaTypesForSqlType.put(Types.STRUCT, new String[]{
            "java.lang.Object"
        });
        javaTypesForSqlType.put(Types.JAVA_OBJECT, new String[]{
            "java.lang.Object"
        });
    }

    static {
        preferredJavaTypeForSqlType.put(Types.TINYINT, "byte");
        preferredJavaTypeForSqlType.put(Types.SMALLINT, "short");
        preferredJavaTypeForSqlType.put(Types.INTEGER, "int");
        preferredJavaTypeForSqlType.put(Types.BIGINT, "long");
        preferredJavaTypeForSqlType.put(Types.REAL, "float");
        preferredJavaTypeForSqlType.put(Types.FLOAT, "double");
        preferredJavaTypeForSqlType.put(Types.DOUBLE, "double");
        preferredJavaTypeForSqlType.put(Types.DECIMAL, "java.math.BigDecimal");
        preferredJavaTypeForSqlType.put(Types.NUMERIC, "java.math.BigDecimal");
        preferredJavaTypeForSqlType.put(Types.BIT, "boolean");
        preferredJavaTypeForSqlType.put(Types.CHAR, "java.lang.String");
        preferredJavaTypeForSqlType.put(Types.VARCHAR, "java.lang.String");
        // according to resultset.gif, we should use java.io.Reader, but String is more convenient for EJB
        preferredJavaTypeForSqlType.put(Types.LONGVARCHAR, "java.lang.String");
        preferredJavaTypeForSqlType.put(Types.BINARY, "byte[]");
        preferredJavaTypeForSqlType.put(Types.VARBINARY, "byte[]");
        preferredJavaTypeForSqlType.put(Types.LONGVARBINARY, "java.io.InputStream");
        preferredJavaTypeForSqlType.put(Types.DATE, "java.sql.Date");
        preferredJavaTypeForSqlType.put(Types.TIME, "java.sql.Time");
        preferredJavaTypeForSqlType.put(Types.TIMESTAMP, "java.sql.Timestamp");
        preferredJavaTypeForSqlType.put(Types.CLOB, "java.sql.Clob");
        preferredJavaTypeForSqlType.put(Types.BLOB, "java.sql.Blob");
        preferredJavaTypeForSqlType.put(Types.ARRAY, "java.sql.Array");
        preferredJavaTypeForSqlType.put(Types.REF, "java.sql.Ref");
        preferredJavaTypeForSqlType.put(Types.STRUCT, "java.lang.Object");
        preferredJavaTypeForSqlType.put(Types.JAVA_OBJECT, "java.lang.Object");
    }
}