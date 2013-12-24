package com.graywolf336.jail;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.beans.CreationPlayer;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.steps.CellCreationSteps;
import com.graywolf336.jail.steps.JailCreationSteps;

/**
 * Handles all things related to jails.
 * 
 * <p>
 * 
 * Stores the following:
 * <ul>
 * 	<li>The {@link Jail jails}, which contains the prisoners and cells.</li>
 * 	<li>Players creating jails, see {@link CreationPlayer}.</li>
 * 	<li>Players creating jail cells, see {@link CreationPlayer}.</li>
 * 	<li>An instance of {@link JailCreationSteps} for stepping players through the Jail creation process.</li>
 * </ul>
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.1.0
 */
public class JailManager {
	private JailMain plugin;
	private HashMap<String, Jail> jails;
	private HashMap<String, CreationPlayer> jailCreators;
	private HashMap<String, CreationPlayer> cellCreators;
	private JailCreationSteps jcs;
	private CellCreationSteps ccs;
	
	public JailManager(JailMain plugin) {
		this.plugin = plugin;
		this.jails = new HashMap<String, Jail>();
		this.jailCreators = new HashMap<String, CreationPlayer>();
		this.cellCreators = new HashMap<String, CreationPlayer>();
		this.jcs = new JailCreationSteps();
		this.ccs = new CellCreationSteps();
	}
	
	/** Returns the instance of the plugin main class. */
	public JailMain getPlugin() {
		return this.plugin;
	}
	
	/** Returns a HashSet of all the jails. */
	public HashSet<Jail> getJails() {
		return new HashSet<Jail>(jails.values());
	}
	
	/** Returns an array of all the names of the jails. */
	public String[] getJailNames() {
		return this.jails.keySet().toArray(new String[jails.size()]);
	}
	
	/**
	 * Adds a jail to the collection of them.
	 * 
	 * @param jail The jail to add
	 * @param n True if this is a new jail, false if it isn't.
	 */
	public void addJail(Jail jail, boolean n) {
		this.jails.put(jail.getName(), jail);
		if(n) plugin.getJailIO().saveJail(jail);
	}
	
	/**
	 * Gets a jail by the given name.
	 * 
	 * @param name The name of the jail to get.
	 * @return The {@link Jail} with the given name, if no jail found this <strong>will</strong> return null.
	 */
	public Jail getJail(String name) {
		return this.jails.get(name);
	}
	
	/**
	 * Gets the nearest {@link Jail} to the player, if the sender is a player or else it will get the first jail defined.
	 * 
	 * @param sender The sender who we are looking around.
	 * @return The nearest {@link Jail} to the sender if it is a player or else the first jail defined.
	 */
	public Jail getNearestJail(CommandSender sender) {
		if(sender instanceof Player) {
			Location loc = ((Player) sender).getLocation();
			
			Jail j = null;
			double len = -1;
			
			for(Jail jail : jails.values()) {
				double clen = jail.getDistance(loc);
				
				if (clen < len || len == -1) {
					len = clen;
					j = jail;
				}
			}
			
			return (j == null ? jails.values().iterator().next() : j);
		}else {
			return jails.values().iterator().next();
		}
	}
	
	/**
	 * Checks to see if the given name for a {@link Jail} is valid, returns true if it is a valid jail.
	 * 
	 * @param name The name of the jail to check.
	 * @return True if a valid jail was found, false if no jail was found.
	 */
	public boolean isValidJail(String name) {
		return this.jails.get(name) != null;
	}
	
	/**
	 * Gets the {@link Jail jail} the given player is in.
	 * 
	 * @param name The name of the player whos jail we are getting.
	 * @return The jail the player is in, <strong>CAN BE NULL</strong>.
	 */
	public Jail getJailPlayerIsIn(String name) {
		Jail re = null;
		
		for(Jail j : jails.values()) {
			if(j.isPlayerAPrisoner(name)) {
				re = j;
				break;
			}
		}
		
		return re;
	}
	
	/**
	 * Gets if the given player is jailed or not, in all the jails and cells.
	 * 
	 * @param name The name of the player to check.
	 * @return true if they are jailed, false if not.
	 */
	public boolean isPlayerJailed(String name) {
		boolean r = false;
		
		for(Jail j : jails.values()) {
			if(j.isPlayerAPrisoner(name)) {
				r = true;
				break;
			}
		}
		
		return r;
	}
	
	/** 
	 * Gets the {@link Prisoner} data from for this user, if they are jailed.
	 * 
	 * @param name The name of prisoner who's data to get
	 * @return {@link Prisoner prisoner} data.
	 */
	public Prisoner getPrisoner(String name) {
		Jail j = getJailPlayerIsIn(name);
		
		if(j != null) {
			return j.getPrisoner(name);
		}else {
			return null;
		}
	}
	
