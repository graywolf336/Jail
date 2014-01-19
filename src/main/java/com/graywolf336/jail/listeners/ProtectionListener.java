package com.graywolf336.jail.listeners;

import java.util.concurrent.TimeUnit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.enums.LangString;
import com.graywolf336.jail.enums.Settings;

public class ProtectionListener implements Listener {
	private JailMain pl;
	
	public ProtectionListener(JailMain plugin) {
		this.pl = plugin;
	}
	
	@EventHandler(ignoreCancelled=true)
	public void protectionBlockBreaking(BlockBreakEvent event) {
		//Before we check if the player is jailed, let's save a
		//tiny bit of resources and check if this protection is enabled
		if(pl.getConfig().getBoolean(Settings.BLOCKBREAKPROTECTION.getPath())) {
			//Let's check if the player is jailed, otherwise the other listener
			//in the BlockListener class will take care of protecting inside
			//of the jails.
			if(pl.getJailManager().isPlayerJailed(event.getPlayer().getName())) {
				//Get the breaking whitelist, check if the current item is in there
				//the list must be lowercase, need to stress that
				if(!Util.isStringInsideList(pl.getConfig().getStringList(Settings.BLOCKBREAKWHITELIST.getPath()),
						event.getBlock().getType().toString().toLowerCase())) {
					//As our Util.getTime throws an exception when the time is in an
					//incorrect format, we catch the exception and don't add any time
					//as a fail safe, don't want us to go crazy adding tons of time.
					try {
						long add = Util.getTime(pl.getConfig().getString(Settings.BLOCKBREAKPENALTY.getPath()));
						pl.getJailManager().getPrisoner(event.getPlayer().getName()).addTime(add);
						
						//Generate the protection message, provide the method with two arguments
						//First is the time in minutes and second is the thing we are protecting against
						String msg = pl.getJailIO().getLanguageString(LangString.PROTECTIONMESSAGE,
								new String[] { String.valueOf(TimeUnit.MINUTES.convert(add, TimeUnit.MILLISECONDS)),
								pl.getJailIO().getLanguageString(LangString.BLOCKBREAKING) });
						
						//Send the message and then stop the event from happening
						event.getPlayer().sendMessage(msg);
						event.setCancelled(true);
					}catch (Exception e) {
						pl.getLogger().severe("Block break penalty's time is in the wrong format, please fix.");
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void protectionBlockPlacing(BlockPlaceEvent event) {
		//Before we check if the player is jailed, let's save a
		//tiny bit of resources and check if this protection is enabled
		if(pl.getConfig().getBoolean(Settings.BLOCKPLACEPROTECTION.getPath())) {
			//Let's check if the player is jailed, otherwise the other listener
			//in the BlockListener class will take care of protecting inside
			//of the jails.
			if(pl.getJailManager().isPlayerJailed(event.getPlayer().getName())) {
				//Get the placing whitelist, check if the current item is in there
				//the list must be lowercase, need to stress that
				if(!Util.isStringInsideList(pl.getConfig().getStringList(Settings.BLOCKPLACEWHITELIST.getPath()),
						event.getBlock().getType().toString().toLowerCase())) {
					//As our Util.getTime throws an exception when the time is in an
					//incorrect format, we catch the exception and don't add any time
					//as a fail safe, don't want us to go crazy adding tons of time.
					try {
						long add = Util.getTime(pl.getConfig().getString(Settings.BLOCKPLACEPENALTY.getPath()));
						pl.getJailManager().getPrisoner(event.getPlayer().getName()).addTime(add);
						
						//Generate the protection message, provide the method with two arguments
						//First is the time in minutes and second is the thing we are protecting against
						String msg = pl.getJailIO().getLanguageString(LangString.PROTECTIONMESSAGE,
								new String[] { String.valueOf(TimeUnit.MINUTES.convert(add, TimeUnit.MILLISECONDS)),
								pl.getJailIO().getLanguageString(LangString.BLOCKPLACING) });
						
						//Send the message and then stop the event from happening
						event.getPlayer().sendMessage(msg);
						event.setCancelled(true);
					}catch (Exception e) {
						pl.getLogger().severe("Block place penalty's time is in the wrong format, please fix.");
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
