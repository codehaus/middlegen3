package org.codehaus.rdbms.jdbc;

import junit.framework.TestCase;

import java.sql.Types;

/**
 * Tests the conversion logic. If you believe any of these tests are incorrect,
 * please file a bug report.
 *
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.3 $
 */
public class SqlToJavaTestCase extends TestCase {
    public void testShouldConvertToBooleanForAnyTypeOfLengthOne() {
        assertReturnsTypeForSize("boolean", 1);
    }

    public void testShouldConvertToByteForAnyTypeForAnyTypeOfLengthTwo() {
        assertReturnsTypeForSize("byte", 2);
    }

    public void testShouldConvertToShortForAnyNumericTypeOfLengthThreeOrFour() {
        assertEquals("short", Sql2JavaHelper.getPreferredJavaType(Types.BIGINT, 3, 0));
        assertEquals("short", Sql2JavaHelper.getPreferredJavaType(Types.BIGINT, 3, 0));
        assertEquals("short", Sql2JavaHelper.getPreferredJavaType(Types.ARRAY, 3, 0));
        assertEquals("short", Sql2JavaHelper.getPreferredJavaType(Types.ARRAY, 3, 0));
        assertEquals("short", Sql2JavaHelper.getPreferredJavaType(Types.ARRAY, 3, 0));
        assertEquals("short", Sql2JavaHelper.getPreferredJavaType(Types.ARRAY, 3, 0));
        assertEquals("short", Sql2JavaHelper.getPreferredJavaType(Types.ARRAY, 3, 0));
    }

    private void assertReturnsTypeForSize(String expectedType, int size) {
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.ARRAY, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.BIGINT, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.BINARY, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.BIT, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.BLOB, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.BOOLEAN, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.CHAR, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.CLOB, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.DATALINK, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.DATE, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.DECIMAL, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.DISTINCT, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.DOUBLE, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.FLOAT, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.INTEGER, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.JAVA_OBJECT, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.LONGVARBINARY, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.LONGVARCHAR, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.NULL, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.NUMERIC, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.OTHER, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.REAL, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.REF, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.SMALLINT, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.STRUCT, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.TIME, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.TIMESTAMP, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.TINYINT , size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.VARBINARY, size, 0));
        assertEquals(expectedType, Sql2JavaHelper.getPreferredJavaType(Types.VARCHAR, size, 0));
    }
}