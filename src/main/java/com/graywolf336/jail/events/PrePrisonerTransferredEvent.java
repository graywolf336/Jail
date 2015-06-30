package com.graywolf336.jail.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;

/**
 * Event thrown before we transfer a prisoner, both offline and online prisoner.
 * 
 * <p />
 * 
 * This event is called right before we actually transfer a prisoner, and is cancellable, whether the prisoner is offline or online, getPlayer() will always return null if isOnline() return false.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.1
 */
public class PrePrisonerTransferredEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Jail originalJail, targetJail;
    private Cell originalCell, targetCell;
    private Prisoner prisoner;
    private Player player;
    private String transferor, cancelMsg;

    /**
     * Creates a new {@link PrePrisonerTransferredEvent prisoner transferred event} for the given player before they get transferred to their jail and cell.
     * 
     * @param originalJail The jail the prisoner is coming from.
     * @param originalCell The cell the prisoner is coming from, can be null.
     * @param targetJail The jail the prisoner is going to.
     * @param targetCell The cell the prisoner is going to, can be null.
     * @param prisoner The prisoner data.
     * @param player The player being jailed.
     * @param transferor The name of what is transferring this prisoner.
     */
    public PrePrisonerTransferredEvent(Jail originalJail, Cell originalCell, Jail targetJail, Cell targetCell, Prisoner prisoner, Player player, String transferor) {
        this.originalJail = originalJail;
        this.originalCell = originalCell;
        this.targetJail = targetJail;
        this.targetCell = targetCell;
        this.prisoner = prisoner;
        this.player = player;
        this.transferor = transferor;
        this.cancelMsg = "";
    }

    /** Gets the {@link Jail} this prisoner is coming from. */
    public Jail getOriginalJail() {
        return this.originalJail;
    }

    /** Gets the {@link Cell} this prisoner is coming from, can be null. */
    public Cell getOriginalCell() {
        return this.originalCell;
    }
    
    /** Checks if there is an original cell involved. */
    public boolean hasOriginalCell() {
    	return this.originalCell != null;
    }

    /** Gets the {@link Jail} this prisoner is being transferred to. */
    public Jail getTargetJail() {
        return this.targetJail;
    }

    /**
     * Sets the target jail where this prisoner is being sent to.
     * 
     * @param jail The {@link Jail} this prisoner should be sent to instead of what it was.
     */
    public void setTargetJail(Jail jail) {
        this.targetJail = jail;
    }

    /** Gets the {@link Cell} this prisoner is being sent to, can be null.
     * 
     * Will return null if the cell is not in the targetJail.
     */
    public Cell getTargetCell() {
        if(this.targetJail.isValidCell(this.targetCell.getName())) return this.targetCell;
        else return null;
    }
    
    /** Checks if there is a target cell involved. */
    public boolean hasTargetCell() {
    	return this.getTargetCell() != null;
    }

    /**
     * Sets the target {@link Cell} this prisoner is being sent to.
     * 
     * @param cell The {@link Cell} this prisoner should be sent to instead of what it was.
     */
    public void setTargetCell(Cell cell) {
        this.targetCell = cell;
    }

    /** Gets the {@link Prisoner} data for this prisoner. */
    public Prisoner getPrisoner() {
        return this.prisoner;
    }

    /** Gets the instance of the player being transferred <strong>but will return null if {@link #isOnline()} returns false</strong>. */
    public Player getPlayer() {
        return this.player;
    }

    /** Gets whether the prisoner being transferred is online or not. */
    public boolean isPlayerOnline() {
        return player != null;
    }

    /** Gets the name of what is transferring this prisoner. */
    public String getTransferor() {
        return this.transferor;
    }

    /**
     * Sets the prisoner whom the data should say jailed this prisoner.
     * 
     * @param transferor The name to put who is the jailer for this prisoner.
     */
    public void setTransferor(String transferor) {
        this.transferor = transferor;
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
