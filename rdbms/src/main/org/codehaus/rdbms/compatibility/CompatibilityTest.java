package org.codehaus.rdbms.compatibility;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.types.Path;
import org.codehaus.rdbms.SchemaFactory;
import org.codehaus.rdbms.Table;
import org.codehaus.rdbms.impl.DefaultDatabaseNameConverter;
import org.codehaus.rdbms.impl.PicoColumnFactory;
import org.codehaus.rdbms.impl.PicoTableFactory;
import org.codehaus.rdbms.jdbc.JDBCConnectionFactory;
import org.codehaus.rdbms.jdbc.JDBCSchemaFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.net.MalformedURLException;

/**
 * This standalone class can be used to verify compatibility with a certain
 * database and JDBC driver.
 *
 * TODO: Load the rest of the stuff in a separate classloader,
 * with the driver in it
 *
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.3 $
 */
public class CompatibilityTest {
    private final Properties properties;

    public CompatibilityTest(Properties properties) {
        this.properties = properties;
    }

    public void testCompatibility() throws Exception {
        try {
            setUpDatabase();
            List tables = loadTables();

            checkIntegrity(tables);
            System.out.println("The database/driver is compatible!");
        } finally {
            tearDownDatabase();
        }
    }

    private void checkIntegrity(List tables) {
//        if (tables.size() != 9) throw new IllegalStateException("There should be 9 tables in the database, but there were " + tables.size());
        int i = tables.size() - 9;
        Table PP = getTable("PP", i+0, tables);
        assertEqualsIgnoreCase("Columns in AA", "[A]", PP.getColumns().toString());
        assertEqualsIgnoreCase("Primary key", "[A]", PP.getPrimaryKey().toString());
        assertEqualsIgnoreCase("Foreign keys", "[]", PP.getForeignKeys().toString());

        Table QQ = getTable("QQ", i+1, tables);
        assertEqualsIgnoreCase("Columns in QQ", "[M]", QQ.getColumns().toString());
        assertEqualsIgnoreCase("Primary key", "[M]", QQ.getPrimaryKey().toString());
        assertEqualsIgnoreCase("Foreign keys", "[[M]-[A]]", QQ.getForeignKeys().toString());

        Table TT = getTable("TT", i+2, tables);
        assertEqualsIgnoreCase("Columns in TT", "[A]", TT.getColumns().toString());
        assertEqualsIgnoreCase("Primary key", "[A]", TT.getPrimaryKey().toString());
        assertEqualsIgnoreCase("Foreign keys", "[]", TT.getForeignKeys().toString());

        Table UU = getTable("UU", i+3, tables);
        assertEqualsIgnoreCase("Columns in UU", "[M, N]", UU.getColumns().toString());
        assertEqualsIgnoreCase("Primary key", "[M, N]", UU.getPrimaryKey().toString());
        assertEqualsIgnoreCase("Foreign keys", "[[M]-[A], [N]-[A]]", UU.getForeignKeys().toString());

        Table VV = getTable("VV", i+4, tables);
        assertEqualsIgnoreCase("Columns in VV", "[A, B]", VV.getColumns().toString());
        assertEqualsIgnoreCase("Primary key", "[A, B]", VV.getPrimaryKey().toString());
        assertEqualsIgnoreCase("Foreign keys", "[]", VV.getForeignKeys().toString());

        Table WW = getTable("WW", i+5, tables);
        assertEqualsIgnoreCase("Columns in WW", "[M, N]", WW.getColumns().toString());
        assertEqualsIgnoreCase("Primary key", "[M, N]", WW.getPrimaryKey().toString());
        assertEqualsIgnoreCase("Foreign keys", "[[M, N]-[A, B]]", WW.getForeignKeys().toString());

        Table XX = getTable("XX", i+6, tables);
        assertEqualsIgnoreCase("Columns in XX", "[A, B, C, D]", XX.getColumns().toString());
        assertEqualsIgnoreCase("Primary key", "[A, B]", XX.getPrimaryKey().toString());
        assertEqualsIgnoreCase("Foreign keys", "[]", XX.getForeignKeys().toString());

        Table YY = getTable("YY", i+7, tables);
        assertEqualsIgnoreCase("Columns in YY", "[M, N, O, P]", YY.getColumns().toString());
        assertEqualsIgnoreCase("Primary key", "[M, N]", YY.getPrimaryKey().toString());
        assertEqualsIgnoreCase("Foreign keys", "[[M, N]-[A, B], [O, P]-[A, B]]", YY.getForeignKeys().toString());

        Table ZZ = getTable("ZZ", i+8, tables);
        assertEqualsIgnoreCase("Columns in ZZ", "[A, B, C, D]", ZZ.getColumns().toString());
        assertEqualsIgnoreCase("Primary key", "[A, B]", ZZ.getPrimaryKey().toString());
        assertEqualsIgnoreCase("Foreign keys", "[]", ZZ.getForeignKeys().toString());
    }

