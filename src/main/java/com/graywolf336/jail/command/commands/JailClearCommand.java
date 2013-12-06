package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 0,
		needsPlayer = false,
		pattern = "jailclear|jclear",
		permission = "jail.command.jailclear",
		usage = "/jailclear (Jail name)"
	)
public class JailClearCommand implements Command {
	
	// If Jail is specified clear all prisoners from that Jail (new feature) else, clear all players from all jails
	
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		return true; //If they made it this far, the command is intact and ready to be processed. :)
	}
	
}
