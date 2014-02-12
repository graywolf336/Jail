package com.graywolf336.jail.command.subcommands;

import java.util.HashSet;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

@CommandInfo(
		maxArgs = 2,
		minimumArgs = 2,
		needsPlayer = false,
		pattern = "transferall|transall",
		permission = "jail.command.jailtransferall",
		usage = "/jail transferall oldjail targetjail"
	)
public class JailTransferAllCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
		if(jm.getJails().isEmpty()) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAILS));
			return true;
		}
		
		jm.getPlugin().debug("Starting to transfer everyone from '" + args[1] + "' into '" + args[2] + "'.");
		
		//Check if the oldjail is not a valid jail
		if(!jm.isValidJail(args[1])) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAIL, args[1]));
			return true;
		}else if(!jm.isValidJail(args[2])) {
			//Check if the targetjail is a valid jail as well
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAIL, args[2]));
			return true;
		}
		
		jm.getPlugin().debug("Sending the transferring off, jail checks all came clean.");
		
		Jail old = jm.getJail(args[1]);
		HashSet<Prisoner> oldPrisoners = new HashSet<Prisoner>(old.getAllPrisoners());
		
		//Transfer all the prisoners
		for(Prisoner p : oldPrisoners) {
			jm.getPlugin().getPrisonerManager().transferPrisoner(old, old.getCellPrisonerIsIn(p.getName()), jm.getJail(args[2]), null, p);
		}
		
		//Send the messages to the sender when completed
		sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.TRANSFERALLCOMPLETE, new String[] { old.getName(), args[2] }));
		
		return true;
	}
}
