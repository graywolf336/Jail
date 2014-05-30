package com.graywolf336.jail.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.beans.Stick;
import com.graywolf336.jail.enums.LangString;
import com.graywolf336.jail.enums.Settings;
import com.graywolf336.jail.events.PrePrisonerJailedByJailStickEvent;

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
						pl.debug("Stepping into creating a jail.");
						jm.getJailCreationSteps().step(jm, p, jm.getJailCreationPlayer(p.getName()), loc);
					}else if(jm.isCreatingACell(p.getName())) {
						pl.debug("Stepping into creating a cell.");
						jm.getCellCreationSteps().step(jm, p, jm.getCellCreationPlayer(p.getName()), loc);
					}
					
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true, priority = EventPriority.LOW)
	public void chatting(AsyncPlayerChatEvent event) {
		if(pl.getJailManager().isPlayerJailed(event.getPlayer().getUniqueId())) {
			if(pl.getJailManager().getPrisoner(event.getPlayer().getUniqueId()).isMuted()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(pl.getJailIO().getLanguageString(LangString.MUTED));
			}
		}
		
		//If the config has receive messages set to false, let's remove all the prisoners
		//from getting the chat messages.
		if(!pl.getConfig().getBoolean(Settings.RECIEVEMESSAGES.getPath())) {
			if(pl.inDebug()) pl.getLogger().info("Debug - There are " + event.getRecipients().size() + " players getting the message before.");
			Set<Player> rec = new HashSet<Player>(event.getRecipients());
			
			for(Jail j : pl.getJailManager().getJails())
				for(Prisoner p : j.getAllPrisoners().values())
					rec.remove(pl.getServer().getPlayer(p.getUUID()));
			
			event.getRecipients().clear();
			event.getRecipients().addAll(rec);
			if(pl.inDebug()) pl.getLogger().info("Debug - There are now " + event.getRecipients().size() + " players getting the message.");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void checkForOfflineJailStuff(PlayerJoinEvent event) {
		//Let's check if the player is jailed
		if(pl.getJailManager().isPlayerJailed(event.getPlayer().getUniqueId())) {
			//Get the prisoner object
			Jail j = pl.getJailManager().getJailPlayerIsIn(event.getPlayer().getUniqueId());
			Prisoner p = j.getPrisoner(event.getPlayer().getUniqueId());
			//update their last known username when they login
			p.setLastKnownName(event.getPlayer().getName());
			
			//Check if they're offline pending, as if this is true then they were jailed offline
			if(p.isOfflinePending()) {
				if(p.getRemainingTime() == 0L) {
					//If their remaining time is 0, let's unjail them
					pl.getPrisonerManager().releasePrisoner(event.getPlayer(), p);
				}else if(p.isToBeTransferred()) {
					Cell c = j.getCellPrisonerIsIn(event.getPlayer().getUniqueId());
					
					//If the player is not jailed in a cell, teleport them to the jail's in
					if(c == null) {
						p.setTeleporting(true);
						event.getPlayer().teleport(j.getTeleportIn());
						p.setTeleporting(false);
					}else {
						//If they are in a cell, teleport them into that cell
						p.setTeleporting(true);
						event.getPlayer().teleport(c.getTeleport());
						p.setTeleporting(false);
					}
					
					p.setToBeTransferred(false);
				} else {
					//Their remaining time isn't 0 so let's proceed with jailing of the prisoner
					pl.getPrisonerManager().jailPlayer(event.getPlayer().getUniqueId());
				}
			}
			
			//Add the scoreboard to them if it is enabled
			if(pl.getConfig().getBoolean(Settings.SCOREBOARDENABLED.getPath())) {
				pl.getScoreBoardManager().addScoreBoard(event.getPlayer(), p);
			}
			
			//if we are ignoring a prisoner's sleeping state, then let's set that
			if(pl.getConfig().getBoolean(Settings.IGNORESLEEPINGSTATE.getPath())) {
				event.getPlayer().setSleepingIgnored(true);
			}
		}
	}
	
	@EventHandler
	public void handleGoingOffline(PlayerQuitEvent event) {
		if(pl.getJailManager().isPlayerJailed(event.getPlayer().getUniqueId())) {
			//Remove the scoreboard to them if it is enabled
			if(pl.getConfig().getBoolean(Settings.SCOREBOARDENABLED.getPath())) {
				pl.getScoreBoardManager().removeScoreBoard(event.getPlayer());
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void handleGettingKicked(PlayerKickEvent event) {
		if(pl.getJailManager().isPlayerJailed(event.getPlayer().getUniqueId())) {
			//Remove the scoreboard to them if it is enabled
			if(pl.getConfig().getBoolean(Settings.SCOREBOARDENABLED.getPath())) {
				pl.getScoreBoardManager().removeScoreBoard(event.getPlayer());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void foodControl(FoodLevelChangeEvent event) {
		if(pl.getConfig().getBoolean(Settings.FOODCONTROL.getPath())) {
			if(pl.getJailManager().isPlayerJailed(event.getEntity().getUniqueId())) {
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
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if(pl.getJailManager().isPlayerJailed(event.getPlayer().getUniqueId())) {
			Jail j = pl.getJailManager().getJailPlayerIsIn(event.getPlayer().getUniqueId());
			
			if(j.isJailedInACell(event.getPlayer().getUniqueId())) {
				event.setRespawnLocation(j.getCellPrisonerIsIn(event.getPlayer().getUniqueId()).getTeleport());
			}else {
				event.setRespawnLocation(j.getTeleportIn());
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void jailStickHandling(EntityDamageByEntityEvent event) {
		//If the damager and the entity getting damage is not a player,
		//we don't want to handle it in this method
		if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
		
		Player attacker = (Player) event.getDamager();
		Player player = (Player) event.getEntity();
		
		if(pl.getJailStickManager().isUsingJailStick(attacker)) {
			if(pl.getJailStickManager().isValidStick(attacker.getItemInHand().getType())) {
				if(attacker.hasPermission("jail.usejailstick." + attacker.getItemInHand().getType().toString().toLowerCase())) {
					//The person the attacker is trying to jail stick is already jailed, don't handle that
					if(pl.getJailManager().isPlayerJailed(player.getUniqueId())) {
						attacker.sendMessage(pl.getJailIO().getLanguageString(LangString.ALREADYJAILED, player.getName()));
					}else {
						if(player.hasPermission("jail.cantbejailed")) {
							attacker.sendMessage(pl.getJailIO().getLanguageString(LangString.CANTBEJAILED));
						}else {
							Stick s = pl.getJailStickManager().getStick(attacker.getItemInHand().getType());
							
							Prisoner p = new Prisoner(player.getUniqueId().toString(), player.getName(),
									pl.getConfig().getBoolean(Settings.AUTOMATICMUTE.getPath()),
									s.getTime(), attacker.getName(), s.getReason());
							
							PrePrisonerJailedByJailStickEvent jEvent = new PrePrisonerJailedByJailStickEvent(
									pl.getJailManager().getJail(s.getJail()), null, p, player, attacker.getName(), s);
							
							pl.getServer().getPluginManager().callEvent(jEvent);
							
							if(jEvent.isCancelled()) {
								if(jEvent.getCancelledMessage().isEmpty())
									attacker.sendMessage(pl.getJailIO().getLanguageString(LangString.CANCELLEDBYANOTHERPLUGIN, player.getName()));
								else
									attacker.sendMessage(jEvent.getCancelledMessage());
							}else {
								//recall data from the event
								Jail j = jEvent.getJail();
								Cell c = jEvent.getCell();
								p = jEvent.getPrisoner();
								player = jEvent.getPlayer();
								
								//Player is not online
								if(player == null) {
									attacker.sendMessage(pl.getJailIO().getLanguageString(LangString.OFFLINEJAIL,
											new String[] { p.getLastKnownName(), String.valueOf(p.getRemainingTimeInMinutes()) }));
								}else {
									//Player *is* online
									attacker.sendMessage(pl.getJailIO().getLanguageString(LangString.ONLINEJAIL,
											new String[] { p.getLastKnownName(), String.valueOf(p.getRemainingTimeInMinutes()) }));
								}
								
								try {
									pl.getPrisonerManager().prepareJail(j, c, player, p);
								} catch (Exception e) {
									attacker.sendMessage(ChatColor.RED + e.getMessage());
								}
							}
						}
					}
				}
			}
		}
	}
}
