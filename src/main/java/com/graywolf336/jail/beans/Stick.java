package com.graywolf336.jail.beans;

/**
 * Represents a Jail Stick, contains all the information.
 * 
 * @author graywolf336
 * @version 1.0.0
 * @since 3.0.0
 *
 */
public class Stick {
	private String jail, reason;
	private long time;
	
	public Stick(String jail, String reason, long time) {
		this.jail = jail;
		this.reason = reason;
		this.time = time;
	}
	
	public String getJail() {
		return this.jail;
	}
	
	public String getJailReason() {
		return this.reason;
	}
	
	public long getJailTime() {
		return this.time;
	}
}