    private Table getTable(String name, int index, List tables) {
        Table table = (Table) tables.get(index);
        assertEqualsIgnoreCase("Table at index " + index, name.toLowerCase(), table.getSqlName().toLowerCase());
        return table;
    }

    private void assertEqualsIgnoreCase(String message, String expected, String actual) {
        if (!expected.equalsIgnoreCase(actual)) {
            throw new IllegalStateException(message + ": expected '" + expected.toString() + "', was '" + actual.toString() + "'");
        }
    }

    private List loadTables() throws MalformedURLException {
        SchemaFactory schemaFactory = createSchemaFactory();
        PicoTableFactory picoTableFactory = new PicoTableFactory();

        PicoColumnFactory columnFactory = new PicoColumnFactory();
        List tables = schemaFactory.loadTables(picoTableFactory, columnFactory);
        return tables;
    }

    private void setUpDatabase() throws Exception {
        runScript(getProperty("middlegen.database.setup.script"));
    }

    private void tearDownDatabase() throws Exception {
        try {
            runScript(getProperty("middlegen.database.teardown.script"));
        } catch (Exception e) {
            System.err.println("WARNING: " + e.getMessage());
        }
    }

    private void runScript(String scriptPath) {
        File script = new File(scriptPath);
        SQLExecEx sql = new SQLExecEx(script);
        try {
            sql.execute();
        } catch (BuildException e) {
            if ("Source file does not exist!".equals(e.getMessage())) {
                throw new IllegalStateException("Can't find " + scriptPath);
            } else {
                throw e;
            }
        }
    }

    private String getProperty(String property) {
        String result = properties.getProperty(property);
        if (result == null) {
            throw new RuntimeException("Missing property:" + property);
        }
        return result;
    }

    private SchemaFactory createSchemaFactory() throws MalformedURLException {

        JDBCConnectionFactory connectionFactory = new JDBCConnectionFactory();
        connectionFactory.setClasspath(getProperty("middlegen.database.driver.classpath"));
        connectionFactory.setDriver(getProperty("middlegen.database.driver"));
        connectionFactory.setUrl(getProperty("middlegen.database.url"));
        connectionFactory.setUserid(getProperty("middlegen.database.userid"));
        connectionFactory.setPassword(getProperty("middlegen.database.password"));

        JDBCSchemaFactory schemaFactory = new JDBCSchemaFactory(new DefaultDatabaseNameConverter(), connectionFactory);
        schemaFactory.setSchema(getProperty("middlegen.database.schema"));
        schemaFactory.setCatalog(getProperty("middlegen.database.catalog"));

        return schemaFactory;
    }

    private final class SQLExecEx extends SQLExec {
        public SQLExecEx(File script) {

            // Basic Ant setup
            Project project = new Project();
            setProject(project);
            project.init();
            setTaskName("sql");

            String classpath = getProperty("middlegen.database.driver.classpath");
            String driver = getProperty("middlegen.database.driver");
            String url = getProperty("middlegen.database.url");
            String userid = getProperty("middlegen.database.userid");
            String password = getProperty("middlegen.database.password");

            // Configuration
            setClasspath(new Path(project, classpath));
            setDriver(driver);
            setUrl(url);
            setUserid(userid);
            setPassword(password);
            setSrc(script);
            setPrint(true);
            setAutocommit(true);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            usage();
        }
        Properties properties = new Properties();
        properties.load(new FileInputStream(args[0]));
        CompatibilityTest compatibilityTest = new CompatibilityTest(properties);
        compatibilityTest.testCompatibility();
    }

    private static void usage() {
        System.out.println("Usage: " + CompatibilityTest.class.getName() + " path_to_properties");
    }

}