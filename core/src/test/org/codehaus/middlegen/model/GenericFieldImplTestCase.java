package org.codehaus.middlegen.model;

import org.codehaus.rdbms.Column;
import org.codehaus.rdbms.impl.DatabaseNameConverter;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.3 $
 */
public class GenericFieldImplTestCase extends MockObjectTestCase  {
    private Mock column;
    private Mock databaseNameConverter;
    private GenericFieldImpl genericField;

    protected void setUp() throws Exception {
        column = new Mock(Column.class);
        databaseNameConverter = new Mock(DatabaseNameConverter.class);
        genericField = new GenericFieldImpl((Column)column.proxy(), (DatabaseNameConverter)databaseNameConverter.proxy());
    }

    public void testShouldGetColumnNameAndConvertItForGetVariableName() {
        column.expects(once()).method("getSqlName").withNoArguments().will(returnValue("foo"));
        databaseNameConverter.expects(once()).method("columnNameToVariableName").with(eq("foo")).will(returnValue("bar"));

        assertEquals("bar", genericField.getVariableName());
        assertEquals("bar", genericField.getVariableName());
    }

    public void testShouldBypassColumnAndConverterWhenNameIsExplicitlySet() {
        genericField.setVariableName("bar");
        assertEquals("bar", genericField.getVariableName());
    }

}