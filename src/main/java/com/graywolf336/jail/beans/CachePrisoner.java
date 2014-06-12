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
	
	/** Gets the Jail this cache is in. */
	public Jail getJail() {
		return this.jail;
	}
	
	/** Gets the Prisoner in this cache. */
	public Prisoner getPrisoner() {
		return this.p;
	}
}
