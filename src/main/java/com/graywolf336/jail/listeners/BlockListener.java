package com.graywolf336.jail.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.enums.Settings;

public class BlockListener implements Listener {
	private JailMain pl;
	
	public BlockListener(JailMain plugin) {
		this.pl = plugin;
	}
	
	@EventHandler(ignoreCancelled=true, priority = EventPriority.HIGHEST)
	public void blockBreak(BlockBreakEvent event) {
		//If we're in debugging mode, let's send the player what block they're breaking.
		if(pl.inDebug()) event.getPlayer().sendMessage("[Jail Debug]: " + event.getBlock().getType().toString());
				
		//If we are protecting against block breaking, then let's do the action.
		//If we're not, let's not use any processing power to get the jail
		//where this block was broke at
		if(pl.getConfig().getBoolean(Settings.BLOCKBREAKPROTECTION.getPath())) {
			//If there is no jail let's skedaddle
			if(pl.getJailManager().getJailFromLocation(event.getBlock().getLocation()) == null) return;
			
			//If the player doesn't have permission to modify the jail,
			//then we stop it here. We won't be doing any of the additions to
			//a prisoner's sentence here as that's for the protections listener
			if(!event.getPlayer().hasPermission("jail.modifyjail")) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true, priority = EventPriority.HIGHEST)
	public void blockPlace(BlockPlaceEvent event) {
		//If we're in debugging mode, let's send the player what block they're placing.
		if(pl.inDebug()) event.getPlayer().sendMessage("[Jail Debug]: " + event.getBlock().getType().toString());
		
		//If we are protecting against block placing, then let's do the action.
		//If we're not, let's not use any processing power to get the jail
		//where this block was placed at
		if(pl.getConfig().getBoolean(Settings.BLOCKPLACEPROTECTION.getPath())) {
			//If there is no jail let's skedaddle
			if(pl.getJailManager().getJailFromLocation(event.getBlock().getLocation()) == null) return;
			
			//If the player doesn't have permission to modify the jail,
			//then we stop it here. We won't be doing any of the additions to
			//a prisoner's sentence here as that's for the protections listener
			if(!event.getPlayer().hasPermission("jail.modifyjail")) {
				event.setCancelled(true);
			}
		}
	}
}
