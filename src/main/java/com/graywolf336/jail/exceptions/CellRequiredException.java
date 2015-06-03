package com.graywolf336.jail.exceptions;

/**
 * 
 * The exception thrown when a cell is required but wasn't provided.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class CellRequiredException extends Exception {
    private static final long serialVersionUID = 6496748770371151376L;
    
    /**
     * Creation of a new cell is required exception.
     * 
     * @param action where the jail is required.
     */
    public CellRequiredException(String action) {
        super("A cell is required (can not be null) for: " + action);
    }
}
