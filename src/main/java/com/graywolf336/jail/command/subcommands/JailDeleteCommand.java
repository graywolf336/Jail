package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "delete|d",
		permission = "jail.command.jaildelete",
		usage = "/jail delete <jail>"
	)
public class JailDeleteCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
		//Check if the jail name provided is a valid jail
		if(jm.isValidJail(args[1])) {
			//check if the jail doesn't contain prisoners
			if(jm.getJail(args[1]).getAllPrisoners().size() == 0) {
				//There are no prisoners, so we can delete it
				jm.removeJail(args[1]);
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.JAILREMOVED, args[1]));
			}else {
				//The jail has prisoners, they need to release them first
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.JAILREMOVALUNSUCCESSFUL, args[1]));
			}
		}else {
			//No jail found by the provided name
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAIL, args[1]));
		}
		
		return true;
	}
}
