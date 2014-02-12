package com.graywolf336.jail.command.subcommands;

import java.util.HashSet;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "deletecells|dcs",
		permission = "jail.command.jaildeletecell",
		usage = "/jail deletecells <jail>"
	)
public class JailDeleteCellsCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
		//Check if the jail name provided is a valid jail
		if(jm.isValidJail(args[1])) {
			Jail j = jm.getJail(args[1]);
			
			if(j.getCellCount() == 0) {
				//There are no cells in this jail, thus we can't delete them.
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOCELLS, args[1]));
			}else {
				//Keep a local copy of the hashset so that we don't get any CMEs.
				HashSet<Cell> cells = new HashSet<Cell>(j.getCells());
				
				for(Cell c : cells) {
					if(c.hasPrisoner()) {
						//The cell has a prisoner, so tell them to first transfer the prisoner
						//or release the prisoner
						sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.CELLREMOVALUNSUCCESSFUL, new String[] { c.getName(), args[1] }));
					}else {
						j.removeCell(c.getName());
						sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.CELLREMOVED, new String[] { args[2], args[1] }));
					}
				}
			}
		}else {
			//No jail found by the provided name
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAIL, args[1]));
		}
		
		return true;
	}
}
