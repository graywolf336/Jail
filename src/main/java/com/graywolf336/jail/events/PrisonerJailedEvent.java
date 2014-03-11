package com.graywolf336.jail.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;

/**
 * Event thrown after a prisoner is put into jail.
 * 
 * <p />
 * 
 * This event is called after everything for the jailing takes place.
 * This event is called for informative purposes, see {@link PrePrisonerJailedEvent}
 * for the event called before they teleported in and all that fancy stuff.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class PrisonerJailedEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Jail jail;
	private Cell cell;
	private Prisoner prisoner;
	private Player player;
	
	/**
	 * Creates a new {@link PrisonerJailedEvent prisoner jailed event} for the given player.
	 * 
	 * @param jail The jail the prisoner will be jailed at.
	 * @param cell The cell we're going to be sending the prisoner to, can be null.
	 * @param prisoner The prisoner data.
	 * @param player The player being jailed.
	 */
	public PrisonerJailedEvent(Jail jail, Cell cell, Prisoner prisoner, Player player) {
		this.jail = jail;
		this.cell = cell;
		this.prisoner = prisoner;
		this.player = player;
	}
	
	/** Gets the {@link Jail} this prisoner is being sent to. */
	public Jail getJail() {
		return this.jail;
	}
	
	/** Gets the cell we're going to be sending the prisoner to, can be null. */
	public Cell getCell() {
		return this.cell;
	}
	
	/** Gets the {@link Prisoner} data for this prisoner. */
	public Prisoner getPrisoner() {
		return this.prisoner;
	}
	
	/** Gets the instance of the player being jailed. */
	public Player getPlayer() {
		return this.player;
	}
	
	/** Gets the jailer who jailed this prisoner. */
	public String getJailer() {
		return this.prisoner.getJailer();
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	public HandlerList getHandlers() {
		return handlers;
	}
}
