package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.ConfirmPlayer;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Confirmation;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
		maxArgs = 1,
		minimumArgs = 0,
		needsPlayer = false,
		pattern = "clearforce|cf",
		permission = "jail.command.jailclearforce",
		usage = "/jail clearforce (Jail name)"
	)
public class JailClearForceCommand implements Command {
	
	// If Jail is specified clear all prisoners from that Jail (new feature) else, clear all players from all jails
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		if(jm.isConfirming(sender.getName())) {
			sender.sendMessage(Lang.ALREADY.get());
		}else {
			jm.addConfirming(sender.getName(), new ConfirmPlayer(sender.getName(), args, Confirmation.CLEARFORCE));
			sender.sendMessage(Lang.START.get());
		}
		
		return true; //If they made it this far, the command is intact and ready to be processed. :)
	}
	
}
