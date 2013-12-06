package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 0,
		needsPlayer = true,
		pattern = "jailcheck|jcheck",
		permission = "jail.command.jailcheck",
		usage = "/jailcheck (Player name)"
	)
public class JailCheckCommand implements Command{

	// Checks the status of the specified prisoner, if no args, will display all players currently jailed
	
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		return true; //If they made it this far, the command is intact and ready to be processed. :)
	}

}
