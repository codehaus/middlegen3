package org.codehaus.rdbms.jdbc;

import org.codehaus.rdbms.Column;
import org.codehaus.rdbms.ColumnFactory;
import org.codehaus.rdbms.MutableColumn;
import org.codehaus.rdbms.MutableTable;
import org.codehaus.rdbms.SchemaFactory;
import org.codehaus.rdbms.TableFactory;
import org.codehaus.rdbms.impl.DatabaseNameConverter;
import org.codehaus.rdbms.impl.ForeignKeyImpl;
import org.codehaus.rdbms.impl.PrimaryKeyImpl;
import org.codehaus.rdbms.impl.Util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Schema Factory that reads metadata from an existing database using JDBC.
 *
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public class JDBCSchemaFactory implements SchemaFactory {
    private final DatabaseNameConverter databaseNameConverter;
    private final ConnectionFactory connectionFactory;

    private String catalog;
    private String schema;
    private DatabaseInfo databaseInfo;

    private DatabaseMetaData metaData;

    private String[] types = null;

    private Connection connection;
    private int foreignKeyCount = 0;

    public JDBCSchemaFactory(DatabaseNameConverter databaseNameConverter, ConnectionFactory connectionFactory) {
        this.databaseNameConverter = databaseNameConverter;
        this.connectionFactory = connectionFactory;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public List loadTables(TableFactory tableFactory, ColumnFactory columnFactory) throws DatabaseException {
        if (tableFactory.getTables().isEmpty()) {
            try {
                tune(getConnection().getMetaData());

                addTables(tableFactory);
                populateTables(tableFactory, columnFactory);

            } catch (SQLException e) {
                throw new DatabaseException(e.getMessage());
            } finally {
                closeConnection();
            }
        }
        return tableFactory.getTables();
    }

    private void addTables(TableFactory tableFactory) throws DatabaseException {

        // We're keeping track of the table names so we can detect if a table
        // occurs in different schemas, and report a warning.
        Map tableSchemaMap = new HashMap();

        /*
        Make a copy of all the tables. There might be some (if preconfigured),
        or none.
        */
        List tables = new ArrayList(tableFactory.getTables());

        if (tables.isEmpty()) {
            // There were no preconfigured tables. Add null, so we get into the loop once.
            tables.add(null);
        }

        for (Iterator tableIterator = tables.iterator(); tableIterator.hasNext();) {
            String tableName;

            MutableTable table = (MutableTable) tableIterator.next();
            if (table == null) {
                // we'll be in this loop only once.
                tableName = null;
            } else {
                // we'll be in this loop once for each preconfigured table.
                tableName = table.getSqlName();
            }

            String schemaName = null;
            ResultSet tableRs = null;
            try {
                // Here, tableName will be null, or have a value, in which case we're
                // looping for each table.
                tableRs = getMetaData().getTables(catalog, schema, tableName, types);

                while (tableRs.next()) {
                    // If we didn't already have a Table instance, make one here and
                    // add it to the model
                    if (table == null) {
                        String tableType = tableRs.getString("TABLE_TYPE");
                        if ("TABLE".equals(tableType)) {
                            // it's a regular table
                            String currentTableName = tableRs.getString("TABLE_NAME");
                            table = tableFactory.createTable(currentTableName);
                        }
                    }

                    if (table != null) {
                        // BUG [ 596044 ] Case in table names - relationships
                        // Update the tableElement with the name reported by the resultset.
                        // The case might not be the same, and some drivers want correct case
                        // in getCrossReference/getExportedKeys which we'll call later.
                        table.setSqlName(tableRs.getString("TABLE_NAME"));
                        table.setSchemaName(tableRs.getString("TABLE_SCHEM"));

                        // Test for tables in different schemas
                        String alreadySchema = (String) tableSchemaMap.get(table.getSqlName());
                        if (alreadySchema != null) {
                            throw new DatabaseException(
                                    "The table named " + table.getSqlName() + " was found both in the schema " +
                                    "named " + alreadySchema + " and in the schema named " + schemaName + ". " +
                                    "You have to specify schema=\"something\"."
                            );
                        }
                        tableSchemaMap.put(table.getSqlName(), schemaName);

                        // Some more schema sanity testing
                        if (!Util.equals(schema, schemaName) && schemaName != null) {
                            System.err.println(
                                    "WARNING: The table named " + table.getSqlName() + " was found in the schema " +
                                    "named \"" + schemaName + "\". However, a specific schema" +
                                    "wasn't specified. You should consider specifying " +
                                    "schema=\"" + schemaName + "\" in the middlegen task."
                            );
                        }
                    }
                    // Set table to null, so a new one gets created in the next iteration.
                    table = null;
                }
            } catch (SQLException e) {
                reportProblem(e);
                throw new DatabaseException("Couldn't get list of tables from database. Probably a JDBC driver problem.");
            } finally {
                try {
                    tableRs.close();
                } catch (SQLException ignore) {
                } catch (NullPointerException ignore) {
                }
            }
        }
        if (tableFactory.getTables().isEmpty()) {
            reportProblem(null);
            throw new DatabaseException("We successfully connected to the database, but " +
                    "couldn't find any tables. Perhaps the specified schema or catalog is wrong? -Or maybe " +
                    "there aren't any tables in the database at all?");
        }
    }


    private void populateTables(TableFactory tableFactory, ColumnFactory columnFactory) throws DatabaseException {
        try {

            for (Iterator tableIterator = tableFactory.getTables().iterator(); tableIterator.hasNext();) {
                MutableTable table = (MutableTable) tableIterator.next();
                addColumns(table, columnFactory);
            }
            for (Iterator tableIterator = tableFactory.getTables().iterator(); tableIterator.hasNext();) {
                MutableTable table = (MutableTable) tableIterator.next();
                addRelations(tableFactory, table);
            }
            // warn if there are no relations
            if (foreignKeyCount == 0) {
                System.err.println
                        ("WARNING: couldn't find any foreign key references between any tables. " +
                        "This may be intentional from the design of the database, but it may " +
                        "also be because you have incorrectly defined the foreign keys. " +
                        "It could also be because the JDBC driver you're using doesn't correctly implement DatabaseMetaData. " +
                        "See the samples (for an example on how to define foreign keys) " +
                        "and verify with the driver vendor that the driver correctly implements DatabaseMetaData."
                        );
            }

            addMany2ManyRelations();

        } catch (SQLException e) {
            throw new DatabaseException("Database problem:" + e.getMessage());
        }
    }

    /**
     * Adds columns to the table.
     *
     * @param table
     * @throws SQLException
     */
    private void addColumns(MutableTable table, ColumnFactory columnFactory) throws DatabaseException, SQLException {
        // get the primary keys
        Collection primaryKeys = new ArrayList();
        ResultSet primaryKeyRs = getMetaData().getPrimaryKeys(catalog, schema, table.getSqlName());
        while (primaryKeyRs.next()) {
            String columnName = primaryKeyRs.getString("COLUMN_NAME");
            primaryKeys.add(columnName);
        }
        primaryKeyRs.close();

        // Keep track of the indexed columns
        List indices = new ArrayList();
        List indexNames = new ArrayList();

        // Keep track of the unique columns
        Collection uniques = new ArrayList();

        ResultSet indexRs = null;
        try {
            indexRs = getMetaData().getIndexInfo(catalog, schema, table.getSqlName(), false, true);
            while (indexRs.next()) {
                String columnName = indexRs.getString("COLUMN_NAME");
                String indexName = indexRs.getString("INDEX_NAME");
                if (columnName != null) {
                    indices.add(columnName);
                    indexNames.add(indexName);
                }
                boolean unique = !indexRs.getBoolean("NON_UNIQUE");
                if (unique) {
                    uniques.add(columnName);
                }
            }
        } catch (Throwable t) {
            // Bug #604761 Oracle getIndexInfo() needs major grants
            // http://sourceforge.net/tracker/index.php?func=detail&aid=604761&group_id=36044&atid=415990
        } finally {
            if (indexRs != null) {
                indexRs.close();
            }
        }

        // get the columns
        ResultSet columnRs = getMetaData().getColumns(catalog, schema, table.getSqlName(), null);
        while (columnRs.next()) {
            int sqlType = columnRs.getInt("DATA_TYPE");
            String sqlTypeName = columnRs.getString("TYPE_NAME");
            String columnName = columnRs.getString("COLUMN_NAME");
            String columnDefaultValue = columnRs.getString("COLUMN_DEF");
            // if columnNoNulls or columnNullableUnknown assume "not nullable"
            boolean isNullable = (DatabaseMetaData.columnNullable == columnRs.getInt("NULLABLE"));
            int size = columnRs.getInt("COLUMN_SIZE");
            int decimalDigits = columnRs.getInt("DECIMAL_DIGITS");

            boolean isPk = primaryKeys.contains(columnName);
            final int indexIndex = indices.indexOf(columnName);
            String indexName = null;
            if(indexIndex != -1) {
                indexName = (String) indexNames.get(indexIndex);
            }
            boolean isUnique = uniques.contains(columnName);

            MutableColumn column = columnFactory.createColumn(table, columnName);

            column.setSqlType(sqlType);
            column.setSqlTypeName(sqlTypeName);
            column.setSqlName(columnName);
            column.setSize(size);
            column.setDecimalDigits(decimalDigits);
            column.setPk(isPk);
            column.setNullable(isNullable);
            column.setIndexed(indexIndex != -1);
            column.setIndexName(indexName);
            column.setUnique(isUnique);
            column.setDefaultValue(columnDefaultValue);

            table.addColumn(column);
        }
        columnRs.close();

        // In case none of the columns were primary keys, issue a warning.
        if (primaryKeys.size() == 0) {
            System.out.println
                    ("WARNING: The JDBC driver didn't report any primary key columns in " + table.getSqlName());
        }
    }


    /**
     * Establishes relations between tables. This is done by first quering what
     * tables are related (for the sake of speed), then by querying how they are
     * related. If there are more than one relation between two tables, the
     * relations must be given different names. In that case, the relation names
     * will be suffixed with "By(fk0)[And()fki]*" <pre>
     *
     *  pk       fk    relation
     * table    table   name
     *
     * (1)
     * +---+    +---+
     * | P |    | Q |
     * +---+    +---+
     * |*a |----| m | q
     * +---+    +---+
     *
     * (2)
     * +---+    +---+
     * | R |    | S |
     * +---+    +---+
     * |*a |----| m | sByM (Illegal in WLS. Relatiions must map to full pk)
     * |*b |----| n | sByN (Illegal in WLS. Relatiions must map to full pk)
     * +---+    +---+
     *
     * (3)
     * +---+    +---+
     * | T |    | U |
     * +---+    +---+
     * |   | ___| m | uByM
     * |*b |<___| n | uByN
     * +---+    +---+
     *
     * (4)
     * +---+    +---+
     * | V |    | W |
     * +---+    +---+
     * |*a |\__/| m | w
     * |*b |/  \| n |
     * +---+    +---+
     *
     * (5)
     * +---+    +---+
     * | X |    | Y |
     * +---+    +---+
     * |*a |\__/| m | yByMAndN
     * |*b |/| \| n |
     * |   | L_/| o | yByOAndP
     * |   |   \| p |
     * +---+    +---+
     * </pre>
     *
     * @param pkTable Describe the method parameter
     * @exception org.codehaus.rdbms.jdbc.DatabaseException if we can't determine relationships
     *      because of a logical error
     * @exception java.sql.SQLException if a database exception occurs
     */
    protected void addRelations(TableFactory tableFactory, MutableTable pkTable) throws DatabaseException, SQLException {
        short fkCounter = 0;

        // first get all the relationships dictated by the database schema

        ResultSet exportedKeyRs = getMetaData().getExportedKeys(catalog, schema, pkTable.getSqlName());
        while (exportedKeyRs.next()) {
            String fkTableName = exportedKeyRs.getString("FKTABLE_NAME");
            // Let's see if that is one of the tables we know about
            if (tableFactory.getTable(fkTableName) != null) {

                String pkColumnName = exportedKeyRs.getString("PKCOLUMN_NAME");
                String fkColumnName = exportedKeyRs.getString("FKCOLUMN_NAME");
                String fkName = exportedKeyRs.getString("FK_NAME");
                short keySeq = exportedKeyRs.getShort("KEY_SEQ");
                // Warn if there is a relation to a column which is not a pk

                if (keySeq == 0) {
                    fkCounter++;
                }
                if (fkName == null) {
                    // Invent a sensible name
                    fkName = fkTableName + "-fk--pk-" + pkTable.getSqlName() + "__" + String.valueOf(fkCounter);
                }

                addCrossref(tableFactory, pkTable, pkColumnName, fkTableName, fkColumnName, fkName);
            }
        }
        exportedKeyRs.close();
    }


    /**
     * Gets the SchemaName attribute of the MiddlegenPopulator object
     *
     * @return The SchemaName value
     */
    String getSchemaName() {
        return schema;
    }

    private Connection getConnection() throws DatabaseException {
        if (connection == null) {
            connection = connectionFactory.getConnection();
        }
        return connection;
    }

    private DatabaseMetaData getMetaData() throws DatabaseException, SQLException {
        if (metaData == null) {
            metaData = getConnection().getMetaData();
        }
        return metaData;
    }

    private void addCrossref(TableFactory tableFactory, MutableTable pkTable, String pkColumnName, String fkTableName, String fkColumnName, String fkName) {

        MutableTable fkTable = tableFactory.getTable(fkTableName);

        PrimaryKeyImpl primaryKey = (PrimaryKeyImpl) pkTable.getPrimaryKey();

        // Get the named foreign key. Create it if it doesn't exist.
        ForeignKeyImpl foreignKey = (ForeignKeyImpl) fkTable.getForeignKey(fkName);
        if (foreignKey == null) {
            foreignKey = new ForeignKeyImpl(fkTable, fkName, primaryKey, databaseNameConverter);
            foreignKeyCount++;
            fkTable.addForeignKey(foreignKey);
        }

        Column pkColumn = pkTable.getColumn(pkColumnName);
        if (!pkColumn.isPrimaryKey()) {
            System.out.println("WARNING: In the relation involving foreign key column " + fkTableName + "(" + fkColumnName +
                    ") and primary key column " + pkTable.getSqlName() + "(" + pkColumnName + ") the primary key column isn't " +
                    "declared as a primary key column in the database. This may cause errors later on."
            );
        }

        // Also, mark the fk field as an fk. It hasn't been done before.
        MutableColumn fkColumn = (MutableColumn) fkTable.getColumn(fkColumnName);
        fkColumn.setFk(true);

        // This was added after the introduction of Key, ForeignKey and PrimaryKey.
        // Most of the above code sohuld probably be removed.
        foreignKey.addReference(fkColumn, pkColumn);
    }


    /**
     * Tunes the settings depending on database.
     *
     * @param metaData Describe what the parameter does
     * @exception java.sql.SQLException Describe the exception
     */
    private void tune(DatabaseMetaData metaData) throws SQLException {
        String databaseProductName = metaData.getDatabaseProductName();
        String databaseProductVersion = metaData.getDatabaseProductVersion();
        String driverName = metaData.getDriverName();
        String driverVersion = metaData.getDriverVersion();

        databaseInfo = new DatabaseInfo(
                databaseProductName,
                databaseProductVersion,
                driverName,
                driverVersion
        );

        // ORACLE TUNING
        if (databaseProductName.toLowerCase().indexOf("oracle") != -1) {
            // Provided by Michael Szlapa to make it work with Oracle 8.1.6

            // capitalize catalogue
            if (catalog != null) {
                catalog = catalog.toUpperCase();
            }

            // usually the access rights are set up so that you can only query your schema
            // ie. schema = username
            if (schema != null) {
                schema = schema.toUpperCase();
            }
            // null will also retrieve objects for which only synonyms exists, but this objects will not
            // be successfully processed anyway - did not check why  -probably columns not retrieved
            types = new String[]{"TABLE", "VIEW"};
        }

        // MSSQL TUNING
        // TODO David Cowan: check the driverName instead. All drivers for MSSQL
        // will probably not behave in the same way. Possibly add other types too...
        if (databaseProductName.toLowerCase().indexOf("microsoft") != -1) {
            // UNIQUEIDENTIFIER type will return BINARY with the XXXXX driver
            Sql2JavaHelper.overridePreferredJavaTypeForSqlType(Types.BINARY, "java.lang.String");

            // possibly other nonstandard types here
            // Sql2JavaHelper.overridePreferredJavaTypeForSqlType(???, ???);
            // Sql2JavaHelper.overrideAllowedJavaTypesForSqlType( ????, new String[]{???,???,???});
        }
        if (databaseProductName.toLowerCase().indexOf("hsql") != -1) {
            // hsqldb is ok now. no hacks needed anymore.
        }
    }


    /**
     * Establishes m:n relationships
     */
    private void addMany2ManyRelations() {
/*
      List relations = _middlegen.getRelations();
      int one2manyCount = relations.size();
      for (int i = 0; i < one2manyCount - 1; i++) {
         Relation firstRelation = (Relation)relations.get(i);
         RelationshipRole firstRole = firstRelation.getLeftRole();
         LogFactory.getLog(JDBCDatabaseFactory.class).debug("first:" + firstRole.getName());
         for (int j = 0; j < one2manyCount; j++) {
            Relation secondRelation = (Relation)relations.get(j);
            RelationshipRole secondRole = secondRelation.getLeftRole();
            LogFactory.getLog(JDBCDatabaseFactory.class).debug("second:" + secondRole.getName());
            if (firstRole.getTarget() == secondRole.getTarget() && firstRole.getTarget() != null) {
               // OK this is a potential m:n. See if it's really wanted.
               if (isWanted(firstRole.getOrigin().getSqlName(), firstRole.getTarget().getSqlName(), secondRole.getOrigin().getSqlName())) {
                  Collection m2mElements = getM2Ms(firstRole.getOrigin().getSqlName(), secondRole.getOrigin().getSqlName());
                  String relationSuffix = null;
                  String fkRoleSuffix = null;
                  if (m2mElements.size() > 1) {
                     // There are several relations for those extremities. Use join table in suffix
                     // flights-persons-via-reservations-by-a-b-c-d
                     relationSuffix = "-via-" + firstRole.getTarget().getSqlName() + getRelationSuffix(firstRelation, secondRelation);
                     fkRoleSuffix = "_via_" + firstRole.getTarget().getSqlName() + getRelationSuffix(firstRelation, secondRelation);
                     fkRoleSuffix = DbNameConverter.getInstance().columnNameToVariableName(fkRoleSuffix);
                  }
                  else {
                     // Only one. Don't use join table in suffix
                     relationSuffix = getRelationSuffix(firstRelation, secondRelation);
                     fkRoleSuffix = getRelationSuffix(firstRelation, secondRelation);
                  }
                  Relation m2m = new Relation(
                        firstRole.getOrigin(),
                        firstRole.getColumnMaps(),
                        secondRole.getOrigin(),
                        secondRole.getColumnMaps(),
                        firstRole.getTarget(),
                        relationSuffix,
                        fkRoleSuffix
                        );
                  _middlegen.addRelation(m2m);
               }
            }
         }
      }
*/
    }

    private void reportProblem(SQLException e) throws DatabaseException {
        // schemaRs and catalogRs are only used for error reporting if we get an exception
        ResultSet schemaRs = null;
        ResultSet catalogRs = null;
        String nl = System.getProperty("line.separator");
        StringBuffer sb = new StringBuffer(nl);
        // Let's give the user some feedback. The exception
        // is probably related to incorrect schema configuration.
        sb.append("Configured schema:").append(schema).append(nl);
        sb.append("Configured catalog:").append(catalog).append(nl);

        try {
            schemaRs = getMetaData().getSchemas();
            sb.append("Available schemas:").append(nl);
            while (schemaRs.next()) {
                sb.append("  ").append(schemaRs.getString("TABLE_SCHEM ")).append(nl);
            }
        } catch (SQLException e2) {
            sb.append("  ?? Couldn't get schemas ??").append(nl);
        } finally {
            try {
                schemaRs.close();
            } catch (Exception ignore) {
            }
        }

        try {
            catalogRs = getMetaData().getCatalogs();
            sb.append("Available catalogs:").append(nl);
            while (catalogRs.next()) {
                sb.append("  ").append(catalogRs.getString("TABLE_CAT")).append(nl);
            }
        } catch (SQLException e2) {
            sb.append("  ?? Couldn't get catalogs ??").append(nl);
        } finally {
            try {
                catalogRs.close();
            } catch (Exception ignore) {
            }
        }
        if (e != null) {
            System.out.println(sb.toString());
            e.printStackTrace();
        } else {
            System.out.println(sb.toString());
        }
    }

    /**
     */
    private void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignore) {
        }
    }


}
