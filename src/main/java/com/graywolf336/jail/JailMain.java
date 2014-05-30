package com.graywolf336.jail;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.command.CommandHandler;
import com.graywolf336.jail.command.JailHandler;
import com.graywolf336.jail.enums.Settings;
import com.graywolf336.jail.legacy.LegacyManager;
import com.graywolf336.jail.listeners.BlockListener;
import com.graywolf336.jail.listeners.CacheListener;
import com.graywolf336.jail.listeners.EntityListener;
import com.graywolf336.jail.listeners.HandCuffListener;
import com.graywolf336.jail.listeners.JailingListener;
import com.graywolf336.jail.listeners.MoveProtectionListener;
import com.graywolf336.jail.listeners.PlayerListener;
import com.graywolf336.jail.listeners.ProtectionListener;

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
	private JailHandler jh;
	private JailIO io;
	private JailManager jm;
	private JailPayManager jpm;
	private JailStickManager jsm;
	private JailTimer jt;
	private PrisonerManager pm;
	private ScoreBoardManager sbm;
	private MoveProtectionListener mpl;
	private boolean debug = false;
	
	public void onEnable() {
		loadConfig();
		
		debug = getConfig().getBoolean(Settings.DEBUG.getPath());
		if(debug) getLogger().info("Debugging enabled.");
		
		hcm = new HandCuffManager();
		jm = new JailManager(this);
		
		//Try to load the old stuff before we load anything, esp the storage stuff
		LegacyManager lm = new LegacyManager(this);
		if(lm.doWeNeedToConvert()) {
			lm.convertOldData();
			if(!lm.wasAnythingConverted()) getLogger().severe("We was unable to convert some, or all, of the old data.");
		}
		
		io = new JailIO(this);
		io.loadLanguage();
		
		//If the prepareStorage returns false, we need to disable the plugin
		if(!io.prepareStorage(true)) {
			this.getLogger().severe("An error occured while preparing the connection to the storage, please see the error above for more information.");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		io.loadJails();
		
		//If we converted something, let's save EVERYTHING including the cells
		if(lm.wasAnythingConverted()) {
			io.saveEverything();
		}
		
		cmdHand = new CommandHandler(this);
		jh = new JailHandler(this);
		pm = new PrisonerManager(this);
		jsm = new JailStickManager(this);
		
		PluginManager plm = this.getServer().getPluginManager();
		plm.registerEvents(new BlockListener(this), this);
		plm.registerEvents(new CacheListener(this), this);
		plm.registerEvents(new EntityListener(this), this);
		plm.registerEvents(new HandCuffListener(this), this);
		plm.registerEvents(new JailingListener(this), this);
		plm.registerEvents(new PlayerListener(this), this);
		plm.registerEvents(new ProtectionListener(this), this);
		
		//Only register the move protection listener if this is enabled in the
		//config when we first start the plugin. The reason for this change is
		//that the move event is called a ton of times per single move and so
		//not registering this event listener will hopefully safe some performance.
		//But doing this also forces people to restart their server if they to
		//enable it after disabling it.
		if(getConfig().getBoolean(Settings.MOVEPROTECTION.getPath())) {
			this.mpl = new MoveProtectionListener(this);
			plm.registerEvents(this.mpl, this);
		}
		
		jt = new JailTimer(this);
		
		reloadJailPayManager();
		
		sbm = new ScoreBoardManager(this);
		
		getLogger().info("Completed enablement.");
	}

	public void onDisable() {
		if(jm != null)
			for(Jail j : jm.getJails())
				io.saveJail(j);
		
		if(jt != null)
			if(jt.getTimer() != null)
				jt.getTimer().stop();
		
		if(io != null)
			io.closeConnection();
		
		jt = null;
		sbm = null;
		jpm = null;
		cmdHand = null;
		pm = null;
		jm = null;
		jsm = null;
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
		if(commandLabel.equalsIgnoreCase("jail") || commandLabel.equalsIgnoreCase("j")) {
			jh.parseCommand(jm, sender, args);
		}else {
			cmdHand.handleCommand(jm, sender, command.getName().toLowerCase(), args);
		}
		
		return true;//Always return true here, that way we can handle the help and command usage ourself.
	}
	
	/** Reloads the scoreboard manager class, useful when something is changed int he config about it. */
	public void reloadScoreBoardManager() {
		this.sbm.removeAllScoreboards();
		this.sbm = null;
		this.sbm = new ScoreBoardManager(this);
		
		if(getConfig().getBoolean(Settings.SCOREBOARDENABLED.getPath())) {
			for(Jail j : jm.getJails()) {
				for(Prisoner p : j.getAllPrisoners().values()) {
					if(getServer().getPlayer(p.getUUID()) != null) {
						this.sbm.addScoreBoard(getServer().getPlayer(p.getUUID()), p);
					}
				}
			}
		}
	}
	
	/** Reloads the Jail Sticks, so the new ones can be loaded from the config. */
	public void reloadJailSticks() {
		this.jsm.removeAllStickUsers();
		this.jsm = null;
		this.jsm = new JailStickManager(this);
	}
	
	/** 
	 * Reloads the {@link JailPayManager}.
	 *  
	 * @throws Exception If we couldn't successfully create a new Jail Pay Manager instance.
	 */
	public void reloadJailPayManager() {
		this.jpm = null;
		
		if(getConfig().getBoolean(Settings.JAILPAYENABLED.getPath())) {
			if(getServer().getPluginManager().isPluginEnabled("Vault")) {
				this.jpm = new JailPayManager(this);
			}else {
				getConfig().set(Settings.JAILPAYENABLED.getPath(), false);
				getLogger().severe("Jail Pay couldn't find an economy, disabling Jail Pay.");
			}
		}
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
	
	/** Gets the {@link JailPayManager} instance. */
	public JailPayManager getJailPayManager() {
		return this.jpm;
	}
	
	/** Gets the {@link PrisonerManager} instance. */
	public PrisonerManager getPrisonerManager() {
		return this.pm;
	}
	
	/** Gets the {@link JailStickManager} instance. */
	public JailStickManager getJailStickManager() {
		return this.jsm;
	}
	
	/** Gets the {@link ScoreBoardManager} instance. */
	public ScoreBoardManager getScoreBoardManager() {
		return this.sbm;
	}
	
	/** Sets whether the plugin is in debugging or not. */
	public boolean setDebugging(boolean debug) {
		this.debug = debug;
		
		//Save whether we are debugging when we disable the plugin
		getConfig().set(Settings.DEBUG.getPath(), this.debug);
		saveConfig();
		
		return this.debug;
	}
	
	/** Returns if the plugin is in debug state or not. */
	public boolean inDebug() {
		return this.debug;
	}
	
	/** Logs a debugging message to the console if debugging is enabled. */
	public void debug(String message) {
		if(inDebug()) getLogger().info("[Debug]: " + message);
	}
	
	public MoveProtectionListener getPlayerMoveListener() {
		return this.mpl;
	}
}
