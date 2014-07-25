package com.graywolf336.jail.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
		maxArgs = 0,
		minimumArgs = 0,
		needsPlayer = false,
		pattern = "reload|r",
		permission = "jail.command.jailreload",
		usage = "/jail reload"
	)
public class JailReloadCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		try {
			jm.getPlugin().reloadConfig();
			jm.getPlugin().getJailIO().loadLanguage();
			jm.getPlugin().getJailIO().loadJails();
			jm.getPlugin().reloadScoreBoardManager();
			jm.getPlugin().reloadJailSticks();
			jm.getPlugin().reloadJailPayManager();
			jm.getPlugin().reloadUpdateCheck();
			
			sender.sendMessage(Lang.PLUGINRELOADED.get());
		}catch (Exception e) {
			sender.sendMessage(ChatColor.RED + e.getMessage());
		}
		
		return true;
	}
}
