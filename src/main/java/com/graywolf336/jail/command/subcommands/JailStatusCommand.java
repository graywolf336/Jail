package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

@CommandInfo(
		maxArgs = 0,
		minimumArgs = 0,
		needsPlayer = false,
		pattern = "status|time",
		permission = "jail.usercmd.jailstatus",
		usage = "/jail status"
	)
public class JailStatusCommand implements Command{
	
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		if(jm.isPlayerJailed(sender.getName())) {
			Prisoner p = jm.getPrisoner(sender.getName());
			//They are jailed, so let's tell them some information
			jm.getPlugin().getJailIO().getLanguageString(LangString.STATUS, new String[] { p.getReason(), p.getJailer(), String.valueOf(p.getRemainingTimeInMinutes()) });
		}else {
			//the sender of the command is not jailed, tell them that
			jm.getPlugin().getJailIO().getLanguageString(LangString.YOUARENOTJAILED);
		}
		
		return true;
	}

}
