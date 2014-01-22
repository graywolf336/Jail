package com.graywolf336.jail.command.commands;

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
		pattern = "jailremovecell|jrcell",
		permission = "jail.command.jailremovecell",
		usage = "/jailremovecell [Jail Name] (Cell Name)"
	)
public class JailRemoveCellCommand implements Command{

	// Remove the specified Cell from the Specified Jail
	
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		Jail j = jm.getJail(args[0]);
		
		if(j != null) {
			if(!j.getCells().isEmpty()) {
				if(j.getCell(args[1]) != null) {
					//remove it!
					j.removeCell(args[1]);
					sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.CELLREMOVED,
							new String[] { args[1], args[0] }));
				}else {
					//No cell by that name
					sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOCELL,
							new String[] { args[1], args[0] }));
				}
			}else {
				//No cells in this jail
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOCELLS, args[0]));
			}
		}else {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAIL, args[0]));
		}
		
		return true;
	}

}
