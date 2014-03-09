package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

@CommandInfo(
		maxArgs = 0,
		minimumArgs = 0,
		needsPlayer = true,
		pattern = "stick",
		permission = "jail.usercmd.jailstick",
		usage = "/jail stick"
	)
public class JailStickCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
		boolean using = jm.getPlugin().getJailStickManager().toggleUsingStick(sender.getName());
		
		if(using) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.JAILSTICKENABLED));
		}else {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.JAILSTICKDISABLED));
		}
		
		return true;
	}
}
