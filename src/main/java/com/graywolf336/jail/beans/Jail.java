package com.graywolf336.jail.beans;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.Util;

/** Represents a Jail, contains the prisoners and the cells.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.3
 */
public class Jail {
	private JailMain plugin;
	private HashMap<String, Cell> cells;
	private HashSet<Prisoner> nocellPrisoners;//prisoners who aren't in a cell
	private String name = "", world = "";
	private int minX, minY, minZ, maxX, maxY, maxZ;
	private SimpleLocation in, free;
	
	public Jail(JailMain plugin, String name) {
		this.plugin = plugin;
		this.name = name;
		cells = new HashMap<String, Cell>();
		nocellPrisoners = new HashSet<Prisoner>();
	}
	
	/** Gets the instance of the plugin's main class. */
	public JailMain getPlugin() {
		return this.plugin;
	}
	
	/** Sets the name of the jail. */
	public void setName(String name) {
		this.name = name;
	}
	
	/** Gets the name of the jail. */
	public String getName() {
		return this.name;
	}
	
	/** Sets the location of the <b>minimum</b> point to the given location's coordinates. */
	public void setMinPoint(Location location) {
		if(this.world.isEmpty()) this.world = location.getWorld().getName();
		
		this.minX = location.getBlockX();
		this.minY = location.getBlockY();
		this.minZ = location.getBlockZ();
	}
	
	/** Accepts an array of ints as the coord, where <strong>0 = x</strong>, <strong>1 = y</strong>, <strong>2 = z</strong>. */
	public void setMinPoint(int[] coords) {
		if(coords.length != 3) return;
		
		this.minX = coords[0];
		this.minY = coords[1];
		this.minZ = coords[2];
	}
	
	/** Gets the minimum point as a Bukkit Location class. */
	public Location getMinPoint() {
		return new Location(Bukkit.getServer().getWorld(world), minX, minY, minZ);
	}
	
	/** Sets the location of the <b>maximum</b> point to the given location's coordinates. */
	public void setMaxPoint(Location location) {
		if(this.world.isEmpty()) this.world = location.getWorld().getName();
		
		this.maxX = location.getBlockX();
		this.maxY = location.getBlockY();
		this.maxZ = location.getBlockZ();
	}
	
	/** Gets the minimum point as a Bukkit Location class. */
	public Location getMaxPoint() {
		return new Location(Bukkit.getServer().getWorld(world), maxX, maxY, maxZ);
	}
	
	/** Accepts an array of ints as the coord, where <strong>0 = x</strong>, <strong>1 = y</strong>, <strong>2 = z</strong>. */
	public void setMaxPoint(int[] coords) {
		if(coords.length != 3) return;
		
		this.maxX = coords[0];
		this.maxY = coords[1];
		this.maxZ = coords[2];
	}
	
	/** Sets the name of the world this Jail is in. */
	public void setWorld(String name) {
		this.world = name;
	}
	
	/** Gets the name of the world this Jail is in. */
	public String getWorldName() {
		return this.world;
	}
	
	/** Gets the instance of the {@link World world} this Jail is in. */
	public World getWorld() {
		return plugin.getServer().getWorld(world);
	}
	
	/** Sets the {@link SimpleLocation location} of the teleport <strong>in</strong>. */
	public void setTeleportIn(SimpleLocation location) {
		if(this.world.isEmpty()) this.world = location.getWorldName();
		
		this.in = location;
	}
	
	/** Gets the {@link Location location} of the teleport in. */
	public Location getTeleportIn() {
		return this.in.getLocation();
	}
	
	/** Sets the {@link SimpleLocation location} of the teleport for the <strong>free</strong> spot. */
	public void setTeleportFree(SimpleLocation location) {
		this.free = location;
	}
	
	/** Gets the {@link Location location} of the teleport free spot.*/
	public Location getTeleportFree() {
		return this.free.getLocation();
	}
	
