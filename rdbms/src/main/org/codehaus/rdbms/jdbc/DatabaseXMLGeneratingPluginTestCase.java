package org.codehaus.rdbms.jdbc;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.types.Path;
import org.codehaus.rdbms.SchemaFactory;
import org.codehaus.rdbms.impl.DefaultDatabaseNameConverter;
import org.codehaus.rdbms.impl.PicoTableFactory;
import org.codehaus.rdbms.impl.RdbmsTableFromJdbcMetadataProvider;
import org.generama.MetadataProvider;
import org.generama.tests.AbstractXMLGeneratingPluginTestCase;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.io.File;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public abstract class DatabaseXMLGeneratingPluginTestCase extends AbstractXMLGeneratingPluginTestCase {
    private RdbmsTableFromJdbcMetadataProvider rdbmsTableMetadataProvider;

    /**
     * Creates tables in the test database
     */
    protected void setUp() throws Exception {
        PicoTableFactory picoTableFactory = new PicoTableFactory();
        SchemaFactory schemaFactory = createSchemaFactory();
        rdbmsTableMetadataProvider = new RdbmsTableFromJdbcMetadataProvider(picoTableFactory, schemaFactory);

        super.setUp();
        runScript(getProperty("middlegen.database.setup.script"));
    }

    /**
     * Deletes tables from the test database
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        runScript(getProperty("middlegen.database.teardown.script"));
    }

    private void runScript(String scriptPath) {
        File script = new File(scriptPath);
        SQLExecEx sql = new SQLExecEx(script);
        sql.execute();
    }

    protected String getProperty(String property) {
        String result = System.getProperty(property);
        assertNotNull("Missing system property:" + property, result);
        return result;
    }

    private SchemaFactory createSchemaFactory() {

        JDBCConnectionFactory connectionFactory = new JDBCConnectionFactory();
        connectionFactory.setDriver(getProperty("middlegen.database.driver"));
        connectionFactory.setUrl(getProperty("middlegen.database.url"));
        connectionFactory.setUserid(getProperty("middlegen.database.userid"));
        connectionFactory.setPassword(getProperty("middlegen.database.password"));

        JDBCSchemaFactory schemaFactory = new JDBCSchemaFactory(new DefaultDatabaseNameConverter(), connectionFactory);
        schemaFactory.setSchema(getProperty("middlegen.database.schema"));
        schemaFactory.setCatalog(getProperty("middlegen.database.catalog"));

        return schemaFactory;
    }

    protected MetadataProvider createMetadataProvider() {
        return rdbmsTableMetadataProvider;
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
        }
    }
}
