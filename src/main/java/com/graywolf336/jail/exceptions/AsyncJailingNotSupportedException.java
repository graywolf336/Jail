package com.graywolf336.jail.exceptions;

/**
 * 
 * The exception thrown trying to jail via a thread that is NOT the primary thread.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class AsyncJailingNotSupportedException extends Exception {
    private static final long serialVersionUID = 2746426914894618352L;

    /**
     * Creation of an exception from jailing via a thread that is not the primary one.
     * 
     */
    public AsyncJailingNotSupportedException() {
        super("Jailing via a thread that is NOT the primary thread is not supported.");
    }
}
