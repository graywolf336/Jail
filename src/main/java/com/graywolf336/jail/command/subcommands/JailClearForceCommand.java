package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 0,
		needsPlayer = false,
		pattern = "clearforce|cf",
		permission = "jail.command.jailclearforce",
		usage = "/jail clearforce (Jail name)"
	)
public class JailClearForceCommand implements Command {
	
	// If Jail is specified clear all prisoners from that Jail (new feature) else, clear all players from all jails
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		if(args.length == 2) {
			Jail j = jm.getJail(args[1]);
			
			if(j != null) {
				j.clearPrisoners();
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PRISONERSCLEARED, j.getName()));
			}else {
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAIL, args[1]));
			}
		}else {
			if(jm.getJails().size() == 0) {
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAILS));
			}else {
				for(Jail j : jm.getJails()) {
					j.clearPrisoners();
				}
				
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PRISONERSCLEARED,
						jm.getPlugin().getJailIO().getLanguageString(LangString.ALLJAILS)));
			}
		}
		
		return true; //If they made it this far, the command is intact and ready to be processed. :)
	}
	
}
