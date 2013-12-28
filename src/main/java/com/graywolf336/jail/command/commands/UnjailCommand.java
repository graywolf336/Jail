package com.graywolf336.jail.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "unjail|uj",
		permission = "jail.command.unjail",
		usage = "/unjail [player]"
	)
public class UnjailCommand implements Command {
	
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		if(jm.isPlayerJailed(args[0])) {
			Player p = jm.getPlugin().getServer().getPlayerExact(args[0]);
			if(p == null) {
				sender.sendMessage(ChatColor.RED + "Offline unjailing is not implemented yet.");
			}else {
				Jail j = jm.getJailPlayerIsIn(args[0]);
				try {
					jm.getPlugin().getPrisonerManager().unJail(j, j.getCellPrisonerIsIn(args[0]), p, j.getPrisoner(args[0]));
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + e.getMessage());
				}
			}
		}else {
			sender.sendMessage(ChatColor.RED + "That player is not jailed.");
		}
		
		return true;
	}
}
