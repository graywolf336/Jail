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
		pattern = "removecell|rcell|rc",
		permission = "jail.command.jailremovecell",
		usage = "/jail removecell [Jail Name] (Cell Name)"
	)
public class JailRemoveCellCommand implements Command{

	// Remove the specified Cell from the Specified Jail
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		Jail j = jm.getJail(args[1]);
		
		if(j != null) {
			if(!j.getCells().isEmpty()) {
				if(j.getCell(args[2]) != null) {
					//remove it!
					j.removeCell(args[2]);
					sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.CELLREMOVED,
							new String[] { args[2], args[1] }));
				}else {
					//No cell by that name
					sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOCELL,
							new String[] { args[2], args[1] }));
				}
			}else {
				//No cells in this jail
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOCELLS, args[1]));
			}
		}else {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAIL, args[1]));
		}
		
		return true;
	}
}
