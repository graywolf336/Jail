package com.graywolf336.jail.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.interfaces.ICell;

/**
 * Event thrown before we are jailing a player, both offline and online players.
 * 
 * <p>
 * 
 * This event is called right before we actually jail a player, and is cancellable, whether the player is offline or online, getPlayer() will always return null if isOnline() return false.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class PrePrisonerJailedEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private boolean online;
    private Jail jail;
    private ICell cell;
    private Prisoner prisoner;
    private Player player;
    private String jailer, cancelMsg;

    /**
     * Creates a new {@link PrePrisonerJailedEvent prisoner jailed event} for the given player before they get sent to jail.
     * 
     * @param jail The jail the prisoner will be jailed at.
     * @param c The cell we're going to be sending the prisoner to, can be null.
     * @param prisoner The prisoner data.
     * @param player The player being jailed.
     * @param online Whether the player is online or not.
     * @param jailer The name of what jailed this prisoner.
     */
    public PrePrisonerJailedEvent(Jail jail, ICell c, Prisoner prisoner, Player player, boolean online, String jailer) {
        this.jail = jail;
        this.cell = c;
        this.prisoner = prisoner;
        this.player = player;
        this.online = online;
        this.jailer = jailer;
        this.cancelMsg = "";
    }

    /** Gets the {@link Jail} this prisoner is being sent to. */
    public Jail getJail() {
        return this.jail;
    }

    /** Gets the cell we're going to be sending the prisoner to. */
    public ICell getCell() {
        return this.cell;
    }

    /** Sets the cell we're going to be sending the prisoner to. */
    public void setCell(Cell cell) {
        this.cell = cell;
    }
    
    /** Checks if there is a cell involved. */
    public boolean hasCell() {
    	return this.cell != null;
    }

    /** Gets the {@link Prisoner} data for this prisoner. */
    public Prisoner getPrisoner() {
        return this.prisoner;
    }

    /** Gets the instance of the player being jailed <strong>but will return null if {@link #isOnline()} returns false</strong>. */
    public Player getPlayer() {
        return this.player;
    }

    /** Gets whether the prisoner being jailed is online or not. */
    public boolean isOnline() {
        return this.online;
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
    
    /** Gets the reason the player was jailed for. */
    public String getReason() {
        return this.prisoner.getReason();
    }
    
    /**
     * Sets the reason the prisoner was jailed for.
     * 
     * @param reason the prisoner was jailed for.
     */
    public void setReason(String reason) {
        this.prisoner.setReason(reason);
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
