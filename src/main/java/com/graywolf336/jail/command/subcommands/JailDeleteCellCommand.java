package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

@CommandInfo(
		maxArgs = 2,
		minimumArgs = 2,
		needsPlayer = false,
		pattern = "deletecell|dc",
		permission = "jail.command.jaildeletecell",
		usage = "/jail deletecell <jail> <cell>"
	)
public class JailDeleteCellCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
		//Check if the jail name provided is a valid jail
		if(jm.isValidJail(args[1])) {
			Jail j = jm.getJail(args[1]);
			
			//check if the cell is a valid cell
			if(j.isValidCell(args[2])) {
				if(j.getCell(args[2]).hasPrisoner()) {
					//The cell has a prisoner, so tell them to first transfer the prisoner
					//or release the prisoner
					sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.CELLREMOVALUNSUCCESSFUL, new String[] { args[2], args[1] }));
				}else {
					j.removeCell(args[2]);
					sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.CELLREMOVED, new String[] { args[2], args[1] }));
				}
			}else {
				//No cell found by the provided name in the stated jail
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOCELL, new String[] { args[2], args[1] }));
			}
		}else {
			//No jail found by the provided name
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAIL, args[1]));
		}
		
		return true;
	}
}
