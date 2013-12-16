package com.graywolf336.jail.command;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.JailManager;

import com.graywolf336.jail.command.commands.CellCreateCommand;
import com.graywolf336.jail.command.commands.JailCheckCommand;
import com.graywolf336.jail.command.commands.JailClearCommand;
import com.graywolf336.jail.command.commands.JailCommand;
import com.graywolf336.jail.command.commands.JailCreateCommand;
import com.graywolf336.jail.command.commands.JailListCellCommand;
import com.graywolf336.jail.command.commands.JailListCommand;
import com.graywolf336.jail.command.commands.JailRemoveCellCommand;
import com.graywolf336.jail.command.commands.JailStopCommand;
import com.graywolf336.jail.command.commands.JailVersionCommand;

/**
 * Where all the commands are registered at and handled, processed, at.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.2
 *
 */
public class CommandHandler {
	private LinkedHashMap<String, Command> commands;
	
	public CommandHandler(JailMain plugin) {
		commands = new LinkedHashMap<String, Command>();
		loadCommands();
		
		plugin.getLogger().info("Loaded " + commands.size() + " commands.");
	}
	
	/**
	 * Handles the given command and checks that the command is in valid form.
	 * 
	 * <p>
	 * 
	 * It checks in the following order:
	 * <ol>
	 * 	<li>If the command is registered or not.</li>
	 * 	<li>If more than one command matches the command's name and sends the usage for each one.</li>
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
	public void handleCommand(JailManager jailmanager, CommandSender sender, String command, String[] args) {
		List<Command> matches = getMatches(command);
		
		if(matches.size() == 0) {
			sender.sendMessage("No commands registered by the name of " + command + ".");
			return;
		}
		
		if(matches.size() > 1) {
			for(Command c : matches)
				showUsage(sender, c);
			return;
		}
		
		Command c = matches.get(0);
		CommandInfo i = c.getClass().getAnnotation(CommandInfo.class);
		
		// First, let's check if the sender has permission for the command.
		if(!sender.hasPermission(i.permission())) {
			sender.sendMessage("No permission to use that command.");//TODO: Make this configurable
			return;
		}
		
		// Next, let's check if we need a player and then if the sender is actually a player
		if(i.needsPlayer() && !(sender instanceof Player)) {
			sender.sendMessage("A player context is required for this command.");
			return;
		}
		
		// Now, let's check the size of the arguments passed. If it is shorter than the minimum required args, let's show the usage.
		if(args.length < i.minimumArgs()) {
			showUsage(sender, c);
			return;
		}
		
		// Then, if the maximumArgs doesn't equal -1, we need to check if the size of the arguments passed is greater than the maximum args.
		if(i.maxArgs() != -1 && i.maxArgs() < args.length) {
			showUsage(sender, c);
			return;
		}
		
		// Since everything has been checked and we're all clear, let's execute it.
		// But if get back false, let's show the usage message.
		if(!c.execute(jailmanager, sender, args)) {
			showUsage(sender, c);
			return;
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
	
	/** Loads all the commands into the hashmap. */
	private void loadCommands() {
		load(CellCreateCommand.class);
		load(JailCheckCommand.class);
		load(JailClearCommand.class);
		load(JailCommand.class);
		load(JailCreateCommand.class);
		load(JailListCellCommand.class);
		load(JailListCommand.class);
		load(JailRemoveCellCommand.class);
		load(JailStopCommand.class);
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
