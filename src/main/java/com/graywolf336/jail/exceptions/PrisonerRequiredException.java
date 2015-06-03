package com.graywolf336.jail.exceptions;

/**
 * 
 * The exception thrown when a prisoner's data is required but wasn't provided.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class PrisonerRequiredException extends Exception {
    private static final long serialVersionUID = 5289068334047189357L;
    
    /**
     * Creation of a new jail is required exception.
     * 
     * @param action where the jail is required.
     */
    public PrisonerRequiredException(String action) {
        super("Prisoner data is required (can not be null) for: " + action);
    }
}
