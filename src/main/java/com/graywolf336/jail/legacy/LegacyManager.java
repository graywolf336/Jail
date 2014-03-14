package com.graywolf336.jail.legacy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.enums.Settings;

/**
 * Manages the old data from Jail 2.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class LegacyManager {
	private JailMain pl;
	private YamlConfiguration global;
	
	public LegacyManager(JailMain plugin) {
		this.pl = plugin;
	}
	
	/** Returns true/false if the old config, global.yml, exists and needs to be converted. */
	public boolean doWeNeedToConvert() {
		File f = new File(pl.getDataFolder(), "global.yml");
		
		return f.exists();
	}
	
	public boolean loadOldData() {
		File f = new File(pl.getDataFolder(), "global.yml");
		
		if(f.exists()) {
			global = new YamlConfiguration();
			try {
				global.load(f);
			} catch (FileNotFoundException e) {
				//e.printStackTrace();
				pl.getLogger().severe("Unable to load the old global config, file not found.");
			} catch (IOException e) {
				//e.printStackTrace();
				pl.getLogger().severe("Unable to load the old global config: " + e.getMessage());
			} catch (InvalidConfigurationException e) {
				//e.printStackTrace();
				pl.getLogger().severe("Unable to load the old global config: " + e.getMessage());
			}finally {
				
			}
		}else {
			pl.debug("The old config file, global.yml, was not found so not laoding anything.");
			return false;
		}
		
		try {
			loadOldConfig();
			return true;
		}catch (Exception e) {
			pl.getLogger().severe("Failed to load the old configuration for some reason.");
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	private void loadOldConfig() {
		FileConfiguration c = pl.getConfig();
		
		int count = 0;
		for(OldSetting s : OldSetting.values()) {
			switch(s) {
				case Debug:
					if(global.contains(s.getString())) {
						c.set(Settings.DEBUG.getPath(), OldSettings.getGlobalBoolean(global, s));
						//Only set it true if the old config had true, this way we can still hold the debuggin over
						//if the old config had it set to false but the new one has it set to true (until a restart/reload)
						if(c.getBoolean(Settings.DEBUG.getPath())) pl.setDebugging(true);
						pl.debug(Settings.DEBUG.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case BroadcastJailMessage:
					if(global.contains(s.getString())) {
						c.set(Settings.BROADCASTJAILING.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.BROADCASTJAILING.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case AllowUpdateNotifications:
					if(global.contains(s.getString())) {
						c.set(Settings.UPDATENOTIFICATIONS.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.UPDATENOTIFICATIONS.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case ExecutedCommandsOnJail:
					if(global.contains(s.getString())) {
						c.set(Settings.COMMANDSONJAIL.getPath(), OldSettings.getGlobalList(global, s));
						pl.debug(Settings.COMMANDSONJAIL.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case ExecutedCommandsOnRelease:
					if(global.contains(s.getString())) {
						c.set(Settings.COMMANDSONRELEASE.getPath(), OldSettings.getGlobalList(global, s));
						pl.debug(Settings.COMMANDSONRELEASE.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case AutomaticMute:
					if(global.contains(s.getString())) {
						c.set(Settings.AUTOMATICMUTE.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.AUTOMATICMUTE.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case StoreInventory:
					if(global.contains(s.getString())) {
						c.set(Settings.JAILEDSTOREINVENTORY.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.JAILEDSTOREINVENTORY.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case CanPrisonerOpenHisChest:
					if(global.contains(s.getString())) {
						c.set(Settings.PRISONEROPENCHEST.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.PRISONEROPENCHEST.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case LogJailingIntoConsole:
					if(global.contains(s.getString())) {
						c.set(Settings.LOGJAILINGTOCONSOLE.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.LOGJAILINGTOCONSOLE.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case CountdownTimeWhenOffline:
					if(global.contains(s.getString())) {
						c.set(Settings.COUNTDOWNTIMEOFFLINE.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.COUNTDOWNTIMEOFFLINE.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case ReleaseBackToPreviousPosition:
					if(global.contains(s.getString())) {
						c.set(Settings.RELEASETOPREVIOUSPOSITION.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.RELEASETOPREVIOUSPOSITION.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case IgnorePrisonersSleepingState:
					if(global.contains(s.getString())) {
						c.set(Settings.IGNORESLEEPINGSTATE.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.IGNORESLEEPINGSTATE.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case TeleportPrisonerOnRelease:
					if(global.contains(s.getString())) {
						c.set(Settings.TELEPORTONRELEASE.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.TELEPORTONRELEASE.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case DefaultJailTime:
					if(global.contains(s.getString())) {
						c.set(Settings.JAILDEFAULTTIME.getPath(), OldSettings.getGlobalInt(global, s));
						pl.debug(Settings.JAILDEFAULTTIME.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case UseBukkitSchedulerTimer:
					if(global.contains(s.getString())) {
						c.set(Settings.USEBUKKITTIMER.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.USEBUKKITTIMER.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case EnableJailStick:
					if(global.contains(s.getString())) {
						c.set(Settings.JAILSTICKENABLED.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.JAILSTICKENABLED.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case JailStickParameters:
					if(global.contains(s.getString())) {
						LinkedList<String> sticks = new LinkedList<String>();
						for (String i : OldSettings.getGlobalString(global, s).split(";")) {
							String[] info = i.split(",");
							//item id,range,time,jail name,reason
							Material m = Material.getMaterial(Integer.valueOf(info[0]).intValue());
							
							//item name,time,jail name,reason
							sticks.push(m.toString().toLowerCase() + "," + info[2] + "," + info[3] + "," + info[4]);
						}
						c.set(Settings.JAILSTICKSTICKS.getPath(), sticks);
						pl.debug(Settings.JAILSTICKSTICKS.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case EnableBlockDestroyProtection:
					if(global.contains(s.getString())) {
						c.set(Settings.BLOCKBREAKPROTECTION.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.BLOCKBREAKPROTECTION.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case BlockDestroyPenalty:
					if(global.contains(s.getString())) {
						c.set(Settings.BLOCKBREAKPENALTY.getPath(), OldSettings.getGlobalInt(global, s));
						pl.debug(Settings.BLOCKBREAKPENALTY.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case BlockDestroyProtectionExceptions:
					if(global.contains(s.getString())) {
						@SuppressWarnings("unchecked")
						List<String> exceptions = (List<String>) OldSettings.getGlobalList(global, s);
						LinkedList<String> whitelist = new LinkedList<String>();
						for(String e : exceptions) {
							whitelist.add(Material.getMaterial(Integer.valueOf(e).intValue()).toString().toLowerCase());
						}
						
						c.set(Settings.BLOCKBREAKWHITELIST.getPath(), whitelist);
						pl.debug(Settings.BLOCKBREAKWHITELIST.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case EnableBlockPlaceProtection:
					if(global.contains(s.getString())) {
						c.set(Settings.BLOCKPLACEPROTECTION.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.BLOCKPLACEPROTECTION.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case BlockPlacePenalty:
					if(global.contains(s.getString())) {
						c.set(Settings.BLOCKPLACEPENALTY.getPath(), OldSettings.getGlobalInt(global, s));
						pl.debug(Settings.BLOCKPLACEPENALTY.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case BlockPlaceProtectionExceptions:
					if(global.contains(s.getString())) {
						@SuppressWarnings("unchecked")
						List<String> exceptions = (List<String>) OldSettings.getGlobalList(global, s);
						LinkedList<String> whitelist = new LinkedList<String>();
						for(String e : exceptions) {
							whitelist.add(Material.getMaterial(Integer.valueOf(e).intValue()).toString().toLowerCase());
						}
						
						c.set(Settings.BLOCKPLACEWHITELIST.getPath(), whitelist);
						pl.debug(Settings.BLOCKPLACEWHITELIST.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case EnablePlayerMoveProtection:
					if(global.contains(s.getString())) {
						c.set(Settings.MOVEPROTECTION.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.MOVEPROTECTION.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case PlayerMoveProtectionPenalty:
					if(global.contains(s.getString())) {
						c.set(Settings.MOVEPENALTY.getPath(), OldSettings.getGlobalInt(global, s));
						pl.debug(Settings.MOVEPENALTY.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case WhitelistedCommands:
					if(global.contains(s.getString())) {
						c.set(Settings.COMMANDWHITELIST.getPath(), OldSettings.getGlobalList(global, s));
						pl.debug(Settings.COMMANDWHITELIST.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case CommandProtectionPenalty:
					if(global.contains(s.getString())) {
						c.set(Settings.COMMANDPENALTY.getPath(), OldSettings.getGlobalInt(global, s));
						pl.debug(Settings.COMMANDPENALTY.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case InteractionPenalty:
					if(global.contains(s.getString())) {
						c.set(Settings.PREVENTINTERACTIONITEMSPENALTY.getPath(), OldSettings.getGlobalInt(global, s));
						pl.debug(Settings.PREVENTINTERACTIONITEMSPENALTY.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case EnableExplosionProtection:
					if(global.contains(s.getString())) {
						c.set(Settings.EXPLOSIONPROTECTION.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.EXPLOSIONPROTECTION.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case EnableFoodControl:
					if(global.contains(s.getString())) {
						c.set(Settings.FOODCONTROL.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.FOODCONTROL.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case FoodControlMinimumFood:
					if(global.contains(s.getString())) {
						c.set(Settings.FOODCONTROLMIN.getPath(), OldSettings.getGlobalInt(global, s));
						pl.debug(Settings.FOODCONTROLMIN.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case FoodControlMaximumFood:
					if(global.contains(s.getString())) {
						c.set(Settings.FOODCONTROLMAX.getPath(), OldSettings.getGlobalInt(global, s));
						pl.debug(Settings.FOODCONTROLMAX.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case PrisonersRecieveMessages:
					if(global.contains(s.getString())) {
						c.set(Settings.RECIEVEMESSAGES.getPath(), OldSettings.getGlobalBoolean(global, s));
						pl.debug(Settings.RECIEVEMESSAGES.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case UseMySQL:
					if(global.contains(s.getString())) {
						c.set("storage.type", OldSettings.getGlobalBoolean(global, s) ? "mysql" : "sqlite");
						pl.debug(Settings.RECIEVEMESSAGES.getPath() + " <-- " + s.getString());
						count++;
					}
					break;
				case MySQLConn:
					if(global.contains(s.getString())) {
						//jdbc:mysql://localhost:3306/minecraft
						String con = OldSettings.getGlobalString(global, s);
						String a = con.split("//")[1];
						//localhost 3306/minecraft
						String[] b1 = a.split(":");
						//3306 minecraft
						String[] b2 = b1[1].split("/");
						
						c.set("storage.mysql.host", b1[0]);
						c.set("storage.mysql.port", b2[0]);
						c.set("storage.mysql.database", b2[1]);
						
						pl.debug("storage.mysql <-- " + s.getString());
						count++;
					}
					break;
				case MySQLUsername:
					if(global.contains(s.getString())) {
						c.set("storage.mysql.username", OldSettings.getGlobalString(global, s));
						pl.debug("storage.mysql.username <-- " + s.getString());
						count++;
					}
					break;
				case MySQLPassword:
					if(global.contains(s.getString())) {
						c.set("storage.mysql.password", OldSettings.getGlobalString(global, s));
						pl.debug("storage.mysql.password <-- " + s.getString());
						count++;
					}
					break;
				default:
					break;
			}
		}
		
		pl.saveConfig();
		pl.getLogger().info("Converted " + count + " old config value" + (count == 1 ? "" : "s") + ".");
	}
}
