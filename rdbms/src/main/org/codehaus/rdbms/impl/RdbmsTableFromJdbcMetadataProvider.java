package org.codehaus.rdbms.impl;

import org.codehaus.rdbms.SchemaFactory;
import org.codehaus.rdbms.Table;
import org.generama.MetadataProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class is a MetaDataProvider for the Generama framework that
 * generates {@link org.codehaus.rdbms.Table} objects from database metadata via JDBC.
 *
 * It can be subclassed (or rather, passed a mixinadder in the ctor!!!!!!!)
 *
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public class RdbmsTableFromJdbcMetadataProvider implements MetadataProvider {

    private List cachedMetadata;
    private PicoTableFactory tableFactory;
    private final SchemaFactory databaseFactory;

    public RdbmsTableFromJdbcMetadataProvider(PicoTableFactory tableFactory, SchemaFactory databaseFactory) {
        this.tableFactory = tableFactory;
        this.databaseFactory = databaseFactory;
    }

    ///////// MetadataProvider interface ///////////

    /**
     * Creates a collection of Metadata for the XDoclet2 framework. The objects in
     * the collection are picovontainer-enhanced instances of {@link org.codehaus.rdbms.Table}
     */
    public Collection getMetadata() {
        if (cachedMetadata == null) {
            addTableMixins();
            PicoColumnFactory columnFactory = new PicoColumnFactory();
            addColumnMixins(columnFactory);
            cachedMetadata = new ArrayList(databaseFactory.loadTables(tableFactory, columnFactory));
            Collections.sort(cachedMetadata);
        }
        return Collections.unmodifiableCollection(cachedMetadata);
    }

    protected void addColumnMixins(PicoColumnFactory columnFactory) {
//        columnFactory.addColumnMixin(GenericFieldImpl.class);
//        columnFactory.addColumnMixin(JavaFieldImpl.class);
    }

    protected void addTableMixins() {
//        tableFactory.addTableMixin(GenericClassImpl.class);
//        tableFactory.addTableMixin(JavaClassImpl.class);
    }

    public String getFilenameSubstitutionValue(Object o) {
        return ((Table) o).getSqlName();
    }

    public String getPackageName(Object o) {
        return null;
    }

    public String getOriginalFileName(Object metadata) {
        return null;
    }

    public String getOriginalPackageName(Object metadata) {
        return null;
    }
}

