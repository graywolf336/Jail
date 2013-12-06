package com.graywolf336.jail.beans;

/**
 * Represents a Prisoner, player who is jailed, and contains all the information about him/her.
 * 
 * @author graywolf336
 * @since 2.x.x
 * @version 2.0.0
 */
public class Prisoner {
	private String name;
	private boolean muted;
	
	/**
	 * Creates a new prisoner with a name and whether they are muted or not.
	 * 
	 * @param name
	 * @param muted
	 */
	public Prisoner(String name, boolean muted) {
		this.name = name;
		this.muted = muted;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isMuted() {
		return this.muted;
	}
}
