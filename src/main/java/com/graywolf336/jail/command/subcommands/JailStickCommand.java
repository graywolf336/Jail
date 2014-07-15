package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;
import com.graywolf336.jail.enums.Settings;

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
		if(jm.getPlugin().getConfig().getBoolean(Settings.JAILSTICKENABLED.getPath())) {
			boolean using = jm.getPlugin().getJailStickManager().toggleUsingStick(((Player) sender).getUniqueId());
			
			if(using) {
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.JAILSTICKENABLED));
			}else {
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.JAILSTICKDISABLED));
			}
		}else {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.JAILSTICKUSAGEDISABLED));
		}
		
		return true;
	}
}
