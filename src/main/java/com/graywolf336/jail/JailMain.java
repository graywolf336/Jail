package com.graywolf336.jail;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.command.CommandHandler;
import com.graywolf336.jail.enums.Settings;
import com.graywolf336.jail.listeners.BlockListener;
import com.graywolf336.jail.listeners.EntityListener;
import com.graywolf336.jail.listeners.HandCuffListener;
import com.graywolf336.jail.listeners.PlayerListener;

/**
 * The main class for this Jail plugin, holds instances of vital classes.
 * 
 * @author graywolf336
 * @since 1.x.x
 * @version 3.0.0
 */
public class JailMain extends JavaPlugin {
	private CommandHandler cmdHand;
	private HandCuffManager hcm;
	private JailIO io;
	private JailManager jm;
	private PrisonerManager pm;
	private boolean debug = false;
	
	public void onEnable() {
		loadConfig();
		
		hcm = new HandCuffManager();
		jm = new JailManager(this);
		io = new JailIO(this);
		io.loadLanguage();
		io.prepareStorage();
		io.loadJails();
		
		cmdHand = new CommandHandler(this);
		pm = new PrisonerManager(this);
		
		PluginManager plm = this.getServer().getPluginManager();
		plm.registerEvents(new BlockListener(), this);
		plm.registerEvents(new EntityListener(), this);
		plm.registerEvents(new HandCuffListener(this), this);
		plm.registerEvents(new PlayerListener(this), this);
		
		debug = getConfig().getBoolean(Settings.DEBUG.getPath());
		
		if(debug) getLogger().info("Debugging enabled.");
		
		//For the time, we will use:
		//http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/TimeUnit.html#convert(long, java.util.concurrent.TimeUnit)
	}

	public void onDisable() {
		if(jm != null)
			for(Jail j : jm.getJails())
				io.saveJail(j);
		
		cmdHand = null;
		pm = null;
		jm = null;
		io = null;
		hcm = null;
	}
	
	private void loadConfig() {
		//Only create the default config if it doesn't exist
		saveDefaultConfig();
		
		//Append new key-value pairs to the config
		getConfig().options().copyDefaults(true);
		
		// Set the header and save
        getConfig().options().header(getHeader());
        saveConfig();
	}
	
	private String getHeader() {
		String sep = System.getProperty("line.separator");
		
		return "###################" + sep
				+ "Jail v" + this.getDescription().getVersion() + " config file" + sep
				+ "Note: You -must- use spaces instead of tabs!" + sep +
				"###################";
	}
	
	/* Majority of the new command system was heavily influenced by the MobArena.
	 * Thank you garbagemule for the great system you have in place there.
	 *
	 * Send the command off to the CommandHandler class, that way this main class doesn't get clogged up.
	 */
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		cmdHand.handleCommand(jm, sender, command.getName().toLowerCase(), args);
		return true;//Always return true here, that way we can handle the help and command usage ourself.
	}
	
	/** Gets the {@link HandCuffManager} instance. */
	public HandCuffManager getHandCuffManager() {
		return this.hcm;
	}
	
	/** Gets the {@link JailIO} instance. */
	public JailIO getJailIO() {
		return this.io;
	}
	
	/** Gets the {@link JailManager} instance. */
	public JailManager getJailManager() {
		return this.jm;
	}
	
	/** Gets the {@link PrisonerManager} instance. */
	public PrisonerManager getPrisonerManager() {
		return this.pm;
	}
	
	/** Returns if the plugin is in debug state or not. */
	public boolean inDebug() {
		return this.debug;
	}
}
