package com.graywolf336.jail.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.enums.LangString;
import com.graywolf336.jail.enums.Settings;

public class PlayerListener implements Listener {
	private JailMain pl;
	
	public PlayerListener(JailMain plugin) {
		this.pl = plugin;
	}
	
	@EventHandler(ignoreCancelled=true)
	public void jailOrCellCreation(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player p = event.getPlayer();
			Location loc = event.getClickedBlock() == null ? p.getLocation() : event.getClickedBlock().getLocation();
			JailManager jm = pl.getJailManager();
			
			if(p.getItemInHand().isSimilar(Util.getWand())) {
				if(jm.isCreatingSomething(p.getName())) {
					if(jm.isCreatingAJail(p.getName())) {
						jm.getJailCreationSteps().step(jm, p, jm.getJailCreationPlayer(p.getName()), loc);
					}else if(jm.isCreatingACell(p.getName())) {
						jm.getCellCreationSteps().step(jm, p, jm.getCellCreationPlayer(p.getName()), loc);
					}
					
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true, priority = EventPriority.LOW)
	public void chatting(AsyncPlayerChatEvent event) {
		if(pl.getJailManager().isPlayerJailed(event.getPlayer().getName())) {
			if(pl.getJailManager().getPrisoner(event.getPlayer().getName()).isMuted()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(pl.getJailIO().getLanguageString(LangString.MUTED));
			}
		}
		
		//If the config has receive messages set to false, let's remove all the prisoners
		//from getting the chat messages.
		if(!pl.getConfig().getBoolean(Settings.RECIEVEMESSAGES.getPath())) {
			if(pl.inDebug()) pl.getLogger().info("Debug - There are " + event.getRecipients().size() + " players getting the message before.");
			Set<Player> rec = new HashSet<Player>(event.getRecipients());
			
			for(Jail j : pl.getJailManager().getJails()) {
				for(Prisoner p : j.getAllPrisoners()) {
					rec.remove(pl.getServer().getPlayerExact(p.getName()));
				}
			}
			
			event.getRecipients().clear();
			event.getRecipients().addAll(rec);
			if(pl.inDebug()) pl.getLogger().info("Debug - There are now " + event.getRecipients().size() + " players getting the message.");
		}
	}
	
	@EventHandler
	public void checkForOfflineJailStuff(PlayerJoinEvent event) {
		//Let's check if the player is jailed
		if(pl.getJailManager().isPlayerJailed(event.getPlayer().getName())) {
			//Get the prisoner object
			Prisoner p = pl.getJailManager().getJailPlayerIsIn(event.getPlayer().getName()).getPrisoner(event.getPlayer().getName());
			//Check if they're offline pending, as if this is true then they were jailed offline
			if(p.isOfflinePending()) {
				if(p.getRemainingTime() == 0L) {
					//If their remaining time is 0, let's unjail them
					pl.getPrisonerManager().releasePrisoner(event.getPlayer(), p);
				}else {
					//Their remaining time isn't 0 so let's proceed with jailing of the prisoner
					pl.getPrisonerManager().jailPrisoner(event.getPlayer().getName());
				}
			}
			
			//if we are ignoring a prisoner's sleeping state, then let's set that
			if(pl.getConfig().getBoolean(Settings.IGNORESLEEPINGSTATE.getPath())) {
				event.getPlayer().setSleepingIgnored(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void foodControl(FoodLevelChangeEvent event) {
		if(pl.getConfig().getBoolean(Settings.FOODCONTROL.getPath())) {
			if(pl.getJailManager().isPlayerJailed(event.getEntity().getName())) {
				int min = pl.getConfig().getInt(Settings.FOODCONTROLMIN.getPath());
				int max = pl.getConfig().getInt(Settings.FOODCONTROLMAX.getPath());
				
				if (event.getFoodLevel() <  min) {
					event.setFoodLevel(min);
				}else {
					event.setFoodLevel(max);
				}
			}
		}
	}
}
