package com.graywolf336.jail.interfaces;

import java.util.UUID;

import org.bukkit.Material;

import com.graywolf336.jail.beans.Stick;

/**
 * Interface for the jail stick manager.
 * 
 * @author graywolf336
 * @version 1.0.0
 * @since 3.0.0
 *
 */
public interface IJailStickManager {
	
	/**
     * Gets the {@link Stick jail stick} by the provided {@link Material}, can be null.
     * 
     * @param mat of the stick to get
     * @return The {@link Stick jail stick}
     */
    public Stick getStick(Material mat);
    
    /**
     * Checks if the provided Material is a valid {@link Stick jail stick}
     * 
     * @param mat the {@link Material} to check
     * @return whether the provided material is a valid stick
     */
    public boolean isValidStick(Material mat);
    
    /**
     * Adds a player to be using a jail stick, with the uuid of the player.
     * 
     * @param id of the player to add
     */
    public void addUsingStick(UUID id);
    
    /**
     * Removes a player from using a jail stick, with the uuid of the player.
     * 
     * @param id of the player to remove using a jail stick
     */
    public void removeUsingStick(UUID id);
    
    /**
     * Returns whether or not the player is using a jail stick.
     * 
     * @param id of the player to check if using one
     * @return true if the player is using a jail stick, false if not
     */
    public boolean isUsingJailStick(UUID id);
    
    /**
     * Toggles whether the player is using a jail stick, returning the true if enabled false if disabled.
     * 
     * @param id of the player to toggle using a stick
     * @return true if we enabled it, false if we disabled it.
     */
    public boolean toggleUsingStick(UUID id);
    
    /**  Removes all the users currently using the sticks. */
    public void removeAllStickUsers();
}
