package org.codehaus.rdbms;

import java.util.Collection;

/**
 * Factory for creation of {@link org.codehaus.rdbms.Column}s.
 *
 * @author <a href="mailto:aslak.hellesoy at bekk.no">Aslak Helles&oslash;y</a>
 * @version $Revision: 1.1 $
 */
public interface ColumnFactory {
    /**
     * Creates a new Column object.
     *
     * @return a newly created Column.
     */
    MutableColumn createColumn(MutableTable table, String sqlName);
}
