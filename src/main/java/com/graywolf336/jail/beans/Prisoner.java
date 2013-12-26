package com.graywolf336.jail.beans;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Represents a Prisoner, player who is jailed, and contains all the information about him/her.
 * 
 * @author graywolf336
 * @since 2.x.x
 * @version 2.0.1
 */
public class Prisoner {
	private String name, reason;
	private boolean muted, offlinePending, teleporting;
	private long time;
	private Location previousPosition;
	
	/**
	 * Creates a new prisoner with a name and whether they are muted or not.
	 * 
	 * @param name The name of the prisoner
	 * @param muted Whether the prisoner is muted or not
	 * @param time The amount of remaining time the prisoner has
	 */
	public Prisoner(String name, boolean muted, long time, String reason) {
		this.name = name;
		this.muted = muted;
		this.time = time;
		this.reason = reason;
		this.offlinePending = false;
		this.teleporting = false;
		this.previousPosition = null;
	}
	
	/** Gets the name of this player. */
	public String getName() {
		return this.name;
	}
	
	/** Gets the reason this player was jailed for. */
	public String getReason() {
		return this.reason;
	}
	
	/** Sets the reason this player was jailed for. */
	public void setReason(String reason) {
		this.reason = reason;
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
	
	/** Gets whether the player is being teleported or not. */
	public boolean isTeleporting() {
		return this.teleporting;
	}
	
	/** Sets whether the player is being teleported or not. */
	public void setTeleporting(boolean teleport) {
		this.teleporting = teleport;
	}
	
	/** Gets the previous location of this player, can be null. */
	public Location getPreviousLocation() {
		return this.previousPosition;
	}
	
	/** Gets the previous location of this player, separated by a comma. */
	public String getPreviousLocationString() {
		if(previousPosition == null) return "";
		else return previousPosition.getWorld().getName() + "," + 
				previousPosition.getX() + "," + 
				previousPosition.getY() + "," +
				previousPosition.getZ() + "," +
				previousPosition.getYaw() + "," +
				previousPosition.getPitch();
	}
	
	/** Sets the previous location of this player. */
	public void setPreviousPosition(Location location) {
		this.previousPosition = location;
	}
	
	/** Sets the previous location of this player from a comma separated string. */
	public void setPreviousPosition(String location) {
		if(location == null) return;
		if(location.isEmpty()) return;
		
		String[] s = location.split(",");
		this.previousPosition = new Location(Bukkit.getWorld(s[0]),
				Double.valueOf(s[1]),
				Double.valueOf(s[2]),
				Double.valueOf(s[3]),
				Float.valueOf(s[4]),
				Float.valueOf(s[5]));
	}
}