	/** Add a prisoner to this jail. */
	public void addPrisoner(Prisoner p) {
		this.nocellPrisoners.add(p);
	}
	
	/** Removes a prisoner from this jail, doesn't remove it from the cell. */
	public void removePrisoner(Prisoner p) {
		this.nocellPrisoners.remove(p);
	}
	
	/** Adds a cell to the Jail. */
	public void addCell(Cell cell, boolean save) {
		if(save) plugin.getJailIO().saveCell(this, cell);
		this.cells.put(cell.getName(), cell);
	}
	
	/** Gets the cell with the given name. */
	public Cell getCell(String name) {
		return this.cells.get(name);
	}
	
	/** Checks if the given name is a valid cell. */
	public boolean isValidCell(String name) {
		return this.cells.get(name) != null;
	}
	
	/** Removes the cell from the jail. */
	public void removeCell(String name) {
		Cell c = this.cells.get(name);
		//If we have a chest, clear the inventory
		if(c.hasChest()) {
			c.getChest().getInventory().clear();
		}
		
		//For each sign, clear the lines on the sign
		for(SimpleLocation s : c.getSigns()) {
			if(s.getLocation().getBlock() instanceof Sign) {
				Sign sign = (Sign) s.getLocation().getBlock();
				for(int i = 0; i < 4; i++) {
					sign.setLine(i, "");
				}
			}
		}
		
		//remove the information from the storage first as it requires an instance
		plugin.getJailIO().removeCell(this, c);
		//now remove it from the local storage
		this.cells.remove(name);
	}
	
	/** Returns the cell which the given player name is jailed in, null if not. */
	public Cell getCellPrisonerIsIn(UUID uuid) {
		for(Cell c : cells.values())
			if(c.hasPrisoner())
				if(c.getPrisoner().getUUID().equals(uuid))
					return c;
		
		return null;
	}
	
	/** Returns the first empty cell, returns null if there aren't any cells or any free cells. */
	public Cell getFirstEmptyCell() {
		for(Cell c : getCells())
			if(c.hasPrisoner())
				continue;
			else
				return c;
		
		return null;
	}
	
	/** Gets the amount of cells the jail. */
	public int getCellCount() {
		return this.cells.size();
	}
	
	/** Gets all the cells in the jail. */
	public HashSet<Cell> getCells() {
		return new HashSet<Cell>(this.cells.values());
	}
	
	/** Gets the closest cell to the provided location, via the teleport in location of the cells. */
	public Cell getNearestCell(Location loc) {
		Cell cell = null;
		double distance = -1;
		
		for(Cell c : getCells()) {
			//Check if the worlds are the same, if not we can't calculate anything
			if(c.getTeleport().getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())) {
				//They are in the same world
				double dist = c.getTeleport().distance(loc);
				if (dist < distance || distance < 0) {
					cell = c;
					distance = dist;
				}
			}else {
				//If they aren't, return the first cell found.
				return c;
			}
		}
		
