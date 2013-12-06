package com.graywolf336.jail.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.beans.Jail;

public class PlayerPreventionsListener implements Listener {
	private JailMain pl;
	
	public PlayerPreventionsListener(JailMain plugin) {
		this.pl = plugin;
	}
	
	@EventHandler
	public void preventChat(AsyncPlayerChatEvent event) {
		if(event.isCancelled()) return;
		
		Jail j = pl.getJailManager().getJailPlayerIsIn(event.getPlayer().getName());
		
		if(j != null) {
			event.setCancelled(j.getPrisoner(event.getPlayer().getName()).isMuted());
		}
	}
}
