package org.codehaus.rdbms.impl;

import org.codehaus.rdbms.Column;
import org.codehaus.rdbms.Reference;

/**
 *
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Helles&oslash;y</a>
 * @version $Revision: 1.1 $
 */
public class ReferenceImpl implements Reference {
    private final Column _fkColumn;
    private final Column _pkColumn;

    public ReferenceImpl(Column fkColumn, Column pkColumn) {
        _fkColumn = fkColumn;
        _pkColumn = pkColumn;
    }

    public Column getForeignColumn() {
        return _fkColumn;
    }

    public Column getPrimaryColumn() {
        return _pkColumn;
    }
}
