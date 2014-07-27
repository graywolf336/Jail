package com.graywolf336.jail.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.beans.Stick;

/**
 * Event thrown before we a player is jailed by someone hitting them with a {@link Stick jail stick}.
 * 
 * <p />
 * 
 * This event is called right before we actually jail a player, and is cancellable.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class PrePrisonerJailedByJailStickEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Jail jail;
    private Cell cell;
    private Prisoner prisoner;
    private Player player;
    private String jailer, cancelMsg;
    private Stick stick;

    /**
     * Creates a new {@link PrePrisonerJailedByJailStickEvent prisoner jailed by a jail stick event} for the given player before they get sent to jail.
     * 
     * @param jail The jail the prisoner will be jailed at.
     * @param cell The cell we're going to be sending the prisoner to, can be null.
     * @param prisoner The prisoner data.
     * @param player The player being jailed.
     * @param jailer The name of what jailed this prisoner.
     * @param stick The {@link Stick jail stick} used.
     */
    public PrePrisonerJailedByJailStickEvent(Jail jail, Cell cell, Prisoner prisoner, Player player, String jailer, Stick stick) {
        this.jail = jail;
        this.cell = cell;
        this.prisoner = prisoner;
        this.player = player;
        this.jailer = jailer;
        this.stick = stick;
        this.cancelMsg = "";
    }

    /** Gets the {@link Jail} this prisoner is being sent to. */
    public Jail getJail() {
        return this.jail;
    }

    /** Gets the cell we're going to be sending the prisoner to. */
    public Cell getCell() {
        return this.cell;
    }

    /** Sets the cell we're going to be sending the prisoner to. */
    public void setCell(Cell cell) {
        this.cell = cell;
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
        return this.jailer;
    }

    /**
     * Sets who jailed this prisoner.
     * 
     * @param jailer The name to put who is the jailer for this prisoner.
     */
    public void setJailer(String jailer) {
        this.jailer = jailer;
    }

    /** Gets the jail stick used. */
    public Stick getJailStick() {
        return this.stick;
    }

    /** Checks whether this event is cancelled or not. */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /** Sets whether this event should be cancelled. */
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /** Returns the cancelled message. */
    public String getCancelledMessage() {
        return this.cancelMsg;
    }

    /** Sets the cancelled message. */
    public void setCancelledMessage(String msg) {
        this.cancelMsg = msg;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
