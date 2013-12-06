package com.graywolf336.jail.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.Command;
import com.graywolf336.jail.command.CommandInfo;

@CommandInfo(
		maxArgs = 2,
		minimumArgs = 2,
		needsPlayer = true,
		pattern = "jailcreatecells|jcc",
		permission = "jail.command.jailcreatecells",
		usage = "/jailcellcreate [jail] [cellname]"
	)
public class CellCreateCommand implements Command {

	public boolean execute(JailManager jm, CommandSender sender, String... args) {
		Player player = (Player) sender;
		String name = player.getName();
		String jail = args[0].toLowerCase();
		String cell = args[1];
		
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
				Cell c = j.getCell(cell);
				
				//No cell found
				if(c == null) {
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
