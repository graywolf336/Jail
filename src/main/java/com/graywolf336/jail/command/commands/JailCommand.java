package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = -1,
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "jail|j",
		permission = "jail.command.jail",
		usage = "/jail [p:name] (t:time) (j:Jail name) (c:Cell name) (r:Reason) (m:Muted)"
	)
public class JailCommand implements Command {

	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		Player p = jm.getPlugin().getServer().getPlayer(args[0]);
		
		//Player is not online
		if(p == null) {
			sender.sendMessage(args[0] + " is offline.");
		}else {
			//Player *is* online
			sender.sendMessage(args[0] + " is online.");
		}
		
		return true;
	}
}
