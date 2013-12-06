package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = 0,
		minimumArgs = 0,
		needsPlayer = true,
		pattern = "jailstop",
		permission = "jail.command.jailstop",
		usage = "/jailstop"
	)
public class JailStopCommand implements Command {

	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		jm.removeJailCreationPlayer(sender.getName());
		jm.removeCellCreationPlayer(sender.getName());
		
		sender.sendMessage("Any creations, jail or cell, have been stopped.");
		return true;
	}
	
}