package com.graywolf336.jail.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "jaillistcells|jcc",
		permission = "jail.command.jaillistcell",
		usage = "/jaillistcells <jail>"
	)
public class JailListCellsCommand implements Command {
	@Override
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		sender.sendMessage(ChatColor.AQUA + "----------Cells----------");
		
		if(!jm.getJails().isEmpty()) {
			if(jm.getJail(args[0]) != null) {
				Jail j = jm.getJail(args[0]);
				
				String message = "";
				for(Cell c : j.getCells()) {
					if(message.isEmpty()) {
						message = c.getName() + (c.getPrisoner() == null ? "" : "(" + c.getPrisoner().getName() + ")");
					}else {
						message += ", " + c.getName() + (c.getPrisoner() == null ? "" : "(" + c.getPrisoner().getName() + ")");
					}
				}
				
				if(message.isEmpty()) {
					sender.sendMessage(ChatColor.RED + "There are no cells for the jail '" + args[0] + "'.");
				}else {
					sender.sendMessage(ChatColor.GREEN + message);
				}
			}else {
				sender.sendMessage(ChatColor.RED + "No jail by the name of '" + args[0] + "'.");
			}
		}else {
			sender.sendMessage(ChatColor.RED + "  There are no jails.");
		}
		
		sender.sendMessage(ChatColor.AQUA + "-------------------------");
		return true;
	}
}
