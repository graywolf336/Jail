package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.ConfirmPlayer;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Confirmation;
import com.graywolf336.jail.enums.LangString;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "delete|d",
		permission = "jail.command.jaildelete",
		usage = "/jail delete <jail>"
	)
public class JailDeleteCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
		if(jm.isConfirming(sender.getName())) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.ALREADY));
		}else {
			jm.addConfirming(sender.getName(), new ConfirmPlayer(sender.getName(), args, Confirmation.DELETE));
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.START));
		}
		
		return true;
	}
}
