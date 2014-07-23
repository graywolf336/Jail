package com.graywolf336.jail;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;

/**
 * Manages the handcuffing of players.
 * 
 * <p />
 * 
 * Provides easy to use methods for adding,
 * removing, and checking if someone has
 * handcuffs.
 * 
 * <ul>
 * 	<li>{@link #addHandCuffs(UUID, Location) addHandCuffs}</li>
 * 	<li>{@link #removeHandCuffs(UUID) removeHandCuffs}</li>
 * 	<li>{@link #isHandCuffed(UUID) isHandCuffed}</li>
 * 	<li>{@link #getLocation(UUID) getLocation}</li>
 * </ul>
 * 
 * @author graywolf336
 * @since 2.6.3
 * @version 1.0.2
 */
public class HandCuffManager {
	private HashMap<UUID, Long> handcuffed;
	private HashMap<UUID, Location> locs;
	
	/** Constructs a new HandCuff Manager, for handling all the handcuffing. */
	public HandCuffManager() {
		this.handcuffed = new HashMap<UUID, Long>();
		this.locs = new HashMap<UUID, Location>();
	}
	
	/**
	 * Adds handcuffs to a player.
	 * 
	 * @param uuid of the player
	 * @param location where the player was handcuffed, so they can't move
	 */
	public void addHandCuffs(UUID uuid, Location location) {
		this.handcuffed.put(uuid, System.currentTimeMillis());
		this.locs.put(uuid, location);
	}
	
	/**
	 * Removes the handcuffs from the given player.
	 * 
	 * @param uuid of the person to remove the handcuffs from
	 */
	public void removeHandCuffs(UUID uuid) {
		this.handcuffed.remove(uuid);
		this.locs.remove(uuid);
	}
	
	/**
	 * Gets if the player is handcuffed or not.
	 * 
	 * @param uuid of the player to check
	 * @return true if they are handcuffed, false if not
	 */
	public boolean isHandCuffed(UUID uuid) {
		return this.handcuffed.containsKey(uuid);
	}
	
	/**
	 * Gets the next Long time we should send a message to the player.
	 * 
	 * @param uuid of the player to get the name we're supposed to message them next
	 * @return long value of the system time in milliseconds
	 */
	public Long getNextMessageTime(UUID uuid) {
		return this.handcuffed.get(uuid);
	}
	
	/**
	 * Updates the time to the next 10 seconds from now to when we should send them a message.
	 * 
	 * @param uuid of the player we're setting the message time to
	 */
	public void updateNextTime(UUID uuid) {
		this.handcuffed.put(uuid, System.currentTimeMillis() + 10000);
	}
	
	/**
	 * Gets the location where the given player was handcuffed at.
	 * 
	 * @param uuid of the player get the location for
	 * @return the location where the player was handcuffed at
	 */
	public Location getLocation(UUID uuid) {
		return this.locs.get(uuid);
	}
}
