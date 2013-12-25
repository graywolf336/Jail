package com.graywolf336.jail.command.commands;

import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.command.parameters.JailParameters;
import com.graywolf336.jail.enums.LangString;
import com.graywolf336.jail.enums.Settings;
import com.graywolf336.jail.events.PrisonerJailedEvent;

@CommandInfo(
		maxArgs = -1,
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "jail|j",
		permission = "jail.command.jail",
		usage = "/jail [-p name] (-t time) (-j JailName) (-c CellName) (-m Muted) (-r A reason for jailing)"
	)
public class JailCommand implements Command {
	
	/*
	 * Executes the command. Checks the following:
	 * 
	 * - If there are any jails.
	 * - If the command can be parsed correctly.
	 * - If the specified jail is null (TODO: if no jail given then find one close to them)
	 * - If the given time can be parsed correctly, defaults to what is defined in the config
	 * - If the prisoner is online or not.
	 */
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
		
		//Check the jail params. If it is empty, let's get the default jail
		//from the config. If that is nearest, let's make a call to getting the nearest jail to
		//the sender but otherwise if it isn't nearest then let's set it to the default jail
		//which is defined in the config.
		if(params.jail().isEmpty()) {
			String dJail = jm.getPlugin().getConfig().getString(Settings.DEFAULTJAIL.getPath());
			
			if(dJail.equalsIgnoreCase("nearest")) {
				params.setJail(jm.getNearestJail(sender).getName());
			}else {
				params.setJail(jm.getPlugin().getConfig().getString(Settings.DEFAULTJAIL.getPath()));
			}
		}else if(jm.getJail(params.jail()) == null) {
			sender.sendMessage(ChatColor.RED + "No jail found by the name of '" + params.jail() + "'.");
			return true;
		}
		
		//Trying out the time.
		Long time = 10L;
		try {
			if(params.time().isEmpty()) {
				time = Util.getTime(jm.getPlugin().getConfig().getString(Settings.JAILDEFAULTTIME.getPath(), "30m"));
			}else {
				time = Util.getTime(params.time());
			}
		}catch(Exception e) {
			sender.sendMessage(ChatColor.RED + "Number format is incorrect.");
			return true;
		}
		
		Jail j = jm.getJail(params.jail());
		Prisoner pris = new Prisoner(params.player(), params.muted(), time);
		Player p = jm.getPlugin().getServer().getPlayer(params.player());
		
		//call the event
		PrisonerJailedEvent event = new PrisonerJailedEvent(j, pris, p, p == null, sender.getName());
		jm.getPlugin().getServer().getPluginManager().callEvent(event);
		
		//check if the event is cancelled
		if(event.isCancelled()) {
			if(event.getCancelledMessage().isEmpty())
				sender.sendMessage(ChatColor.RED + "Jailing " + params.player() + " was cancelled by another plugin and they didn't leave you a message.");
			else
				sender.sendMessage(event.getCancelledMessage());
				
			return true;
		}
		
		//recall data from the event
		j = event.getJail();
		pris = event.getPrisoner();
		p = event.getPlayer();
		String jailer = event.getJailer();
		
		//Player is not online
		if(p == null) {
			sender.sendMessage(pris.getName() + " is offline and will be jailed for " + pris.getRemainingTime() + " milliseconds in the jail " + params.jail() + " in the cell " + params.cell() + " and will be muted: " + pris.isMuted() + ".");
			sender.sendMessage(pris.getName() + " will be jailed for " + TimeUnit.MINUTES.convert(pris.getRemainingTime(), TimeUnit.MILLISECONDS) + " minutes by " + jailer + ".");
		}else {
			//Player *is* online
			sender.sendMessage(pris.getName() + " is online and will be jailed for " + pris.getRemainingTime() + " milliseconds in the jail " + params.jail() + " in the cell " + params.cell() + " and will be muted: " + pris.isMuted() + ".");
			sender.sendMessage(pris.getName() + " will be jailed for " + TimeUnit.MINUTES.convert(pris.getRemainingTime(), TimeUnit.MILLISECONDS) + " minutes by " + jailer + ".");
			
			if(params.reason().isEmpty()) {
				p.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.JAILED));
			}else {
				p.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.JAILEDWITHREASON, new String[] { params.reason() }));
			}
		}
		
		return true;
	}
}
