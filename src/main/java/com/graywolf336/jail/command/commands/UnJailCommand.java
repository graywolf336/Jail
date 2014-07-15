package com.graywolf336.jail.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;
import com.graywolf336.jail.enums.Settings;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "unjail|uj",
		permission = "jail.command.unjail",
		usage = "/unjail [player]"
	)
public class UnJailCommand implements Command {
	
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		//Check if the player is jailed
		if(jm.isPlayerJailedByLastKnownUsername(args[0])) {
			Jail j = jm.getJailPlayerIsInByLastKnownName(args[0]);
			Prisoner pris = j.getPrisonerByLastKnownName(args[0]);
			Player p = jm.getPlugin().getServer().getPlayer(pris.getUUID());
			
			//Check if the player is on the server or not
			if(p == null) {
				//Check if the player has offline pending and their remaining time is above 0, if so then
				//forceably unjail them
				if(pris.isOfflinePending() && pris.getRemainingTime() != 0L) {
					jm.getPlugin().getPrisonerManager().forceUnJail(j, j.getCellPrisonerIsIn(pris.getUUID()), p, pris, sender);
				}else {
					//The player is not, so we'll set the remaining time to zero and do it when they login next
					pris.setRemainingTime(0L);
					pris.setOfflinePending(true);
					sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.WILLBEUNJAILED, args[0]));
				}
			}else {
				//Player is online, so let's try unjailing them
				try {
					jm.getPlugin().getPrisonerManager().unJail(j, j.getCellPrisonerIsIn(pris.getUUID()), p, pris, sender);
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + e.getMessage());
				}
			}
			
			if(jm.getPlugin().getConfig().getBoolean(Settings.LOGJAILINGTOCONSOLE.getPath())) {
				jm.getPlugin().getLogger().info(ChatColor.stripColor(jm.getPlugin().getJailIO().getLanguageString(LangString.BROADCASTUNJAILING, new String[] { args[0], sender.getName() })));
			}
		}else {
			//The player is not currently jailed
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOTJAILED, args[0]));
		}
		
		return true;
	}
}
