package com.graywolf336.jail.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.Util;

public class PlayerListener implements Listener {
	private JailMain pl;
	
	public PlayerListener(JailMain plugin) {
		this.pl = plugin;
	}
	
	@EventHandler
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
	
	@EventHandler
	public void chatting(AsyncPlayerChatEvent event) {
		if(pl.getJailManager().isPlayerJailed(event.getPlayer().getName())) {
			if(pl.getJailManager().getPrisoner(event.getPlayer().getName()).isMuted()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "Stop talking, you're currently jailed and muted.");
			}
		}
	}
	
	@EventHandler
	public void checkForOfflineJail(PlayerJoinEvent event) {
		if(pl.getJailManager().isPlayerJailed(event.getPlayer().getName())) {
			if(pl.getJailManager().getJailPlayerIsIn(event.getPlayer().getName()).getPrisoner(event.getPlayer().getName()).isOfflinePending()) {
				pl.getPrisonerManager().jailPrisoner(event.getPlayer().getName());
			}
		}
	}
}
