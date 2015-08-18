package com.graywolf336.jail.beans;

/**
 * An object for storing online cached prisoners.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class CachePrisoner {
    private Jail jail;
    private Prisoner p;

    public CachePrisoner(Jail jail, Prisoner prisoner) {
        this.jail = jail;
        this.p = prisoner;
    }

    /**
     * Gets the {@link Jail} this cache is in.
     * 
     * @return the {@link Jail} instance
     */
    public Jail getJail() {
        return this.jail;
    }

    /**
     * Gets the {@link Prisoner} in this cache.
     * 
     * @return the {@link Prisoner} instance
     */
    public Prisoner getPrisoner() {
        return this.p;
    }
}
