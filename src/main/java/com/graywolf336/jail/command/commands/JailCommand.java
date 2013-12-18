package com.graywolf336.jail.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.command.parameters.JailParameters;

@CommandInfo(
		maxArgs = -1,
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "jail|j",
		permission = "jail.command.jail",
		usage = "/jail [-p name] (-t time) (-j JailName) (-c CellName) (-m Muted) (-r A reason for jailing)"
	)
public class JailCommand implements Command {

	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		JailParameters params = new JailParameters();
		
		try {
			new JCommander(params, args);
		}catch(ParameterException e) {
			return false;
		}
		
		
		Player p = jm.getPlugin().getServer().getPlayer(params.player());
		
		//Player is not online
		if(p == null) {
			sender.sendMessage(params.player() + " is offline and will be jailed for " + params.time() + " minutes in the jail " + params.jail() + "in the cell " + params.cell() + " and will be muted: " + params.muted() + ".");
		}else {
			//Player *is* online
			sender.sendMessage(params.player() + " is online and will be jailed for " + params.time() + " minutes in the jail " + params.jail() + "in the cell " + params.cell() + " and will be muted: " + params.muted() + ".");
		}
		
		return true;
	}
}
