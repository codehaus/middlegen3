package org.codehaus.rdbms.jdbc;

/**
 * @author <a href="mailto:aslak.hellesoy at netcom.no">Aslak Hellesøy</a>
 * @version $Revision: 1.1 $
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String msg) {
        super(msg);
    }

    public DatabaseException(String msg, Exception e) {
        super(msg, e);
    }
}

