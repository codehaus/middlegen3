package org.codehaus.rdbms;

import org.codehaus.rdbms.jdbc.DatabaseException;

import java.util.Collection;
import java.util.List;

/**
 * Factory for an entire schema.
 *
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public interface SchemaFactory {
    /**
     * Makes a new Collection of {@link org.codehaus.rdbms.Table}. This method
     * is not named createTables() on purpose to avoid this
     * method being accidentally called by Ant if someone uses a &lt;tables&gt;
     * element.
     *
     * @param tableFactory the factory to use for creation of individual tables.
     * @param columnFactory the factory to use for creation of individual columns.
     * @return a Collection of {@link org.codehaus.rdbms.Table}.
     * @throws org.codehaus.rdbms.jdbc.DatabaseException if creation fails.
     */
    List loadTables( TableFactory tableFactory, ColumnFactory columnFactory ) throws DatabaseException;
}
