package com.graywolf336.jail;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.graywolf336.jail.beans.Stick;
import com.graywolf336.jail.enums.Settings;

/**
 * Manages the jail stick users.
 * 
 * @author graywolf336
 * @version 1.0.2
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
		for(String s : config.getStringList(Settings.JAILSTICKSTICKS.getPath())) {
			pl.debug(s);
			String[] a = s.split(",");
			
			//Check if the jail given, if any, exists
			if(!a[2].isEmpty()) {
				if(!pl.getJailManager().isValidJail(a[2])) {
					pl.getLogger().severe(s);
					pl.getLogger().severe("The above jail stick configuration is invalid and references a jail that doesn't exist.");
					continue;
				}
			}
			
			Material m = Material.getMaterial(a[0].toUpperCase());
			if(this.sticks.containsKey(m)) {
				pl.getLogger().severe(s);
				pl.getLogger().severe("You can not use the same item for two different Jail Sticks. This already exists as a Jail Stick: " + a[0]);
				continue;
			}
			
			try {
				this.sticks.put(m, new Stick(a[2], a[3], Long.valueOf(a[1]), Double.valueOf(a[4])));
			}catch (Exception e) {
				e.printStackTrace();
				pl.getLogger().severe(s);
				pl.getLogger().severe("Unable to create a new stick for " + a[0] + ", see the exception above for details.");
				continue;
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
	public boolean isUsingJailStick(Player player) {
		return this.stickers.contains(player.getName());
	}
	
	/**
	 * Returns whether or not the player is using a jail stick.
	 * 
	 * @param name of the player to check if using one
	 * @return true if the player is using a jail stick, false if not
	 */
	public boolean isUsingJailStick(String name) {
		return this.stickers.contains(name);
	}
	
	/**
	 * Toggles whether the player is using a jail stick, returning the true if enabled false if disabled.
	 * 
	 * @param player to toggle using a stick
	 * @return true if we enabled it, false if we disabled it.
	 */
	public boolean toggleUsingStick(Player player) {
		return this.toggleUsingStick(player.getName());
	}
	
	/**
	 * Toggles whether the player is using a jail stick, returning the true if enabled false if disabled.
	 * 
	 * @param name of the person to toggle
	 * @return true if we enabled it, false if we disabled it.
	 */
	public boolean toggleUsingStick(String name) {
		if(this.stickers.contains(name)) {
			this.stickers.remove(name);
			return false;
		}else {
			this.stickers.add(name);
			return true;
		}
	}
	
	/**  Removes all the users currently using the sticks. */
	public void removeAllStickUsers() {
		for(String s: stickers) {
			this.removeUsingStick(s);
		}
	}
}
