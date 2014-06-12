package com.graywolf336.jail.beans;

/**
 * Represents a Jail Stick, contains all the information.
 * 
 * @author graywolf336
 * @version 1.0.1
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
	
	public String getReason() {
		return this.reason;
	}
	
	public long getTime() {
		return this.time;
	}
	
	@Override
	public String toString() {
		return time + "," + jail + "," + reason;
	}
}
