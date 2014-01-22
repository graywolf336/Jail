package com.graywolf336.jail.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

@CommandInfo(
		maxArgs = 0,
		minimumArgs = 0,
		needsPlayer = false,
		pattern = "jaillist|jl",
		permission = "jail.command.jaillist",
		usage = "/jaillist"
	)
public class JailListCommand implements Command {

	@Override
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		sender.sendMessage(ChatColor.AQUA + "----------Jails----------");
		
		if(!jm.getJails().isEmpty()) {
			for(Jail j : jm.getJails()) {
				sender.sendMessage(ChatColor.BLUE + "    " + j.getName() + " (" + j.getAllPrisoners().size() + ")");
			}
		}else {
			sender.sendMessage(" " + jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAILS));
		}
		
		sender.sendMessage(ChatColor.AQUA + "-------------------------");
		return true;
	}
}
