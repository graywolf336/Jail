package com.graywolf336.jail;

import java.util.HashMap;

import org.bukkit.Location;

/**
 * 
 * @author graywolf336
 * @since 2.6.3
 * @version 1.0.1
 */
public class HandCuffManager {
	private HashMap<String, Long> handcuffed;
	private HashMap<String, Location> locs;
	
	/** Constructs a new HandCuff Manager, for handling all the handcuffing. */
	public HandCuffManager() {
		this.handcuffed = new HashMap<String, Long>();
		this.locs = new HashMap<String, Location>();
	}
	
	/**
	 * Adds handcuffs to a player.
	 * 
	 * @param name of the player
	 * @param location where the player was handcuffed, so they can't move
	 */
	public void addHandCuffs(String name, Location location) {
		this.handcuffed.put(name.toLowerCase(), System.currentTimeMillis());
		this.locs.put(name.toLowerCase(), location);
	}
	
	/**
	 * Removes the handcuffs from the given player.
	 * 
	 * @param name of the person to remove the handcuffs from
	 */
	public void removeHandCuffs(String name) {
		this.handcuffed.remove(name.toLowerCase());
		this.locs.remove(name.toLowerCase());
	}
	
	/**
	 * Gets if the player is handcuffed or not.
	 * 
	 * @param name of the player to check
	 * @return true if they are handcuffed, false if not
	 */
	public boolean isHandCuffed(String name) {
		return this.handcuffed.containsKey(name.toLowerCase());
	}
	
	/**
	 * Gets the next Long time we should send a message to the player.
	 * 
	 * @param name of the player to get the name we're supposed to message them next
	 * @return long value of the system time in milliseconds
	 */
	public Long getNextMessageTime(String name) {
		return this.handcuffed.get(name.toLowerCase());
	}
	
	/**
	 * Updates the time to the next 10 seconds from now to when we should send them a message.
	 * 
	 * @param name of the player we're setting the message time to
	 */
	public void updateNextTime(String name) {
		this.handcuffed.put(name.toLowerCase(), System.currentTimeMillis() + 10000);
	}
	
	/**
	 * Gets the location where the given player was handcuffed at.
	 * 
	 * @param name of the player get the location for
	 * @return the location where the player was handcuffed at
	 */
	public Location getLocation(String name) {
		return this.locs.get(name.toLowerCase());
	}
}
