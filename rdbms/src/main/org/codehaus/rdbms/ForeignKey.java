package org.codehaus.rdbms;

import java.util.Collection;

/**
 * A foreign key, and thereby also a {@link Relationship}.
 *
 * The foreign key belongs to 'a' table (the 'many' side). The 'b' table is
 * always on the 'one' side.
 *
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Helles&oslash;y</a>
 * @version $Revision: 1.1 $
 */
public interface ForeignKey extends Key, Relationship {
    /**
     * @return a Collection of {@link org.codehaus.rdbms.Reference}.
     */
    Collection getReferences();

    /**
     * Gets the referenced PrimaryKey
     *
     * @return The PrimaryKey we're referencing
     */
    PrimaryKey getPrimaryKey();

    boolean isUnique();

    /**
     * The logical name of the foreign key. This is <em>not</em> the name of the column
     * (because a foreign key consists of one or more columns), but a logical name attributed
     * by the database/driver.
     * @return
     */
    String getName();

    /**
     * Sets the multiplicity of the target side.
     * TODO do we really need this? RdbmsTableFromJdbcMetadataProvider figures it out!
     * @param originMany
     * @throws org.codehaus.rdbms.jdbc.DatabaseException if the multiplicity is illegal.
     */
//    void setOriginMany(boolean originMany) throws DatabaseException;
}
