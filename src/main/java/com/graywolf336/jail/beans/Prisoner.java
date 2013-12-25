package com.graywolf336.jail.beans;

import java.util.concurrent.TimeUnit;

/**
 * Represents a Prisoner, player who is jailed, and contains all the information about him/her.
 * 
 * @author graywolf336
 * @since 2.x.x
 * @version 2.0.1
 */
public class Prisoner {
	private String name;
	private boolean muted, offlinePending;
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
	
	/** Gets the remaining time the prisoner has. */
	public long getRemainingTime() {
		return this.time;
	}
	
	/** Gets the remaining time the prisoner has in minutes. */
	public long getRemainingTimeInMinutes() {
		return TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Sets the remaining time the prisoner has left.
	 * 
	 * @param time The amount of time left, in milliseconds.
	 */
	public void setRemainingTime(long time) {
		this.time = time;
	}
	
	/** Gets whether the player is offline or not. */
	public boolean isOfflinePending() {
		return this.offlinePending;
	}
	
	/** Sets whether the player is offline or not. */
	public void setOfflinePending(boolean offline) {
		this.offlinePending = offline;
	}
}
