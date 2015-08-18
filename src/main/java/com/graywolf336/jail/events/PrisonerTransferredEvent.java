package com.graywolf336.jail.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;

/**
 * Event thrown after we transfer a prisoner, both an offline and an online prisoner.
 * 
 * <br /><br />
 * 
 * This event is called after we transfer the prisoner whether the prisoner is offline or online, getPlayer() will always return null if isOnline() return false.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class PrisonerTransferredEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Jail originalJail, targetJail;
    private Cell originalCell, targetCell;
    private Prisoner prisoner;
    private Player player;

    /**
     * Creates a new {@link PrisonerTransferredEvent prisoner transferred event} for the given player after they get transferred to their new jail and cell.
     * 
     * @param originalJail The jail the prisoner is coming from.
     * @param originalCell The cell the prisoner is coming from, can be null.
     * @param targetJail The jail the prisoner went to.
     * @param targetCell The cell the prisoner went to, can be null.
     * @param prisoner The prisoner data.
     * @param player The player being jailed.
     */
    public PrisonerTransferredEvent(Jail originalJail, Cell originalCell, Jail targetJail, Cell targetCell, Prisoner prisoner, Player player) {
        this.originalJail = originalJail;
        this.originalCell = originalCell;
        this.targetJail = targetJail;
        this.targetCell = targetCell;
        this.prisoner = prisoner;
        this.player = player;
    }

    /** Gets the {@link Jail} this prisoner is coming from. */
    public Jail getOriginalJail() {
        return this.originalJail;
    }

    /** Gets the {@link Cell} this prisoner is coming from, can be null. */
    public Cell getOriginalCell() {
        return this.originalCell;
    }
    
    /** Checks if there was an original cell involved. */
    public boolean hasOriginalCell() {
    	return this.originalCell != null;
    }

    /** Gets the {@link Jail} this prisoner is being transferred to. */
    public Jail getTargetJail() {
        return this.targetJail;
    }

    /** Gets the {@link Cell} this prisoner is being sent to, can be null.
     * 
     * Will return null if the cell is not in the targetJail.
     */
    public Cell getTargetCell() {
        if(this.targetJail.isValidCell(this.targetCell.getName())) return this.targetCell;
        else return null;
    }
    
    /** Checks if there was a target cell involved. */
    public boolean hasTargetCell() {
    	return this.getTargetCell() != null;
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
    public boolean isOnline() {
        return player == null;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
