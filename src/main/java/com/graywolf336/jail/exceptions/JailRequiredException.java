package com.graywolf336.jail.exceptions;

/**
 * 
 * The exception thrown when a jail is required but wasn't provided.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class JailRequiredException extends Exception {
    private static final long serialVersionUID = 1046287197309037470L;
    
    /**
     * Creation of a new jail is required exception.
     * 
     * @param action where the jail is required.
     */
    public JailRequiredException(String action) {
        super("A jail is required (can not be null) for: " + action);
    }
}
