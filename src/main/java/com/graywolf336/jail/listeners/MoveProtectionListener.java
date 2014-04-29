package com.graywolf336.jail.listeners;

import java.util.concurrent.TimeUnit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.enums.LangString;
import com.graywolf336.jail.enums.Settings;

public class MoveProtectionListener implements Listener {
	private JailMain pl;
	
	public MoveProtectionListener(JailMain plugin) {
		this.pl = plugin;
	}
	
	@EventHandler(ignoreCancelled=true)
	public void moveProtection(PlayerMoveEvent event) {
		//If we have the move protection enabled, then let's do it.
		//Other wise we don't need to deal with it.
		if(pl.getConfig().getBoolean(Settings.MOVEPROTECTION.getPath())) {
			//Let's be sure the player we're dealing with is in jail
			if(pl.getJailManager().isPlayerJailed(event.getPlayer().getUniqueId())) {
				Jail j = pl.getJailManager().getJailPlayerIsIn(event.getPlayer().getUniqueId());
				Prisoner p = j.getPrisoner(event.getPlayer().getUniqueId());
				
				//If the player is being teleported, let's ignore it
				if(p.isTeleporting()) {
					return;
				}
				
				//They moved, so they're no longer afk
				p.setAFKTime(0L);
				
				//If the event's to location is NOT inside the jail, then let's do some action.
				//For right now, we're only going to apply the time. Later we're going to do
				//the guards, but first get a beta version out.
				if (!j.isInside(event.getTo())) {
					try {
						long add = Util.getTime(pl.getConfig().getString(Settings.MOVEPENALTY.getPath()));
						pl.getJailManager().getPrisoner(event.getPlayer().getUniqueId()).addTime(add);
						
						String msg = "";
						if(add == 0L) {
							//Generate the protection message, provide the method with one argument
							//which is the thing we are protecting against
							msg = pl.getJailIO().getLanguageString(LangString.PROTECTIONMESSAGENOPENALTY, pl.getJailIO().getLanguageString(LangString.MOVING));
						}else {
							//Generate the protection message, provide the method with two arguments
							//First is the time in minutes and second is the thing we are protecting against
							msg = pl.getJailIO().getLanguageString(LangString.PROTECTIONMESSAGE,
									new String[] { String.valueOf(TimeUnit.MINUTES.convert(add, TimeUnit.MILLISECONDS)),
									pl.getJailIO().getLanguageString(LangString.MOVING) });
						}
						
						//Send the message
						event.getPlayer().sendMessage(msg);
					}catch(Exception e) {
						pl.getLogger().severe("Moving (escaping) outside a jail penalty time is in the wrong format, please fix.");
					}
					
					//If the prisoner is in a cell, then let's teleport them to the cell's in location
					if(j.isJailedInACell(event.getPlayer().getUniqueId())) {
						event.setTo(j.getCellPrisonerIsIn(event.getPlayer().getUniqueId()).getTeleport());
					}else {
						//Otherwise let's teleport them to the in location of the jail
						event.setTo(j.getTeleportIn());
					}
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		PlayerMoveEvent move = new PlayerMoveEvent(event.getPlayer(), event.getFrom(), event.getTo());
		moveProtection(move);
	}
}
