package com.graywolf336.jail.exceptions;

/**
 * 
 * The exception thrown when a prisoner is already jailed.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class PrisonerAlreadyJailedException extends Exception {
    private static final long serialVersionUID = -5830449694077279409L;
    
    /**
     * Creation of a prisoner is already jailed exception.
     * 
     */
    public PrisonerAlreadyJailedException() {
        super("A prisoner (no name or uuid provided) is already jailed and can not be jailed twice.");
    }
    
    /**
     * Creation of a prisoner is already jailed exception.
     * 
     * @param prisonerName the prisoner's name
     * @param prisonerUUID the uuid of the prisoner
     */
    public PrisonerAlreadyJailedException(String prisonerName, String prisonerUUID) {
        super("The prisoner " + prisonerName + " (" + prisonerUUID + ") is already jailed and can not be jailed twice.");
    }
}
