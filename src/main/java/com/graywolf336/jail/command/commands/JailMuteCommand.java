package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.LangString;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "jailmute|jmute",
		permission = "jail.command.jailmute",
		usage = "/jailmute <player>"
	)
public class JailMuteCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
		//Let's check if the player they're sending us is jailed
		if(jm.isPlayerJailed(args[0])) {
			//They are, so let's toggle whether they are muted or not
			boolean muted = !jm.getPrisoner(args[0]).isMuted();
			jm.getPrisoner(args[0]).setMuted(muted);
			
			//Send the message to the sender based upon whether they are muted or unmuted
			if(muted)
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOWMUTED, args[0]));
			else
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOWUNMUTED, args[0]));
		}else {
			//The player provided is not jailed, so let's tell the sender that
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOTJAILED, args[0]));
		}
		
		return true;
	}
}
