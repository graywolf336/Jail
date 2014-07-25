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
		pattern = "clear",
		permission = "jail.command.jailclear",
		usage = "/jail clear (Jail name)"
	)
public class JailClearCommand implements Command {
	
	// If Jail is specified unjails all the prisoners from that Jail (new feature) otherwise it unjails all the prisoners from all the jails
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		if(jm.isConfirming(sender.getName())) {
			sender.sendMessage(Lang.ALREADY.get());
		}else {
			jm.addConfirming(sender.getName(), new ConfirmPlayer(sender.getName(), args, Confirmation.CLEAR));
			sender.sendMessage(Lang.START.get());
		}
		
		return true;
	}
}
