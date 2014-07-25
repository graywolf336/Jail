package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;

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
			sender.sendMessage(Lang.PLAYERNOTONLINE.get());
		}else if(player.hasPermission("jail.cantbehandcuffed")) {
			sender.sendMessage(Lang.CANTBEHANDCUFFED.get(player.getName()));
		}else if(jm.isPlayerJailed(player.getUniqueId())) {
			sender.sendMessage(Lang.CURRENTLYJAILEDHANDCUFF.get(player.getName()));
		}else if(jm.getPlugin().getHandCuffManager().isHandCuffed(player.getUniqueId())) {
			sender.sendMessage(Lang.HANDCUFFSRELEASED.get(player.getName()));
			jm.getPlugin().getHandCuffManager().removeHandCuffs(player.getUniqueId());
			player.sendMessage(Lang.UNHANDCUFFED.get());
		}else {
			jm.getPlugin().getHandCuffManager().addHandCuffs(player.getUniqueId(), player.getLocation());
			sender.sendMessage(Lang.HANDCUFFSON.get(player.getName()));
			player.sendMessage(Lang.HANDCUFFED.get());
		}
		
		return true;
	}
}
