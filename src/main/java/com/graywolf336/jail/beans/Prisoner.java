package com.graywolf336.jail.beans;

/**
 * Represents a Prisoner, player who is jailed, and contains all the information about him/her.
 * 
 * @author graywolf336
 * @since 2.x.x
 * @version 2.0.1
 */
public class Prisoner {
	private String name;
	private boolean muted;
	private long time;
	
	/**
	 * Creates a new prisoner with a name and whether they are muted or not.
	 * 
	 * @param name The name of the prisoner
	 * @param muted Whether the prisoner is muted or not
	 * @param time The amount of remaining time the prisoner has
	 */
	public Prisoner(String name, boolean muted, long time) {
		this.name = name;
		this.muted = muted;
		this.time = time;
	}
	
	/** Gets the name of this player. */
	public String getName() {
		return this.name;
	}
	
	/** Gets whether the prisoner is muted or not. */
	public boolean isMuted() {
		return this.muted;
	}
	
	/** Sets whether the prisoner is muted or not. */
	public void setMuted(boolean muted) {
		this.muted = muted;
	}
	
	/** Gets the remaining time the prisoner has.
	 * 
	 * <p />
	 * 
	 * <strong>WARNING:</strong> The time system hasn't been implemented so this is likely to change.
	 */
	public long getRemainingTime() {
		return this.time;
	}
	
	/**
	 * Sets the remaining time the prisoner has left.
	 * 
	 * <p />
	 * 
	 * <strong>WARNING:</strong> The time system hasn't been implemented so this is likely to change.
	 * 
	 * @param time The amount of time left, in milliseconds.
	 */
	public void setRemainingTime(long time) {
		this.time = time;
	}
}