		return cell;
	}
	
	/** Clears all the prisoners from this Jail. */
	public void clearPrisoners() {
		//Remove the prisoners from all the cells
		for(Cell c : getCells()) {
			c.removePrisoner();
		}
		
		//Replace all the current no cell prisoners with
		//a new hashset of prisoners.
		this.nocellPrisoners = new HashSet<Prisoner>();
	}
	
	/** Gets a HashSet of <b>all</b> the prisoners, the ones in cells and ones who aren't. */
	public HashSet<Prisoner> getAllPrisoners() {
		HashSet<Prisoner> all = new HashSet<Prisoner>(nocellPrisoners); //initalize the temp one to return with the prisoners not in any cells
		
		for(Cell c : cells.values())
			if(c.hasPrisoner())
				all.add(c.getPrisoner());
		
		return all;
	}
	
	/** Gets a HashSet of the prisoners <b>in cells</b>. */
	public HashSet<Prisoner> getPrisonersInCells() {
		HashSet<Prisoner> all = new HashSet<Prisoner>();
		
		for(Cell c : getCells())
			if(c.hasPrisoner())
				all.add(c.getPrisoner());
		
		return all;
	}
	
	/** Gets a HashSet of the prisoners <b>not</b> in cells.*/
	public HashSet<Prisoner> getPrisonersNotInCells() {
		return this.nocellPrisoners;
	}
	
	/**
	 * Returns whether the player is a prisoner in the system, whether in a cell or no cell.
	 * 
	 * @param player The {@link Player player instance} of the person we're checking.
	 * @return true if is jailed, false if not.
	 */
	public boolean isPlayerJailed(Player player) {
		return this.isPlayerAPrisoner(player.getUniqueId());
	}
	
	/**
	 * Returns whether the uuid of a player is a prisoner in the system, whether in a cell or no cell.
	 * 
	 * @param uuid The uuid of the person we're checking.
	 * @return true if is jailed, false if not.
	 */
	public boolean isPlayerJailed(UUID uuid) {
		return this.isPlayerAPrisoner(uuid);
	}
	
	/**
	 * Returns whether the uuid of a player is a prisoner in this jail, no matter if they're in a cell or not.
	 * 
	 * @param uuid The name of the person we're checking.
	 * @return true if is a prisoner, false if not.
	 */
	private boolean isPlayerAPrisoner(UUID uuid) {
		for(Prisoner p : this.getAllPrisoners())
			if(p.getUUID().equals(uuid))
				return true;
		
		return false;
	}
	
	/**
	 * Checks if the given uuid of a player is a prisoner in a cell.
	 * 
	 * @param uuid of the prisoner to check.
	 * @return true if is jailed in a cell, false if not.
	 */
	public boolean isJailedInACell(UUID uuid) {
		for(Prisoner p : nocellPrisoners)
			if(p.getUUID().equals(uuid))
				return false;
		
		for(Cell c : cells.values())
			if(c.getPrisoner() != null)
				if(c.getPrisoner().getUUID().equals(uuid))
					return true;
		
		return false;
	}
	
	/**
	 * Gets the {@link Prisoner prisoner} instance for the given name.
	 * 
	 * @param name The name of the prisoner to get.
	 * @return the prisoner instance, can be null
	 */
	public Prisoner getPrisonerByLastKnownName(String name) {
		for(Prisoner p : this.getAllPrisoners())
			if(p.getLastKnownName().equalsIgnoreCase(name))
				return p;
		
		return null;
	}
	
	/**
	 * Gets the {@link Prisoner prisoner} instance for the given uuid.
	 * 
	 * @param uuid The uuid of the prisoner to get.
	 * @return the prisoner instance, can be null
	 */
	public Prisoner getPrisoner(UUID uuid) {
		for(Prisoner p : this.getAllPrisoners())
			if(p.getUUID().equals(uuid))
				return p;
			
		return null;
	}
	
	/**
	 * Returns the squared distance between teleport location of this jail
	 * and specified location in blocks. If locations are not in same world,
	 * distance cannot be calculated and it will return Integer.MAX_VALUE.
	 * 
	 * @param loc The location to check
	 * @return Distance between the location provided and the teleport in location.
	 */
	public double getDistance(Location loc) {
		if (loc.getWorld().getName().equalsIgnoreCase(getTeleportIn().getWorld().getName())) return (double) Integer.MAX_VALUE;
		else return loc.distance(getTeleportIn());
	}
	
	/**
	 * Returns whether the given location is inside this Jail.
	 * 
	 * @param loc to check whether is inside this jail
	 * @return True if the location is in the jail, false if it isn't
	 */
	public boolean isInside(Location loc) {
		if(loc.getWorld().getName().equalsIgnoreCase(world)) {
			return Util.isInsideAB(loc.toVector(), new Vector(minX, minY, minZ), new Vector(maxX, maxY, maxZ));
		}else {
			return false;
		}
	}
}
