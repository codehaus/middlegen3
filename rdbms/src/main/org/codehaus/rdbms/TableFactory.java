package org.codehaus.rdbms;

import java.util.List;

/**
 * Factory for creation of {@link org.codehaus.rdbms.Table}s.
 *
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.1 $
 */
public interface TableFactory {
    List getTables();

    MutableTable createTable(String sqlName);
    MutableTable getTable(String sqlName);

}
