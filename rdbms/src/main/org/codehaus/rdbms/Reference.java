package org.codehaus.rdbms;

import org.codehaus.rdbms.Column;

/**
 *
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Helles&oslash;y</a>
 * @version $Revision: 1.1 $
 */
public interface Reference {
    public Column getForeignColumn();
    public Column getPrimaryColumn();
}
