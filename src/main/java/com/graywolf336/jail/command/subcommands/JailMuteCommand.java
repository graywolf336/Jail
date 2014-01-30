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
		pattern = "mute|m",
		permission = "jail.command.jailmute",
		usage = "/jail mute <player>"
	)
public class JailMuteCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
		//Let's check if the player they're sending us is jailed
		if(jm.isPlayerJailed(args[1])) {
			//They are, so let's toggle whether they are muted or not
			boolean muted = !jm.getPrisoner(args[1]).isMuted();
			jm.getPrisoner(args[1]).setMuted(muted);
			
			//Send the message to the sender based upon whether they are muted or unmuted
			if(muted)
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOWMUTED, args[1]));
			else
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOWUNMUTED, args[1]));
		}else {
			//The player provided is not jailed, so let's tell the sender that
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOTJAILED, args[1]));
		}
		
		return true;
	}
}
