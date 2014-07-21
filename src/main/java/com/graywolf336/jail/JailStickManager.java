package com.graywolf336.jail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;

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
	private ArrayList<UUID> stickers;
	private HashMap<Material, Stick> sticks;
	
	public JailStickManager(JailMain plugin) {
		this.stickers = new ArrayList<UUID>();
		this.sticks = new HashMap<Material, Stick>();
		
		this.loadJailSticks(plugin);
	}
	
	private void loadJailSticks(JailMain pl) {
		//item name,time,jail name,reason
		if(pl.getJailManager().getJails().size() == 0) {
			pl.getLogger().warning("Can't have jail sticks without any jails.");
		}else {
			for(String s : pl.getConfig().getStringList(Settings.JAILSTICKSTICKS.getPath())) {
				pl.debug(s);
				String[] a = s.split(",");
				String jail = a[2];
				
				//Check if the jail given, if any, exists
				if(jail.isEmpty()) {
					jail = pl.getJailManager().getJail("").getName();
				}else {
					if(!pl.getJailManager().isValidJail(jail)) {
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
				
				long time = 5;
				try {
					 time = Util.getTime(a[1]);
				} catch (Exception e) {
					pl.getLogger().severe(s);
					pl.getLogger().severe("The time format on the above jail stick configuration is incorrect.");
					continue;
				}
				
				double health = -1;
				if(a.length > 5) health = Double.valueOf(a[4]);
				
				try {
					this.sticks.put(m, new Stick(jail, a[3], time, health));
				}catch (Exception e) {
					e.printStackTrace();
					pl.getLogger().severe(s);
					pl.getLogger().severe("Unable to create a new stick for " + a[0] + ", see the exception above for details.");
					continue;
				}
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
	 * Adds a player to be using a jail stick, with the uuid of the player.
	 * 
	 * @param id of the player to add
	 */
	public void addUsingStick(UUID id) {
		this.stickers.add(id);
	}
	
	/**
	 * Removes a player from using a jail stick, with the uuid of the player.
	 * 
	 * @param id of the player to remove using a jail stick
	 */
	public void removeUsingStick(UUID id) {
		this.stickers.remove(id);
	}
	
	/**
	 * Returns whether or not the player is using a jail stick.
	 * 
	 * @param id of the player to check if using one
	 * @return true if the player is using a jail stick, false if not
	 */
	public boolean isUsingJailStick(UUID id) {
		return this.stickers.contains(id);
	}
	
	/**
	 * Toggles whether the player is using a jail stick, returning the true if enabled false if disabled.
	 * 
	 * @param id of the player to toggle using a stick
	 * @return true if we enabled it, false if we disabled it.
	 */
	public boolean toggleUsingStick(UUID id) {
		if(this.stickers.contains(id)) {
			this.stickers.remove(id);
			return false;
		}else {
			this.stickers.add(id);
			return true;
		}
	}
	
	/**  Removes all the users currently using the sticks. */
	public void removeAllStickUsers() {
		for(UUID id : stickers) {
			this.removeUsingStick(id);
		}
	}
}
