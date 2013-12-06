package com.graywolf336.jail.events;

/**
 * Event thrown when a player is fixing to be jailed, both offline and online players.
 * 
 * This event is called right before we actually jail a player, and is cancellable, whether the player is offline or online, getPlayer() will always return null if isOnline() return false.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 0.0.0
 */
public class PrisonerJailedEvent {

}
