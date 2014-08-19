package com.graywolf336.jail.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;

/**
 * Event thrown when a prisoner dies.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class PrisonerDeathEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private PlayerDeathEvent event;
    private Jail jail;
    private Cell cell;
    private Prisoner prisoner;
    private Player player;

    /**
     * Creates a new {@link PrisonerDeathEvent prisoner death event} for the given player.
     * 
     * @param event The {@link PlayerDeathEvent} which triggered this event.
     * @param jail The jail the prisoner is in.
     * @param cell The cell the prisoner is in, can be null.
     * @param prisoner The prisoner's data.
     * @param player The player being jailed.
     */
    public PrisonerDeathEvent(PlayerDeathEvent event, Jail jail, Cell cell, Prisoner prisoner, Player player) {
        this.event = event;
        this.jail = jail;
        this.cell = cell;
        this.prisoner = prisoner;
        this.player = player;
    }

    /** Gets the {@link PlayerDeathEvent} which triggered this PrisonerDeathEvent. */
    public PlayerDeathEvent getPlayerDeathEvent() {
        return this.event;
    }

    /** Gets the {@link Jail} this prisoner is in. */
    public Jail getJail() {
        return this.jail;
    }

    /** Gets the cell the prisoner is in, can be null. */
    public Cell getCell() {
        return this.cell;
    }
    
    /** Checks if there is a cell involved. */
    public boolean hasCell() {
    	return this.cell != null;
    }

    /** Gets the {@link Prisoner}'s data. */
    public Prisoner getPrisoner() {
        return this.prisoner;
    }

    /** Gets the instance of the player who died. */
    public Player getPlayer() {
        return this.player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
