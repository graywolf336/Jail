package com.graywolf336.jail;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.graywolf336.jail.beans.Stick;

/**
 * Manages the jail stick users.
 * 
 * @author graywolf336
 * @version 1.0.0
 * @since 3.0.0
 *
 */
public class JailStickManager {
	private ArrayList<String> stickers;
	private HashMap<Material, Stick> sticks;
	
	public JailStickManager(JailMain plugin) {
		this.stickers = new ArrayList<String>();
		this.sticks = new HashMap<Material, Stick>();
		
		this.loadJailSticks(plugin);
	}
	
	private void loadJailSticks(JailMain pl) {
		FileConfiguration config =  pl.getConfig();
		
		//item name,time,jail name,reason
		for(String s : config.getStringList("jailstick")) {
			String[] a = s.split(",");
			
			try {
				this.sticks.put(Material.getMaterial(a[0]), new Stick(a[2], a[1], Long.valueOf(a[3])));
			}catch (Exception e) {
				e.printStackTrace();
				pl.getLogger().info(s);
				pl.getLogger().severe("Unable to create a new stick for " + a[0] + ", see the exception above for details.");
			}
		}
		
		int c = sticks.size();
		pl.getLogger().info("Loaded " + c + " jail stick" + (c == 1 ? "" : "s") + ".");
	}
	
	/**
	 * Gets the {@link Stick jail stick} by the provided {@link Material}, can be null.
	 * 
	 * @param mat of the stick to get
	 * @return The {@link Stick jail stick}
	 */
	public Stick getStick(Material mat) {
		return this.sticks.get(mat);
	}
	
	/** Checks if the provided Material is a valid {@link Stick jail stick}. */
	public boolean isValidStick(Material mat) {
		return this.sticks.containsKey(mat);
	}
	
	/**
	 * Adds a player to be using a jail stick, with the player instance.
	 * 
	 * @param player to add
	 */
	public void addUsingStick(Player player) {
		this.stickers.add(player.getName());
	}
	
	/**
	 * Adds a player to be using a jail stick, with their username.
	 * 
	 * @param name of the player to add
	 */
	public void addUsingStick(String name) {
		this.stickers.add(name);
	}
	
	/**
	 * Removes a player from using a jail stick, with the player instance.
	 * 
	 * @param player to remove using a jail stick
	 */
	public void removeUsingStick(Player player) {
		this.stickers.remove(player.getName());
	}
	
	/**
	 * Removes a player from using a jail stick, with their username.
	 * 
	 * @param name of the player to remove using a jail stick
	 */
	public void removeUsingStick(String name) {
		this.stickers.remove(name);
	}
	
	/**
	 * Returns whether or not the player is using a jail stick.
	 * 
	 * @param player to check if using one
	 * @return true if the player is using a jail stick, false if not
	 */
	public boolean usingJailStick(Player player) {
		return this.stickers.contains(player.getName());
	}
	
	/**
	 * Returns whether or not the player is using a jail stick.
	 * 
	 * @param name of the player to check if using one
	 * @return true if the player is using a jail stick, false if not
	 */
	public boolean usingJailStick(String name) {
		return this.stickers.contains(name);
	}
}
