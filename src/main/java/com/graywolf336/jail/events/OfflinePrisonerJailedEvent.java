package com.graywolf336.jail.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.interfaces.ICell;

/**
 * Event thrown when a prisoner is offline put will be put into jail next time they come online.
 * 
 * <p>
 * 
 * This event is called before everything for the jailing takes place, as the player is offline.
 * This event is called for informative purposes, see {@link PrePrisonerJailedEvent}
 * for the event called before they teleported in and all that fancy stuff.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class OfflinePrisonerJailedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Jail jail;
    private ICell cell;
    private Prisoner prisoner;

    /**
     * Creates a new {@link OfflinePrisonerJailedEvent prisoner jailed event} for the given prisoner.
     * 
     * @param jail The jail the prisoner will be jailed at.
     * @param cell The cell we're going to be sending the prisoner to, can be null.
     * @param prisoner The prisoner data.
     */
    public OfflinePrisonerJailedEvent(Jail jail, ICell cell, Prisoner prisoner) {
        this.jail = jail;
        this.cell = cell;
        this.prisoner = prisoner;
    }

    /** Gets the {@link Jail} this prisoner is being sent to. */
    public Jail getJail() {
        return this.jail;
    }

    /** Gets the cell we're going to be sending the prisoner to, can be null. */
    public ICell getCell() {
        return this.cell;
    }
    
    /** Checks if there was a cell involved. */
    public boolean hasCell() {
        return this.cell != null;
    }

    /** Gets the {@link Prisoner} data for this prisoner. */
    public Prisoner getPrisoner() {
        return this.prisoner;
    }

    /** Gets the jailer who jailed this prisoner. */
    public String getJailer() {
        return this.prisoner.getJailer();
    }
    
    /** Gets the reason the prisoner was jailed. */
    public String getReason() {
        return this.prisoner.getReason();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
