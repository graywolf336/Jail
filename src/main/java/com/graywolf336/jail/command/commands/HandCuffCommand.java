package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

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
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PLAYERNOTONLINE));
		}else if(player.hasPermission("jail.cantbehandcuffed")) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.CANTBEHANDCUFFED, new String[] { player.getName() }));
		}else if(jm.isPlayerJailed(player.getName())) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.CURRENTLYJAILEDHANDCUFF, new String[] { player.getName() }));
		}else if(jm.getPlugin().getHandCuffManager().isHandCuffed(player.getName())) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.HANDCUFFSRELEASED, new String[] { player.getName() }));
			jm.getPlugin().getHandCuffManager().removeHandCuffs(player.getName());
			player.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.UNHANDCUFFED));
		}else {
			jm.getPlugin().getHandCuffManager().addHandCuffs(player.getName(), player.getLocation());
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.HANDCUFFSON, new String[] { player.getName() }));
			player.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.HANDCUFFED));
		}
		
		return true;
	}
}
