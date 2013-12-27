package com.graywolf336.jail;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.enums.LangString;
import com.graywolf336.jail.enums.Settings;

public class PrisonerManager {
	private JailMain pl;
	
	public PrisonerManager(JailMain plugin) {
		this.pl = plugin;
	}
	
	/**
	 * Prepare the jailing of this player.
	 * 
	 * <p />
	 * 
	 * In this we do the following:
	 * <ol>
	 * 	<li>Checks if the jail is null, if so it throws an Exception</li>
	 * 	<li>Checks if the prisoner is null, if so it throws an Exception</li>
	 * 	<li>Sets the prisoner data to offline pending or not, player == null</li>
	 * 	<li>If the cell is null, add the prisoner data to the jail otherwise we set the cell's prisoner to this one. <em>Check before here if the cell already contains a prisoner.</em></li>
	 * 	<li>Saves the jail information, goes out to the {@link JailIO} to initate a save.</li>
	 * 	<li>If the prisoner is <em>not</em> offline, we will actually {@link #jailPrisoner(Jail, Cell, Player, Prisoner) jail} them now.</li>
	 * 	<li>Does checks to get the right message for the next two items.</li>
	 * 	<li>If we broadcast the jailing, then let's broadcast it.</li>
	 * 	<li>If we log the jailing to console <em>and</em> we haven't broadcasted it, then we log it to the console.</li>
	 * </ol>
	 * 
	 * @param jail The jail instance we are sending this prisoner to
	 * @param cell The name of the cell we are sending this prisoner to
	 * @param player The player we are preparing the jail for.
	 * @param prisoner The prisoner file.
	 * @throws Exception if the jail or prisoner are null.
	 */
	public void prepareJail(Jail jail, Cell cell, Player player, Prisoner prisoner) throws Exception {
		//Do some checks of whether the passed params are null.
		if(jail == null)
			throw new Exception("The jail can not be null.");
		
		if(prisoner == null)
			throw new Exception("Prisoner data can not be null.");
		
		//Set whether the prisoner is offline or not.
		prisoner.setOfflinePending(player == null);
		
		//Now that we've got those checks out of the way, let's start preparing.
		if(cell == null) {
			jail.addPrisoner(prisoner);
		}else {
			cell.setPrisoner(prisoner);
		}
		
		//Save the jail after adding them to the jail
		pl.getJailIO().saveJail(jail);
		
		//If they are NOT offline, jail them
		if(!prisoner.isOfflinePending()) {
			jailPrisoner(jail, cell, player, prisoner);
		}
		
		//Get a message ready for broadcasting or logging.
		String msg = "";
		
		if(prisoner.getRemainingTime() < 0)
			msg = pl.getJailIO().getLanguageString(LangString.BROADCASTMESSAGEFOREVER, new String[] { prisoner.getName() });
		else//
			msg = pl.getJailIO().getLanguageString(LangString.BROADCASTMESSAGEFOREVER, new String[] { prisoner.getName(), String.valueOf(prisoner.getRemainingTimeInMinutes()) });
		
		boolean broadcasted = false;
		//Broadcast the message, if it is enabled
		if(pl.getConfig().getBoolean(Settings.BROADCASTJAILING.getPath(), false)) {
			pl.getServer().broadcastMessage(msg);
			broadcasted = true;
		}
		
		//Log the message, if it is enabled
		if(pl.getConfig().getBoolean(Settings.LOGJAILING.getPath(), true) && !broadcasted) {
			pl.getServer().getConsoleSender().sendMessage(msg);
		}
	}
	
	/**
	 * Jails the prisoner with the given name.
	 * 
	 * @param name of the prisoner to jail.
	 */
	public void jailPrisoner(String name) {
		Jail j = pl.getJailManager().getJailPlayerIsIn(name);
		
		jailPrisoner(j, j.getCellPrisonerIsIn(name), pl.getServer().getPlayerExact(name), j.getPrisoner(name));
	}
	
	/**
	 * Jails the prisoner with the proper information given.
	 * 
	 * @param jail where they are going
	 * @param cell where they are being placed in, can be null
	 * @param player who is the prisoner
	 * @param prisoner data containing everything pertaining to them
	 */
	public void jailPrisoner(Jail jail, Cell cell, Player player, Prisoner prisoner) {
		//They are no longer offline, so set that.
		prisoner.setOfflinePending(false);
		
		//We are getting ready to teleport them, so set it to true so that
		//the *future* move checkers won't be cancelling our moving.
		prisoner.setTeleporting(true);
		
		//If their reason is empty send proper message, else send other proper message
		if(prisoner.getReason().isEmpty()) {
			player.sendMessage(pl.getJailIO().getLanguageString(LangString.JAILED));
		}else {
			player.sendMessage(pl.getJailIO().getLanguageString(LangString.JAILEDWITHREASON, new String[] { prisoner.getReason() }));
		}
		
		//If the config has inventory deletion, then let's delete it
		if(pl.getConfig().getBoolean(Settings.DELETEINVENTORY.getPath(), false)) {
			player.getInventory().setArmorContents(null);
			player.getInventory().clear();
		}
		
		//If the config has releasing them back to their previous position,
		//then let's set it in the prisoner data.
		if(pl.getConfig().getBoolean(Settings.RELEASETOPREVIOUSPOSITION.getPath(), false)) {
			prisoner.setPreviousPosition(player.getLocation());
		}
		
		//If the config has restoring their previous gamemode enabled,
		//then let's set it in their prisoner data.
		if(pl.getConfig().getBoolean(Settings.RESTOREPREVIOUSGAMEMODE.getPath(), false)) {
			prisoner.setPreviousGameMode(player.getGameMode());
		}
		
		//Set their gamemode to the one in the config, if we get a null value
		//from the parsing then we set theirs to adventure
		try {
			player.setGameMode(GameMode.valueOf(pl.getConfig().getString(Settings.JAILEDGAMEMODE.getPath(), "ADVENTURE").toUpperCase()));
		}catch(Exception e) {
			pl.getLogger().severe("Your jailedgamemode setting is incorrect, please fix.");
			player.setGameMode(GameMode.ADVENTURE);
		}
		
		//only eject them if they're inside a vehicle and also eject anyone else on top of them
    	if(player.isInsideVehicle()) {
    		player.getVehicle().eject();
    		player.getPassenger().eject();
    		player.eject();
    	}
    	
    	//If the cell doesn't equal null, then let's put them in the jail
    	if(cell != null) {
    		
    	}else {
    		//There is no cell we're jailing them to, so stick them in the jail
    	}
	}
}
