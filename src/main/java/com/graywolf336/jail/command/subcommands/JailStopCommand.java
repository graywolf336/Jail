package com.graywolf336.jail.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = 0,
		minimumArgs = 0,
		needsPlayer = true,
		pattern = "stop|s",
		permission = "jail.command.jailstop",
		usage = "/jail stop"
	)
public class JailStopCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		if(jm.isCreatingACell(sender.getName())) {
			jm.removeCellCreationPlayer(sender.getName());
			sender.sendMessage(ChatColor.RED + "You have stopped creating cells.");
		}
		
		if(jm.isCreatingAJail(sender.getName())) {
			jm.removeJailCreationPlayer(sender.getName());
			sender.sendMessage(ChatColor.RED + "You have stopped creating a jail.");
		}
		
		return true;
	}
}