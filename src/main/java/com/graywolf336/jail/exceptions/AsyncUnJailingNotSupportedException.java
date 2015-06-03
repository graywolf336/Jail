package com.graywolf336.jail.exceptions;

/**
 * 
 * The exception thrown trying to unjail via a thread that is NOT the primary thread.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class AsyncUnJailingNotSupportedException extends Exception {
    private static final long serialVersionUID = -1540695375715404835L;

    /**
     * Creation of an exception from unjailing via a thread that is not the primary one.
     * 
     */
    public AsyncUnJailingNotSupportedException() {
        super("Unjailing via a thread that is NOT the primary thread is not supported.");
    }
}
