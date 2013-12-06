package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = -1,
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "jail|j",
		permission = "jail.command.jail",
		usage = "/jail [name] (time) (j:Jail name) (c:Cell name) (r:Reason) (m:Muted)"
	)
public class JailCommand implements Command {

	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		return true; //If they made it this far, the command is intact and ready to be processed. :)
	}

}
