package com.graywolf336.jail.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;
import com.graywolf336.jail.command.commands.jewels.Transfer;
import com.graywolf336.jail.enums.LangString;
import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;

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
		Transfer params = null;
		
		try {
			params = CliFactory.parseArguments(Transfer.class, args);
		}catch(ArgumentValidationException e) {
			sender.sendMessage(ChatColor.RED + e.getMessage());
			return true;
		}
		
		//Verify they gave us a player and if so check if they're jailed
		if(params.getPlayer().isEmpty()) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PROVIDEAPLAYER, LangString.TRANSFERRING));
			return true;
		}else if(!jm.isPlayerJailed(params.getPlayer())) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOTJAILED, params.getPlayer()));
			return true;
		}
		
		jm.getPlugin().debug("Checking everything before we transfer: " + params.getPlayer());
		
		//If they didn't provide a jail, tell them we need one
		if(params.getJail().isEmpty()) {
			sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.PROVIDEAJAIL, LangString.TRANSFERRING));
			return true;
		}else {
			//Check if the jail they did provide exists
			if(jm.getJail(params.getJail()) == null) {
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOJAIL, params.getJail()));
				return true;
			}
		}
		
		Jail target = jm.getJail(params.getJail());
		Cell targetCell = null;
		
		//Check if they provided a cell and if so does it exist
		if(!params.getCell().isEmpty()) {
			if(target.getCell(params.getCell()) == null) {
				sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOCELL, params.getCell()));
				return true;
			}else {
				//Store the cell for easy of access and also check if it already is full
				targetCell = target.getCell(params.getCell());
				if(targetCell.hasPrisoner()) {
					//If the cell has a prisoner, don't allow jailing them to that particular cell
					sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.CELLNOTEMPTY, params.getCell()));
					
					//But suggest the first empty cell we find
					Cell suggestedCell = jm.getJail(params.getCell()).getFirstEmptyCell();
					if(suggestedCell != null) {
						sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.SUGGESTEDCELL, new String[] { params.getCell(), suggestedCell.getName() }));
					}else {
						sender.sendMessage(jm.getPlugin().getJailIO().getLanguageString(LangString.NOEMPTYCELLS, params.getCell()));
					}
					
					return true;
				}
			}
		}
		
		jm.getPlugin().debug("Sending the transferring off, jail and cell check all came out clean.");
		
		//Start the transferring of the prisoner
		jm.getPlugin().getPrisonerManager().transferPrisoner(jm.getJailPlayerIsIn(params.getPlayer()),
				jm.getJailPlayerIsIn(params.getPlayer()).getCellPrisonerIsIn(params.getPlayer()),
				target, targetCell, jm.getPrisoner(params.getPlayer()));
		
		return true;
	}
}
