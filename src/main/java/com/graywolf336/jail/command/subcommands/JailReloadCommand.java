package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

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
		jm.getPlugin().reloadConfig();
		jm.getPlugin().getJailIO().loadLanguage();
		jm.getPlugin().getJailIO().loadJails();
		jm.getPlugin().reloadScoreBoardManager();
		jm.getPlugin().reloadJailSticks();
		
		sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PLUGINRELOADED));
		return true;
	}
}
