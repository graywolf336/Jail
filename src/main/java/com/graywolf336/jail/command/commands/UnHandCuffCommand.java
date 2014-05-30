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
		pattern = "unhandcuff|uhc",
		permission = "jail.command.handcuff",
		usage = "/unhandcuff [player]"
	)
public class UnHandCuffCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		Player player = jm.getPlugin().getServer().getPlayerExact(args[0]);
		
		if(player == null) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PLAYERNOTONLINE));
		}else if(jm.getPlugin().getHandCuffManager().isHandCuffed(player.getUniqueId())) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.HANDCUFFSRELEASED, new String[] { player.getName() }));
			jm.getPlugin().getHandCuffManager().removeHandCuffs(player.getUniqueId());
			player.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.UNHANDCUFFED));
		}else {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOTHANDCUFFED, new String[] { player.getName() }));
		}
		
		return true;
	}
}
