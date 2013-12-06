package com.graywolf336.jail.beans;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

/** Represents a Cell inside of a {@link Jail}.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.1.1
 */
public class Cell {
	private String name;
	private Prisoner p;
	private HashSet<SimpleLocation> signs;
	private SimpleLocation teleport, chest;
	
	public Cell(String name) {
		this.name = name;
		this.signs = new HashSet<SimpleLocation>();
	}
	
	/** Gets the name of the cell. */
	public String getName() {
		return this.name;
	}
	
	/** Sets the prisoner in this cell. */
	public void setPrisoner(Prisoner prisoner) {
		this.p = prisoner;
	}
	
	/** Gets the prisoner being held in this cell. */
	public Prisoner getPrisoner() {
		return this.p;
	}
	
	public void removePrisoner() {
		this.p = null;
	}
	
	/** Returns true if there is currently a prisoner in this cell. */
	public boolean hasPrisoner() {
		return this.p != null; //Return true if prison is not null, as when it isn't null we have a prisoner in this cell
	}
	
	/** Adds all the given signs to the cell. */
	public void addAllSigns(HashSet<SimpleLocation> signs) {
		this.signs.addAll(signs);
	}
	
	/** Adds a sign to the cell. */
	public void addSign(SimpleLocation sign) {
		this.signs.add(sign);
	}
	
	/** Sets the location of where the prisoner will be teleported at when jailed here. */
	public void setTeleport(SimpleLocation location) {
		this.teleport = location;
	}
	
	/** Gets the teleport location where the prisoner will be teleported at when jailed here. */
	public Location getTeleport() {
		return this.teleport.getLocation();
	}
	
	/** Sets the location of the chest. */
	public void setChestLocation(Location location) {
		this.chest = new SimpleLocation(location);
	}
	
	/**
	 * Gets the location of the chest, returns null if no chest is stored at this cell.
	 * 
	 * @return The location of the chest, null if none.
	 */
	public Location getChestLocation() {
		return this.chest.getLocation();
	}
	
	/**
	 * Gets the chest for this cell, returns null if there is no chest or the location we have is not a chest.
	 * 
	 * @return The chest and its state.
	 */
	public Chest getChest() {
		if(this.chest == null) return null;
		if((this.chest.getLocation().getBlock() == null) || (this.chest.getLocation().getBlock().getType() != Material.CHEST)) return null;
		
		return (Chest) this.chest.getLocation().getBlock().getState();
	}
}
