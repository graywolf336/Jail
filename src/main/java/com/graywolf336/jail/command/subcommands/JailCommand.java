package com.graywolf336.jail.command.subcommands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.command.jcommands.Jailing;
import com.graywolf336.jail.enums.LangString;
import com.graywolf336.jail.enums.Settings;
import com.graywolf336.jail.events.PrePrisonerJailedEvent;

@CommandInfo(
		maxArgs = -1,
		minimumArgs = 1,
		needsPlayer = false,
		pattern = "jail|j",
		permission = "jail.command.jail",
		usage = "/jail [name] (-t time) (-j JailName) (-c CellName) (-m Muted) (-r A reason for jailing)"
	)
public class JailCommand implements Command {
	
	/*
	 * Executes the command. Checks the following:
	 * 
	 * - If there are any jails.
	 * - If the command can be parsed correctly.
	 * - If the player is already jailed.
	 * - If the given time can be parsed correctly, defaults to what is defined in the config
	 * - If the jail is reasonable or not, else sets the one from the config
	 * - If the cell is not empty then checks to be sure that cell exists
	 * - If the prisoner is online or not.
	 */
	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		
		if(jm.getJails().isEmpty()) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAILS));
			return true;
		}
		
		//This is just to add the -p param so jCommander doesn't blow up
		List<String> arguments = new LinkedList<String>(Arrays.asList(args));
		arguments.add(0, "-p");
		
		Jailing params = new Jailing();
		
		try {
			new JCommander(params, arguments.toArray(new String[arguments.size()]));
		}catch(ParameterException e) {
			sender.sendMessage(ChatColor.RED + e.getMessage());
			return true;
		}
		
		//Check if the given player is already jailed or not
		if(jm.isPlayerJailed(params.player())) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.ALREADYJAILED));
			return true;
		}
		
		//Try to parse the time, if they give us nothing in the time parameter then we get the default time
		//from the config and if that isn't there then we default to thirty minutes.
		Long time = 10L;
		try {
			if(params.time().isEmpty()) {
				time = Util.getTime(jm.getPlugin().getConfig().getString(Settings.JAILDEFAULTTIME.getPath(), "30m"));
			}else if(params.time() == String.valueOf(-1)) {
				time = -1L;
			}else {
				time = Util.getTime(params.time());
			}
		}catch(Exception e) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NUMBERFORMATINCORRECT));
			return true;
		}
		
		//Check the jail params. If it is empty, let's get the default jail
		//from the config. If that is nearest, let's make a call to getting the nearest jail to
		//the sender but otherwise if it isn't nearest then let's set it to the default jail
		//which is defined in the config. After that is done, we set the name of it in the params
		//so that we can keep consistency.
		if(params.jail().isEmpty()) {
			String dJail = jm.getPlugin().getConfig().getString(Settings.DEFAULTJAIL.getPath());
			
			if(dJail.equalsIgnoreCase("nearest")) {
				params.setJail(jm.getNearestJail(sender).getName());
			}else {
				params.setJail(jm.getPlugin().getConfig().getString(Settings.DEFAULTJAIL.getPath()));
			}
		}else if(jm.getJail(params.jail()) == null) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAIL, params.jail()));
			return true;
		}
		
		//Check if the cell is defined, and if so check to be sure it exists.
		if(!params.cell().isEmpty()) {
			if(jm.getJail(params.jail()).getCell(params.cell()) == null) {
				//There is no cell by that name
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOCELL, new String[] { params.cell(), params.jail() }));
				return true;
			}else if(jm.getJail(params.jail()).getCell(params.cell()).hasPrisoner()) {
				//If the cell has a prisoner, don't allow jailing them to that particular cell
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.CELLNOTEMPTY, params.cell()));
				Cell suggestedCell = jm.getJail(params.jail()).getFirstEmptyCell();
				if(suggestedCell != null) {
					sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.SUGGESTEDCELL, new String[] { params.jail(), suggestedCell.getName() }));
				}else {
					sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOEMPTYCELLS, params.jail()));
				}
				
				return true;
			}
		}
		
		//If the jailer gave no reason, then let's get the default reason
		if(params.reason().isEmpty()) {
			params.setReason(jm.getPlugin().getJailIO().getLanguageString(LangString.DEFAULTJAILEDREASON));
		}
		
		//If the config has automatic muting, then let's set them as muted
		if(jm.getPlugin().getConfig().getBoolean(Settings.AUTOMATICMUTE.getPath())) {
			params.setMuted(true);
		}
		
		Player p = jm.getPlugin().getServer().getPlayer(params.player());
		
		//If the player instance is not null and the player has the permission
		//'jail.cantbejailed' then don't allow this to happen
		if(p != null && p.hasPermission("jail.cantbejailed")) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.CANTBEJAILED));
			return true;
		}
		
		//Get the jail instance from the name of jail in the params.
		Jail j = jm.getJail(params.jail());
		Cell c = j.getCell(params.cell());
		Prisoner pris = new Prisoner(params.player(), params.muted(), time, sender.getName(), params.reason());
		
		//call the event
		PrePrisonerJailedEvent event = new PrePrisonerJailedEvent(j, c, pris, p, p == null, pris.getJailer());
		jm.getPlugin().getServer().getPluginManager().callEvent(event);
		
		//check if the event is cancelled
		if(event.isCancelled()) {
			if(event.getCancelledMessage().isEmpty())
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.CANCELLEDBYANOTHERPLUGIN, params.player()));
			else
				sender.sendMessage(event.getCancelledMessage());
				
			return true;
		}
		
		//recall data from the event
		j = event.getJail();
		c = event.getCell();
		pris = event.getPrisoner();
		p = event.getPlayer();
		
		//Player is not online
		if(p == null) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.OFFLINEJAIL, new String[] { pris.getName(), String.valueOf(pris.getRemainingTimeInMinutes()) }));
		}else {
			//Player *is* online
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.ONLINEJAIL, new String[] { pris.getName(), String.valueOf(pris.getRemainingTimeInMinutes()) }));
		}
		
		try {
			jm.getPlugin().getPrisonerManager().prepareJail(j, c, p, pris);
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED + e.getMessage());
			return true;
		}
		
		return true;
	}
}
