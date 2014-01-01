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
		pattern = "handcuff|hc",
		permission = "jail.command.handcuff",
		usage = "/handcuff [player]"
	)
public class HandCuffCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		Player player = jm.getPlugin().getServer().getPlayer(args[0]);
		
		if(player == null) {
			sender.sendMessage(ChatColor.RED + "That player is not online!");
		}else if(player.hasPermission("jail.cantbehandcuffed")) {
			sender.sendMessage(ChatColor.RED + "That player can't be handcuffed.");
		}else if(jm.isPlayerJailed(player.getName())) {
			sender.sendMessage(ChatColor.RED + "That player is currently jailed, you can't handcuff a prisoner.");
		}else if(jm.getPlugin().getHandCuffManager().isHandCuffed(player.getName())) {
			sender.sendMessage(ChatColor.GREEN + "That player is already handcuffed, releasing them now!");
			jm.getPlugin().getHandCuffManager().removeHandCuffs(player.getName());
			player.sendMessage(ChatColor.GREEN + "Your handcuffs have been rmeoved.");
		}else {
			jm.getPlugin().getHandCuffManager().addHandCuffs(player.getName(), player.getLocation());
			sender.sendMessage(ChatColor.BLUE + args[0] + ChatColor.GREEN + " has been handcuffed!");
			player.sendMessage(ChatColor.RED + "You've been handcuffed.");
		}
		
		return true;
	}
}
