package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 0,
		needsPlayer = false,
		pattern = "jailclear|jclear",
		permission = "jail.command.jailclear",
		usage = "/jailclear (Jail name)"
	)
public class JailClearCommand implements Command {
	
	// If Jail is specified unjails all the prisoners from that Jail (new feature) otherwise it unjails all the prisoners from all the jails
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		if(args.length == 1) {
			Jail j = jm.getJail(args[0]);
			
			if(j != null) {
				for(Prisoner p : j.getAllPrisoners()) {
					jm.getPlugin().getPrisonerManager().releasePrisoner(jm.getPlugin().getServer().getPlayerExact(p.getName()), p);
				}
				
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PRISONERSCLEARED, j.getName()));
			}else {
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAIL, args[0]));
			}
		}else {
			if(jm.getJails().size() == 0) {
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAILS));
			}else {
				for(Jail j : jm.getJails()) {
					for(Prisoner p : j.getAllPrisoners()) {
						jm.getPlugin().getPrisonerManager().releasePrisoner(jm.getPlugin().getServer().getPlayerExact(p.getName()), p);
					}
				}
				
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PRISONERSCLEARED,
						jm.getPlugin().getJailIO().getLanguageString(LangString.ALLJAILS)));
			}
		}
		
		return true;
	}
}
