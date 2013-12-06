package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = 2,
		minimumArgs = 1,
		needsPlayer = true,
		pattern = "jailremovecell|jrcell",
		permission = "jail.command.jailremovecell",
		usage = "/jailremovecell [Jail Name] (Cell Name)"
	)
public class JailRemoveCellCommand implements Command{

	// Remove the specified Cell from the Specified Jail, if no cell specified will delete nearest cell
	
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		return true; //If they made it this far, the command is intact and ready to be processed. :)
	}

}
