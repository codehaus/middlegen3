package org.codehaus.rdbms;

import org.codehaus.rdbms.Key;

import java.util.List;

import org.codehaus.rdbms.Key;

/**
 *
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Helles&oslash;y</a>
 * @version $Revision: 1.1 $
 */
public interface PrimaryKey extends Key {
    List getForeignKeys();
}