	/**
	 * Returns whether or not the player is creating a jail or a cell.
	 * 
	 * <p>
	 * 
	 * If you want to check to see if they're just creating a jail then use {@link #isCreatingAJail(String) isCreatingAJail} or if you want to see if they're creating a cell then use {@link #isCreatingACell(String) isCreatingACell}. 
	 * 
	 * @param name The name of the player, in any case as we convert it to lowercase.
	 * @return True if the player is creating a jail or cell, false if they're not creating anything.
	 */
	public boolean isCreatingSomething(String name) {
		return this.jailCreators.containsKey(name.toLowerCase()) || this.cellCreators.containsKey(name.toLowerCase());
	}
	
	/** Returns a message used for telling them what they're creating and what step they're on. */
	public String getStepMessage(String player) {
		String message = "";
		
		if(isCreatingACell(player)) {//Check whether it is a jail cell
			CreationPlayer cp = this.getCellCreationPlayer(player);
			message = "You're already creating a Cell with the name '" + cp.getCellName() + "' and you still need to ";
			
			switch(cp.getStep()) {
				case 1:
					message += "set the teleport in location.";
					break;
				case 2:
					message += "select all the signs.";
					break;
				case 3:
					message += "set the double chest location.";
					break;
			}
			
		}else if(isCreatingAJail(player)) {//If not a cell, then check if a jail.
			CreationPlayer cp = this.getJailCreationPlayer(player);
			message = "You're already creating a Jail with the name '" + cp.getJailName() + "' and you still need to ";
			
			switch(cp.getStep()) {
				case 1:
					message += "select the first point.";
					break;
				case 2:
					message += "select the second point.";
					break;
				case 3:
					message += "set the teleport in location.";
					break;
				case 4:
					message += "set the release location.";
					break;
			}
		}
		
		return message;
	}
	
	/** Returns whether or not someone is creating a <strong>Jail</strong>. */
	public boolean isCreatingAJail(String name) {
		return this.jailCreators.containsKey(name.toLowerCase());
	}
	
	/**
	 * Method for setting a player to be creating a Jail, returns whether or not they were added successfully.
	 * 
	 * @param player The player who is creating a jail.
	 * @param jailName The name of the jail we are creating.
	 * @return True if they were added successfully, false if they are already creating a Jail.
	 */
	public boolean addCreatingJail(String player, String jailName) {
		if(isCreatingAJail(player)) {
			return false;
		}else {
			this.jailCreators.put(player.toLowerCase(), new CreationPlayer(jailName));
			return true;
		}
	}
	
	/** Returns the instance of the CreationPlayer for this player, null if there was none found. */
	public CreationPlayer getJailCreationPlayer(String name) {
		return this.jailCreators.get(name.toLowerCase());
	}
	
	/** Removes a CreationPlayer with the given name from the jail creators. */
	public void removeJailCreationPlayer(String name) {
		this.jailCreators.remove(name.toLowerCase());
	}
	
	/** Returns whether or not someone is creating a <strong>Cell</strong>. */
	public boolean isCreatingACell(String name) {
		return this.cellCreators.containsKey(name.toLowerCase());
	}
	
	/**
	 * Method for setting a player to be creating a Cell, returns whether or not they were added successfully.
	 * 
	 * @param player The player who is creating a jail.
	 * @param jailName The name of the jail this cell is going.
	 * @param cellName The name of the cell we are creating.
	 * @return True if they were added successfully, false if they are already creating a Jail.
	 */
	public boolean addCreatingCell(String player, String jailName, String cellName) {
		if(isCreatingACell(player)) {
			return false;
		}else {
			this.cellCreators.put(player.toLowerCase(), new CreationPlayer(jailName, cellName));
			return true;
		}
	}
	
	/** Returns the instance of the CreationPlayer for this player, null if there was none found. */
	public CreationPlayer getCellCreationPlayer(String name) {
		return this.cellCreators.get(name.toLowerCase());
	}
	
	/** Removes a CreationPlayer with the given name from the cell creators. */
	public void removeCellCreationPlayer(String name) {
		this.cellCreators.remove(name.toLowerCase());
	}
	
	/** Gets the instance of the {@link JailCreationSteps}. */
	public JailCreationSteps getJailCreationSteps() {
		return this.jcs;
	}
	
	/** Gets the instance of the {@link CellCreationSteps}. */
	public CellCreationSteps getCellCreationSteps() {
		return this.ccs;
	}
}
