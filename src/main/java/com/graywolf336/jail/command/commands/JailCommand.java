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
		String player = "", jail = "", cell = "", reason = "";
		int time = 30;
		boolean muted = false;
		
		for(String s : args) {
			if(s.startsWith("p:")) {
				player = s.substring(2);
			}else if(s.startsWith("t:")) {
				Integer.parseInt(s.substring(2));
			}else if(s.startsWith("j:")) {
				jail = s.substring(2);
			}else if(s.startsWith("c:")) {
				cell = s.substring(2);
			}else if(s.startsWith("m:")) {
				muted = Boolean.parseBoolean(s.substring(2));
			}else if(s.startsWith("r:")) {
				reason = s.substring(2);
			}
		}
		
		Player p = jm.getPlugin().getServer().getPlayer(player);
		
		//Player is not online
		if(p == null) {
			sender.sendMessage(player + " is offline.");
		}else {
			//Player *is* online
			sender.sendMessage(player + " is online.");
		}
		
		return true;
	}
}
