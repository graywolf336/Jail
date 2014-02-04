package com.graywolf336.jail.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.command.commands.params.Transferring;
import com.graywolf336.jail.enums.LangString;

@CommandInfo(
		maxArgs = 6,
		minimumArgs = 2,
		needsPlayer = false,
		pattern = "transfer|trans",
		permission = "jail.command.jailtransfer",
		usage = "/jail transfer -p player -j jail -c cell"
	)
public class JailTransferCommand implements Command {
	public boolean execute(JailManager jm, CommandSender sender, String... args) throws Exception {
		if(jm.getJails().isEmpty()) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAILS));
			return true;
		}
		
		//Parse the command
		Transferring params = new Transferring();
		
		try {
			new JCommander(params, args);
		}catch(ParameterException e) {
			sender.sendMessage(ChatColor.RED + e.getMessage());
			return true;
		}
		
		//Verify they gave us a player and if so check if they're jailed
		if(params.player().isEmpty()) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PROVIDEAPLAYER, LangString.TRANSFERRING));
			return true;
		}else if(!jm.isPlayerJailed(params.player())) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOTJAILED, params.player()));
			return true;
		}
		
		jm.getPlugin().debug("Checking everything before we transfer: " + params.player());
		
		//If they didn't provide a jail, tell them we need one
		if(params.jail().isEmpty()) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PROVIDEAJAIL, LangString.TRANSFERRING));
			return true;
		}else {
			//Check if the jail they did provide exists
			if(jm.getJail(params.jail()) == null) {
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAIL, params.jail()));
				return true;
			}
		}
		
		Jail target = jm.getJail(params.jail());
		Cell targetCell = null;
		
		//Check if they provided a cell and if so does it exist
		if(!params.cell().isEmpty()) {
			if(target.getCell(params.cell()) == null) {
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOCELL, params.cell()));
				return true;
			}else {
				//Store the cell for easy of access and also check if it already is full
				targetCell = target.getCell(params.cell());
				if(targetCell.hasPrisoner()) {
					//If the cell has a prisoner, don't allow jailing them to that particular cell
					sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.CELLNOTEMPTY, params.cell()));
					
					//But suggest the first empty cell we find
					Cell suggestedCell = jm.getJail(params.jail()).getFirstEmptyCell();
					if(suggestedCell != null) {
						sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.SUGGESTEDCELL, new String[] { params.jail(), suggestedCell.getName() }));
					}else {
						sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOEMPTYCELLS, params.jail()));
					}
					
					return true;
				}
			}
		}
		
		jm.getPlugin().debug("Sending the transferring off, jail and cell check all came out clean.");
		
		//Start the transferring of the prisoner
		jm.getPlugin().getPrisonerManager().transferPrisoner(jm.getJailPlayerIsIn(params.player()),
				jm.getJailPlayerIsIn(params.player()).getCellPrisonerIsIn(params.player()),
				target, targetCell, jm.getPrisoner(params.player()));
		
		return true;
	}
}
