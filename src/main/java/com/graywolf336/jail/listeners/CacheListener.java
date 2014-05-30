package com.graywolf336.jail.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.beans.CachePrisoner;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.events.PrePrisonerTransferredEvent;
import com.graywolf336.jail.events.PrisonerJailedEvent;
import com.graywolf336.jail.events.PrisonerReleasedEvent;
import com.graywolf336.jail.events.PrisonerTransferredEvent;

public class CacheListener implements Listener {
	private JailMain pl;
	
	public CacheListener(JailMain plugin) {
		this.pl = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void joinListener(PlayerJoinEvent event) {
		if(pl.getJailManager().isPlayerJailed(event.getPlayer().getUniqueId())) {
			Jail j = pl.getJailManager().getJailPlayerIsIn(event.getPlayer().getUniqueId());
			Prisoner p = j.getPrisoner(event.getPlayer().getUniqueId());
			
			pl.getJailManager().addCacheObject(new CachePrisoner(j, p));
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void leaveListener(PlayerQuitEvent event) {
		if(pl.getJailManager().inCache(event.getPlayer().getUniqueId())) {
			pl.getJailManager().removeCacheObject(event.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void kickListener(PlayerKickEvent event) {
		if(pl.getJailManager().inCache(event.getPlayer().getUniqueId())) {
			pl.getJailManager().removeCacheObject(event.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void jailListener(PrisonerJailedEvent event) {
		pl.getJailManager().addCacheObject(new CachePrisoner(event.getJail(), event.getPrisoner()));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void releaseListener(PrisonerReleasedEvent event) {
		if(pl.getJailManager().inCache(event.getPlayer().getUniqueId())) {
			pl.getJailManager().removeCacheObject(event.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void beforeTransferringListener(PrePrisonerTransferredEvent event) {
		if(pl.getJailManager().inCache(event.getPlayer().getUniqueId())) {
			pl.getJailManager().removeCacheObject(event.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void transferListener(PrisonerTransferredEvent event) {
		pl.getJailManager().addCacheObject(new CachePrisoner(event.getTargetJail(), event.getPrisoner()));
	}
}
