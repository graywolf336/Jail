package com.graywolf336.jail.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = 2,
		minimumArgs = 1,
		needsPlayer = true,
		pattern = "createcell|cc",
		permission = "jail.command.jailcreatecells",
		usage = "/jail cellcreate [jail] (cellname)"
	)
public class JailCellCreateCommand implements Command {

	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		Player player = (Player) sender;
		String name = player.getName();
		String jail = args[1].toLowerCase();
		String cell = "";
		
		//Only get the cell name they provide if they provide it
		if(args.length >= 3) {
			cell = args[2];
		}
		
		//Check if the player is currently creating something else
		if(jm.isCreatingSomething(name)) {
			String message = jm.getStepMessage(name);
			if(!message.isEmpty()) {
				player.sendMessage(ChatColor.RED + message);
			}else {
				player.sendMessage(ChatColor.RED + "You're already creating something else, please finish it or cancel.");
			}
		}else {
			//Not creating anything, so let them create some cells.
			if(jm.isValidJail(jail)) {
				Jail j = jm.getJail(jail);
				
				//If they didn't provide a cell name, let's provide one ourself.
				if(cell.isEmpty()) cell = "cell_n" + (j.getCellCount() + 1);
				
				if(j.getCell(cell) == null) {
					if(jm.addCreatingCell(name, jail, cell)) {
						jm.getCellCreationSteps().startStepping(player);
					}else {
						player.sendMessage(ChatColor.RED + "Appears you're creating a cell or something went wrong on our side.");
					}
				}else {
					player.sendMessage(ChatColor.RED + "There's already a cell with the name '" + cell + "', please pick a new one or remove that cell.");
				}
			}else {
				player.sendMessage(ChatColor.RED + "No such jail found by the name of '" + jail + "'.");
			}
		}
		
		return true;
	}

}
