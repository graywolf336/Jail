package com.graywolf336.jail.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.enums.Lang;

@CommandInfo(
		maxArgs = 0,
		minimumArgs = 0,
		needsPlayer = true,
		pattern = "status|s",
		permission = "jail.usercmd.jailstatus",
		usage = "/jail status"
	)
public class JailStatusCommand implements Command{
	
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		Player pl = (Player) sender;
		
		if(jm.isPlayerJailed(pl.getUniqueId())) {
			Prisoner p = jm.getPrisoner(pl.getUniqueId());
			//They are jailed, so let's tell them some information
			sender.sendMessage(Lang.STATUS.get(new String[] { p.getReason(), p.getJailer(), String.valueOf(p.getRemainingTimeInMinutes()) }));
		}else {
			//the sender of the command is not jailed, tell them that
			sender.sendMessage(Lang.YOUARENOTJAILED.get());
		}
		
		return true;
	}

}
