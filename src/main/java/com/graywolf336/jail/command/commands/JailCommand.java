package com.graywolf336.jail.command.commands;

import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Prisoner;
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
		
		if(jm.getJails().size() == 0) {
			sender.sendMessage(ChatColor.RED + "No jails found.");
			return true;
		}
		
		JailParameters params = new JailParameters();
		
		try {
			new JCommander(params, args);
		}catch(ParameterException e) {
			sender.sendMessage(ChatColor.RED + e.getMessage());
			return true;
		}
		
		Long time = 10L;
		
		try {
			Long.parseLong(params.time());
		}catch(NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "Number format is incorrect.");
			return true;
		}
		
		Player p = jm.getPlugin().getServer().getPlayer(params.player());
		Prisoner pris = new Prisoner(params.player(), params.muted(), TimeUnit.MILLISECONDS.convert(time, TimeUnit.MINUTES));
		
		//Player is not online
		if(p == null) {
			sender.sendMessage(pris.getName() + " is offline and will be jailed for " + pris.getRemainingTime() + " milliseconds in the jail " + params.jail() + "in the cell " + params.cell() + " and will be muted: " + pris.isMuted() + ".");
			sender.sendMessage(pris.getName() + " will be jailed for " + TimeUnit.MINUTES.convert(pris.getRemainingTime(), TimeUnit.MILLISECONDS) + " minutes.");
		}else {
			//Player *is* online
			sender.sendMessage(pris.getName() + " is online and will be jailed for " + pris.getRemainingTime() + " milliseconds in the jail " + params.jail() + "in the cell " + params.cell() + " and will be muted: " + pris.isMuted() + ".");
			sender.sendMessage(pris.getName() + " will be jailed for " + TimeUnit.MINUTES.convert(pris.getRemainingTime(), TimeUnit.MILLISECONDS) + " minutes.");
		}
		
		return true;
	}
}
