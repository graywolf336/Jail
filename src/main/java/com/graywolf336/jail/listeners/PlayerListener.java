package com.graywolf336.jail.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.JailManager;

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
			
			if(jm.isCreatingSomething(p.getName())) {
				if(jm.isCreatingAJail(p.getName())) {
					
					jm.getJailCreationSteps().step(jm, p, jm.getJailCreationPlayer(p.getName()), loc);
					
				}else if(jm.isCreatingACell(p.getName())) {
					
					//One for jail cell creation
					
				}
				
				event.setCancelled(true);
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
}
