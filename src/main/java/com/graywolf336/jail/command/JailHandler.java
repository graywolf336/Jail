package com.graywolf336.jail.command;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.JailManager;
import com.graywolf336.jail.command.subcommands.JailCellCreateCommand;
import com.graywolf336.jail.command.subcommands.JailCheckCommand;
import com.graywolf336.jail.command.subcommands.JailClearCommand;
import com.graywolf336.jail.command.subcommands.JailClearForceCommand;
import com.graywolf336.jail.command.subcommands.JailCommand;
import com.graywolf336.jail.command.subcommands.JailCreateCommand;
import com.graywolf336.jail.command.subcommands.JailDeleteCellCommand;
import com.graywolf336.jail.command.subcommands.JailDeleteCellsCommand;
import com.graywolf336.jail.command.subcommands.JailDeleteCommand;
import com.graywolf336.jail.command.subcommands.JailListCellsCommand;
import com.graywolf336.jail.command.subcommands.JailListCommand;
import com.graywolf336.jail.command.subcommands.JailMuteCommand;
import com.graywolf336.jail.command.subcommands.JailReloadCommand;
import com.graywolf336.jail.command.subcommands.JailRemoveCellCommand;
import com.graywolf336.jail.command.subcommands.JailStopCommand;
import com.graywolf336.jail.command.subcommands.JailTeleInCommand;
import com.graywolf336.jail.command.subcommands.JailTeleOutCommand;
import com.graywolf336.jail.command.subcommands.JailTransferAllCommand;
import com.graywolf336.jail.command.subcommands.JailTransferCommand;
import com.graywolf336.jail.command.subcommands.JailVersionCommand;
import com.graywolf336.jail.enums.LangString;

public class JailHandler {
	private LinkedHashMap<String, Command> commands;
	
	public JailHandler(JailMain plugin) {
		commands = new LinkedHashMap<String, Command>();
		loadCommands();
		
		plugin.getLogger().info("Loaded " + commands.size() + " sub-commands of /jail.");
	}
	
	/**
	 * Handles the given command and checks that the command is in valid form.
	 * 
	 * <p>
	 * 
	 * It checks in the following order:
	 * <ol>
	 * 	<li>If they have permission for it, if they don't then we send them a message stating so.</li>
	 * 	<li>If the command needs a player instance, if so we send a message stating that.</li>
	 * 	<li>If the required minimum arguments have been passed, if not sends the usage.</li>
	 * 	<li>If the required maximum arguments have been passed (if there is a max, -1 if no max), if not sends the usage.</li>
	 * 	<li>Then executes, upon failed execution it sends the usage command.</li>
	 * </ol>
	 * 
	 * @param jailmanager The instance of {@link JailManager}.
	 * @param sender The sender of the command.
	 * @param command The name of the command.
	 * @param args The arguments passed to the command.
	 */
	public boolean parseCommand(JailManager jailmanager, CommandSender sender, String[] args) {
		Command c = null;
		
		//If they didn't provide any arguments (aka just: /jail) then we will need to send them some help
		if(args.length == 0) {
			//TODO: Create the help page(s)
			c = getMatches("jail").get(0);
			
		}else {
			//Get the matches from the first argument passed
			List<Command> matches = getMatches(args[0]);
			
			if(matches.size() == 0) {
				//No matches found, thus it is more likely than not they are trying to jail someone
				c = getMatches("jail").get(0);
				
			} else if(matches.size() > 1) {
				//If there was found more than one match
				//then let's send the usage of each match to the sender
				for(Command cmd : matches)
					showUsage(sender, cmd);
				return true;
				
			}else {
				//Only one match was found, so let's continue
				c = matches.get(0);
			}
		}
				
		CommandInfo i = c.getClass().getAnnotation(CommandInfo.class);
		
		// First, let's check if the sender has permission for the command.
		if(!sender.hasPermission(i.permission())) {
			jailmanager.getPlugin().debug("Sender has no permission.");
			sender.sendMessage(jailmanager.getPlugin().getJailIO().getLanguageString(LangString.NOPERMISSION));
			return true;
		}
		
		// Next, let's check if we need a player and then if the sender is actually a player
		if(i.needsPlayer() && !(sender instanceof Player)) {
			jailmanager.getPlugin().debug("Sender is not a player.");
			sender.sendMessage(jailmanager.getPlugin().getJailIO().getLanguageString(LangString.PLAYERCONTEXTREQUIRED));
			return true;
		}
		
		// Now, let's check the size of the arguments passed. If it is shorter than the minimum required args, let's show the usage.
		// The reason we are subtracting one is because the command is now `/jail <subcommand>` and the subcommand is viewed as an argument
		if(args.length - 1 < i.minimumArgs()) {
			jailmanager.getPlugin().debug("Sender didn't provide enough arguments.");
			showUsage(sender, c);
			return true;
		}
		
		// Then, if the maximumArgs doesn't equal -1, we need to check if the size of the arguments passed is greater than the maximum args.
		// The reason we are subtracting one is because the command is now `/jail <subcommand>` and the subcommand is viewed as an argument
		if(i.maxArgs() != -1 && i.maxArgs() < args.length - 1) {
			jailmanager.getPlugin().debug("Sender provided too many arguments.");
			showUsage(sender, c);
			return true;
		}
		
		// Since everything has been checked and we're all clear, let's execute it.
		// But if get back false, let's show the usage message.
		try {
			if(!c.execute(jailmanager, sender, args)) {
				showUsage(sender, c);
				return true;
			}else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			jailmanager.getPlugin().getLogger().severe("An error occured while handling the command: " + i.usage());
			showUsage(sender, c);
			return true;
		}
	}
	
	private List<Command> getMatches(String command) {
		List<Command> result = new ArrayList<Command>();
		
		for(Entry<String, Command> entry : commands.entrySet()) {
			if(command.matches(entry.getKey())) {
				result.add(entry.getValue());
			}
		}
		
		return result;
	}
	
	/**
	 * Shows the usage information to the sender, if they have permission.
	 * 
	 * @param sender The sender of the command
	 * @param command The command to send usage of.
	 */
	private void showUsage(CommandSender sender, Command command) {
		CommandInfo info = command.getClass().getAnnotation(CommandInfo.class);
		if(!sender.hasPermission(info.permission())) return;
		
		sender.sendMessage(info.usage());
	}
	
	private void loadCommands() {
		load(JailCellCreateCommand.class);
		load(JailCheckCommand.class);
		load(JailClearCommand.class);
		load(JailClearForceCommand.class);
		load(JailCommand.class);
		load(JailCreateCommand.class);
		load(JailDeleteCellCommand.class);
		load(JailDeleteCellsCommand.class);
		load(JailDeleteCommand.class);
		load(JailListCellsCommand.class);
		load(JailListCommand.class);
		load(JailMuteCommand.class);
		load(JailReloadCommand.class);
		load(JailRemoveCellCommand.class);
		load(JailStopCommand.class);
		load(JailTeleInCommand.class);
		load(JailTeleOutCommand.class);
		load(JailTransferAllCommand.class);
		load(JailTransferCommand.class);
		load(JailVersionCommand.class);
	}
	
	private void load(Class<? extends Command> c) {
		CommandInfo info = c.getAnnotation(CommandInfo.class);
		if(info == null) return;
		
		try {
			commands.put(info.pattern(), c.newInstance());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
