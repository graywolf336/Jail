package com.graywolf336.jail.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "unhandcuff|uhc",
		permission = "jail.command.handcuff",
		usage = "/unhandcuff [player]"
	)
public class UnHandCuffCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		Player player = jm.getPlugin().getServer().getPlayerExact(args[0]);
		
		if(player == null) {
			sender.sendMessage(ChatColor.RED + "That player is not online!");
		}else if(jm.getPlugin().getHandCuffManager().isHandCuffed(player.getName())) {
			sender.sendMessage(ChatColor.GREEN + "Releasing them now!");
			jm.getPlugin().getHandCuffManager().removeHandCuffs(player.getName());
			player.sendMessage(ChatColor.GREEN + "Your handcuffs have been rmeoved.");
		}else {
			sender.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.RED + " doesn't have any handcuffs to remove!");
		}
		
		return true;
	}
}
