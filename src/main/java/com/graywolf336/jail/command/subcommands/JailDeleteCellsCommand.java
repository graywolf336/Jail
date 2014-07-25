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
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "deletecells|dcs",
		permission = "jail.command.jaildeletecell",
		usage = "/jail deletecells [jail]"
	)
public class JailDeleteCellsCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
		if(jm.isConfirming(sender.getName())) {
			sender.sendMessage(Lang.ALREADY.get());
		}else {
			jm.addConfirming(sender.getName(), new ConfirmPlayer(sender.getName(), args, Confirmation.DELETECELLS));
			sender.sendMessage(Lang.START.get());
		}
		
		return true;
	}
}
