package org.codehaus.rdbms.impl;

import junit.framework.TestCase;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public class DefaultDatabaseNameConverterTestCase extends TestCase {
    private DatabaseNameConverter databaseNameConverter = new DefaultDatabaseNameConverter();

    public void testDbNameToVariableName1() {
		String convert = "FOOVER_BART_ZAPP";
		assertEquals(convert, "FooverBartZapp", databaseNameConverter.tableNameToVariableName(convert));
	}

	public void testDbNameToVariableName2() {
		String convert = "foover_bart_zapp";
		assertEquals(convert, "FooverBartZapp", databaseNameConverter.tableNameToVariableName(convert));
	}

	public void testDbNameToVariableName3() {
		String convert = "fooverbartzapp";
		assertEquals(convert, "Fooverbartzapp", databaseNameConverter.tableNameToVariableName(convert));
	}

	public void testDbNameToVariableName4() {
		String convert = "fooverBartZapp";
		assertEquals(convert, "FooverBartZapp", databaseNameConverter.tableNameToVariableName(convert));
	}

	public void testDbNameToVariableName5() {
		String convert = "FOOVER_bartZapp";
		assertEquals(convert, "FooverBartZapp", databaseNameConverter.tableNameToVariableName(convert));
	}

	public void testDbNameToVariableName6() {
		String convert = "fooverBart_ZAPP";
		assertEquals(convert, "FooverBartZapp", databaseNameConverter.tableNameToVariableName(convert));
	}
}

