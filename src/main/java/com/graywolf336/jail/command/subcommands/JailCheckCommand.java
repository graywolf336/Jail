package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 0,
		needsPlayer = false,
		pattern = "check",
		permission = "jail.command.jailcheck",
		usage = "/jail check (Player name)"
	)
public class JailCheckCommand implements Command{

	// Checks the status of the specified prisoner, if no args, will display all players currently jailed
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		//No args means show everyone
		if(args.length == 1) {
			//TODO
		}else {
			//Otherwise let's check the first arg
			if(jm.isPlayerJailed(args[1])) {
				//TODO
			}else {
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOTJAILED, args[1]));
			}
		}
		
		return true;
	}

}
