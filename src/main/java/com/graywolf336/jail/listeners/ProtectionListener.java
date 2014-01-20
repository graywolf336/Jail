package com.graywolf336.jail.listeners;

import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
						
						//Send the message
						event.getPlayer().sendMessage(msg);
					}catch (Exception e) {
						pl.getLogger().severe("Block break penalty's time is in the wrong format, please fix.");
					}
					
					//Stop the event from happening, as the block wasn't in the whitelist
					event.setCancelled(true);
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
						
						//Send the message
						event.getPlayer().sendMessage(msg);
					}catch (Exception e) {
						pl.getLogger().severe("Block place penalty's time is in the wrong format, please fix.");
					}
					
					//Stop the event from happening, as the block wasn't in the whitelist
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void commandProtection(PlayerCommandPreprocessEvent event) {
		//Before we check if the player is jailed, let's save a
		//tiny bit of resources and check if this protection is enabled
		if(pl.getConfig().getBoolean(Settings.COMMANDPROTECTION.getPath())) {
			//Let's check if this player is jailed, if so then we continue
			//otherwise we don't care about commands in here
			if(pl.getJailManager().isPlayerJailed(event.getPlayer().getName())) {
				boolean match = false;
				
				for(String whited : pl.getConfig().getStringList(Settings.COMMANDWHITELIST.getPath()))
					if(event.getMessage().toLowerCase().startsWith(whited.toLowerCase()))
						match = true;
				
				//If no match found in the whitelist, then let's block this command.
				if(!match) {
					try {
						long add = Util.getTime(pl.getConfig().getString(Settings.COMMANDPENALTY.getPath()));
						pl.getJailManager().getPrisoner(event.getPlayer().getName()).addTime(add);
						
						//Generate the protection message, provide the method with two arguments
						//First is the time in minutes and second is the thing we are protecting against
						String msg = pl.getJailIO().getLanguageString(LangString.PROTECTIONMESSAGE,
								new String[] { String.valueOf(TimeUnit.MINUTES.convert(add, TimeUnit.MILLISECONDS)),
								pl.getJailIO().getLanguageString(LangString.COMMAND) });
						
						//Send the message
						event.getPlayer().sendMessage(msg);
					}catch (Exception e) {
						pl.getLogger().severe("Command Protection penalty's time is in the wrong format, please fix.");
					}
					
					//Stop the command from happening, as it wasn't whitelisted
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void chestProtection(PlayerInteractEvent event) {
		//First thing is first, let's be sure the player we're dealing with is in jail
		if(pl.getJailManager().isPlayerJailed(event.getPlayer().getName())) {
			//Next, let's check if it is a chest and if they're in a cell
			//If they are in a cell and are opening a chest, then we check
			//the config to see if they can open the chests
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST) {
				//Let's get the cell the player is in, then check if it is null or not.
				if(pl.getJailManager().getJailPlayerIsIn(event.getPlayer().getName()).isJailedInACell(event.getPlayer().getName())) {
					if(pl.getConfig().getBoolean(Settings.OPENCHEST.getPath())) {
						//The prisoner is in a cell, so let's check if it is a couple chest.
						Material bpos1 = event.getClickedBlock().getLocation().add(-1, 0, 0).getBlock().getType();
					    Material bpos2 = event.getClickedBlock().getLocation().add(+1, 0, 0).getBlock().getType();
					    Material bpos3 = event.getClickedBlock().getLocation().add(0, 0, -1).getBlock().getType();
					    Material bpos4 = event.getClickedBlock().getLocation().add(0, 0, +1).getBlock().getType();
					    
					    boolean pos1 = bpos1 == Material.CHEST || bpos1 == Material.TRAPPED_CHEST;
					    boolean pos2 = bpos2 == Material.CHEST || bpos2 == Material.TRAPPED_CHEST;
					    boolean pos3 = bpos3 == Material.CHEST || bpos3 == Material.TRAPPED_CHEST;
					    boolean pos4 = bpos4 == Material.CHEST || bpos4 == Material.TRAPPED_CHEST;
					    
					    if(pos1 || pos2 || pos3 || pos4) {
					    	//it is a double chest, so they're free to go!
					    	if(pl.inDebug()) event.getPlayer().sendMessage("[Jail Debug]: You're opening up a double chest.");
					    }else {
					    	//it is not a double chest, so we won't be allowing it.
					    	event.setCancelled(true);
					    	return;
					    }
					}else {
						//the config has opening chests disabled
				    	event.setCancelled(true);
				    	return;
					}
				}else {
					//The prisoner is not in a cell, so let's not allow it. IF we get feedback from people who
					//use the plugin, this might get removed or permission node might be added.
					event.setCancelled(true);
					return;
				}
			}
		}
	}
}
