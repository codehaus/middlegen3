package org.codehaus.middlegen;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision: 1.2 $
 */
public class MiddlegenException extends RuntimeException {

    private Throwable cause;

    public MiddlegenException(String msg) {
        super(msg);
    }

    public MiddlegenException(Exception cause) {
        super(cause.getMessage());
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }
}

