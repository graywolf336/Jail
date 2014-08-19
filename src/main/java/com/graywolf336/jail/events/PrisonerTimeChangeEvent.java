package com.graywolf336.jail.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;

/**
 * Event thrown when a prisoner's time changes.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class PrisonerTimeChangeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Jail jail;
    private Cell cell;
    private Prisoner prisoner;
    private Player player;
    private long before, after;

    /**
     * Creates a new {@link PrisonerTimeChangeEvent prisoner time change event} for the given prisoner.
     * 
     * @param jail The jail the prisoner is in.
     * @param cell The cell the prisoner is in, can be null.
     * @param prisoner The prisoner's data.
     * @param player The player being jailed.
     * @param before the time before it changed
     * @param after the time after it changed
     */
    public PrisonerTimeChangeEvent(Jail jail, Cell cell, Prisoner prisoner, Player player, long before, long after) {
        this.jail = jail;
        this.cell = cell;
        this.prisoner = prisoner;
        this.player = player;
        this.before = before;
        this.after = after;
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
    
    /** Gets the time the prisoner had before it changed. */
    public long getTimeBeforeChange() {
    	return this.before;
    }
    
    /** Gets the time the prisoner will have after the change. */
    public long getTimeAfterChange() {
    	return this.after;
    }
    
    /** Sets the time the prisoner will have after the change. */
    public void setTimeAfterChange(long newtime) {
    	this.after = Math.max(0, newtime);
    }
    
    /**
     * Gets the difference the change is making.
     * 
     * <p />
     * 
     * The change can be positive or negative. If it is positive then the
     * time after is smaller than it was before. If it is negative then the
     * time after is larger than it was before.
     * 
     * @return The difference between the two time changes.
     */
    public long getTimeDifference() {
    	return this.before - this.after;
    }
    
    /** Checks whether this event is cancelled or not. */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /** Sets whether this event should be cancelled. */
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
