package com.graywolf336.jail;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.beans.SimpleLocation;
import com.graywolf336.jail.enums.LangString;

/**
 * Handles all the saving and loading of the plugin's data.
 * 
 * @author graywolf336
 * @since 2.x.x
 * @version 3.0.0
 * 
 */
public class JailIO {
	private JailMain pl;
	private FileConfiguration flat, lang;
	private Connection con;
	private int storage; //0 = flatfile, 1 = sqlite, 2 = mysql
	private String prefix;
	
	public JailIO(JailMain plugin) {
		this.pl = plugin;
		
		String st = pl.getConfig().getString("storage.type", "flatfile");
		if(st.equalsIgnoreCase("sqlite")) {
			storage = 1;
			prefix = pl.getConfig().getString("storage.mysql.prefix");
		}else if(st.equalsIgnoreCase("mysql")) {
			storage = 2;
			prefix = pl.getConfig().getString("storage.mysql.prefix");
		}else {
			storage = 0;
		}
		
		pl.debug("The storage type " + st + " with the type being " + storage + ".");
	}
	
	/** Loads the language file from disk, if there is none then we save the default one. */
	public void loadLanguage() {
		String language = pl.getConfig().getString("system.language");
		boolean save = false;
		File langFile = new File(pl.getDataFolder(), language + ".yml");
		
		//File or folder already exists, let's check
		if(langFile.exists()) {
			if(langFile.isFile()) {
				lang = YamlConfiguration.loadConfiguration(langFile);
				pl.getLogger().info("Loaded the language: " + language);
			}else {
				pl.getLogger().severe("The language file can not be a folder.");
				pl.getLogger().severe("As a result, we are reverting back to English as the language.");
				lang = YamlConfiguration.loadConfiguration(pl.getResource("en.yml"));
				save = true;
			}
		}else {
			pl.getLogger().warning("Loading the default language of: en");
			pl.getLogger().warning("If you wish to change this, please rename 'en.yml' to whatever you wish and set the config value to the name of the file.");
			lang = YamlConfiguration.loadConfiguration(pl.getResource("en.yml"));
			save = true;
		}
		
		//If we have flagged to save the language file, let's save it as en.yml as this flag usually means they didn't have it loaded.
		if(save) {
			try {
				lang.save(new File(pl.getDataFolder(), "en.yml"));
			} catch (IOException e) {
				pl.getLogger().severe("Unable to save the language file: " + e.getMessage());
			}
		}
	}
	
	/** Returns the message in the language, no variables are replaced.*/
	public String getLanguageString(LangString langString) {
		return getLanguageString(langString, new String[] {});
	}
	
	/** Returns the message in the language, no variables are replaced.*/
	public String getLanguageString(LangString langString, LangString langString2) {
		return getLanguageString(langString, getLanguageString(langString2, new String[] {}));
	}
	
	/**
	 * Returns the message in the language, with the provided variables being replaced.
	 * 
	 * @param langString Which {@link LangString} we should be getting to send.
	 * @param variables All the variables to replace, in order from 0 to however many.
	 * @return The message as a colorful message or an empty message if that isn't defined in the language file.
	 */
	public String getLanguageString(LangString langString, String... variables) {
		String message = lang.getString("language." + langString.getSection() + "." + langString.getName());
		
		if(message == null) return "";
		
		for (int i = 0; i < variables.length; i++) {
			message = message.replaceAll("%" + i + "%", variables[i]);
		}
		
		return Util.getColorfulMessage(message);
	}
	
	/** Prepares the storage engine to be used, returns true if everything went good. */
	public boolean prepareStorage(boolean doInitialCreations) {
		switch(storage) {
			case 1:
				try {
					Class.forName("org.sqlite.JDBC");
					pl.getLogger().info("Connecting to the sqlite database.");
					Connection sqliteConnection = DriverManager.getConnection("jdbc:sqlite:" +  new File(pl.getDataFolder().getPath(), "jail.sqlite").getPath());
					sqliteConnection.setAutoCommit(false);
					this.con = sqliteConnection;
					pl.debug("Connection created for sqlite.");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Sqlite driver not found, disabling the plugin.");
					return false;
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Unable to connect to the sqlite database, please update your config accordingly.");
					return false;
				}
                
				break;
			case 2:
				try {
					Class.forName("com.mysql.jdbc.Driver");
					pl.getLogger().info("Connecting to the MySQL database.");
					Connection mysqlConnection = DriverManager.getConnection("jdbc:mysql://" + pl.getConfig().getString("storage.mysql.host") + ":"
							+ pl.getConfig().getString("storage.mysql.port") + "/"
							+ pl.getConfig().getString("storage.mysql.database"), pl.getConfig().getString("storage.mysql.username"), pl.getConfig().getString("storage.mysql.password"));
					mysqlConnection.setAutoCommit(false);
					this.con = mysqlConnection;
					pl.debug("Connection created for MySQL.");
					
					if(doInitialCreations) createTables();
				} catch(ClassNotFoundException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("MySQL driver not found, disabling the plugin.");
					return false;
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Unable to connect to the MySQL database, please update your config accordingly.");
					return false;
				}
				
				break;
			default:
				flat = YamlConfiguration.loadConfiguration(new File(pl.getDataFolder(), "data.yml"));
				break;
		}
		
		return true;
	}
	
	/**
	 * Gets the connection for the sqlite and mysql, null if flatfile.
	 * 
	 * @return The connection for the sql database.
	 */
	public Connection getConnection() {
		switch(storage) {
			case 1:
			case 2:
				if(con == null) this.prepareStorage(false);
				return con;
			default:
				return null;
		}
	}
	
	/** Closes the sql connection. */
	public void closeConnection() {
		switch(storage) {
			case 1:
			case 2:
				try {
					if(con != null) {
						con.close();
						con = null;
						
						pl.debug("Closed the SQL connection.");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Unable to close the SQL connection.");
				}
				
				break;
			default:
				break;
		}
	}
	
	private void createTables() {
		if(con == null) {
			pl.debug("The connection was null when we tried to create a table.");
			return;
		}
		
		try {
			Statement st = con.createStatement();
			switch(storage){ 
				case 1:
					break;
				case 2:
					String jailCreateCmd = "CREATE TABLE IF NOT EXISTS `" + prefix + "jails` ("
							  + "`name` VARCHAR(250) NOT NULL,"
							  + "`world` VARCHAR(250) NOT NULL COMMENT 'The world for the top, bottom, and teleport in.',"
							  + "`top.x` INT NOT NULL COMMENT 'The top coordinate x.',"
							  + "`top.y` INT NOT NULL COMMENT 'The top coordinate y.',"
							  + "`top.z` INT NOT NULL COMMENT 'The top coordinate z.',"
							  + "`bottom.x` INT NOT NULL COMMENT 'The bottom coordinate x.',"
							  + "`bottom.y` INT NOT NULL COMMENT 'The bottom coordinate y.',"
							  + "`bottom.z` INT NOT NULL COMMENT 'The bottom coordinate z.',"
							  + "`tps.in.x` DOUBLE NOT NULL COMMENT 'The teleport in x coordinate.',"
							  + "`tps.in.y` DOUBLE NOT NULL COMMENT 'The teleport in y coordinate.',"
							  + "`tps.in.z` DOUBLE NOT NULL COMMENT 'The teleport in z coordinate.',"
							  + "`tps.in.yaw` DOUBLE NOT NULL COMMENT 'The teleport in yaw.',"
							  + "`tps.in.pitch` DOUBLE NOT NULL COMMENT 'The teleport in pitch.',"
							  + "`tps.free.world` VARCHAR(250) NOT NULL COMMENT 'The teleport for being free world.',"
							  + "`tps.free.x` DOUBLE NOT NULL COMMENT 'The teleport for being free x coordinate.',"
							  + "`tps.free.y` DOUBLE NOT NULL COMMENT 'The teleport for being free y coordinate.',"
							  + "`tps.free.z` DOUBLE NOT NULL COMMENT 'The teleport for being free z coordinate.',"
							  + "`tps.free.yaw` DOUBLE NOT NULL COMMENT 'The teleport for being free yaw.',"
							  + "`tps.free.pitch` DOUBLE NOT NULL COMMENT 'The teleport for being free pitch.',"
							  + "PRIMARY KEY (`name`),"
							  + "UNIQUE INDEX `name_UNIQUE` (`name` ASC))"
							  + "COMMENT = 'Holds all the jails for the Bukkit Jail plugin.';";
					
					pl.debug(jailCreateCmd);
					st.executeUpdate(jailCreateCmd);
					
					String cellCreateCmd = "CREATE TABLE IF NOT EXISTS `" + prefix + "cells` ("
							  + "`cellid` INT NOT NULL AUTO_INCREMENT COMMENT 'The cellid for the database.',"
							  + "`name` VARCHAR(250) NOT NULL COMMENT 'The name of the cell.',"
							  + "`jail` VARCHAR(250) NOT NULL COMMENT 'The name of the jail the cell is in.',"
							  + "`tp.x` DOUBLE NOT NULL COMMENT 'The teleport in x coordinate.',"
							  + "`tp.y` DOUBLE NOT NULL COMMENT 'The teleport in y coordinate.',"
							  + "`tp.z` DOUBLE NOT NULL COMMENT 'The teleport in z coordinate.',"
							  + "`tp.yaw` DOUBLE NOT NULL COMMENT 'The teleport in yaw.',"
							  + "`tp.pitch` DOUBLE NOT NULL COMMENT 'The teleport in pitch.',"
							  + "`chest.x` INT NULL COMMENT 'The chest x coordinate.',"
							  + "`chest.y` INT NULL COMMENT 'The chest y coordinate.',"
							  + "`chest.z` INT NULL COMMENT 'The chest z coordinate.',"
							  + "`signs` VARCHAR(250) NULL COMMENT 'A string containing the signs.',"
							  + "PRIMARY KEY (`cellid`),"
							  + "UNIQUE INDEX `cellid_UNIQUE` (`cellid` ASC))"
							  + "COMMENT = 'Contains all the cells for the jails.';";
					
					pl.debug(cellCreateCmd);
					st.executeUpdate(cellCreateCmd);
					
					String prisCreateCmd = "CREATE TABLE IF NOT EXISTS `" + prefix + "prisoners` ("
							  + "`name` VARCHAR(16) NOT NULL COMMENT 'The name of the prisoner.',"
							  + "`jail` VARCHAR(250) NOT NULL COMMENT 'The jail the prisoner is in.',"
							  + "`cell` VARCHAR(250) NULL COMMENT 'The cell the prisoner is in.',"
							  + "`muted` TINYINT NOT NULL COMMENT 'Whether the player is muted or not.',"
							  + "`time` INT NOT NULL COMMENT 'The remaining time the prisoner has.',"
							  + "`offlinePending` TINYINT NOT NULL COMMENT 'Whether the prisoner has something happened to them while they were offline.',"
							  + "`toBeTransferred` TINYINT NOT NULL COMMENT 'Whether the prisoner is to be transferred.',"
							  + "`jailer` VARCHAR(250) NOT NULL COMMENT 'The name of the person who jailed them.',"
							  + "`reason` VARCHAR(250) NOT NULL COMMENT 'The reason they are jailed.',"
							  + "`inventory` BLOB NULL COMMENT 'Their inventory in base64.',"
							  + "`armor` BLOB NULL COMMENT 'The armor in base64.',"
							  + "`previousLocation` VARCHAR(250) NULL COMMENT 'A string of their previous location.',"
							  + "`previousGameMode` VARCHAR(16) NULL COMMENT 'Their previous gamemode before they were jailed.',"
							  + "PRIMARY KEY (`name`),"
							  + "UNIQUE INDEX `name_UNIQUE` (`name` ASC))"
							  + "COMMENT = 'Contains all the prisoners, in cells and jails.';";
					
					pl.debug(prisCreateCmd);
					st.executeUpdate(prisCreateCmd);
					
					String proCreateCmd = "CREATE TABLE IF NOT EXISTS `" + prefix + "profiles` ("
							  + "`profileid` INT NOT NULL AUTO_INCREMENT COMMENT 'Auto generated number for the profiles database.',"
							  + "`username` VARCHAR(16) NOT NULL COMMENT 'The username of the prisoner.',"
							  + "`jailer` VARCHAR(250) NOT NULL COMMENT 'The name of the person who jailed the prisoner.',"
							  + "`date` VARCHAR(32) NOT NULL COMMENT 'A string of the date.',"
							  + "`time` INT NOT NULL COMMENT 'The milliseconds they were jailed for.',"
							  + "`reason` VARCHAR(250) NOT NULL COMMENT 'The reason they were jailed for.',"
							  + "PRIMARY KEY (`profileid`),"
							  + "UNIQUE INDEX `profileid_UNIQUE` (`profileid` ASC))"
							  + "COMMENT = 'Holds a history of all the times prisoners have been jailed.'";
					
					pl.debug(proCreateCmd);
					st.executeUpdate(proCreateCmd);
				
					con.commit();
					st.close();
					break;
				default:
					break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			pl.getLogger().severe("---------- Jail Error!!! ----------");
			pl.getLogger().severe("Error while creating the tables, please check the error and fix what is wrong.");
		}
	}
	
	/**
	 * Loads the jails, this should <strong>only</strong> be called after {@link #prepareStorage()}.
	 */
	public void loadJails() {
		switch(storage) {
			case 1:
				//load the jails from sqlite
				break;
			case 2:
				//load the jails from mysql
				pl.debug("Time Now (str): " + System.currentTimeMillis());
				
				try {
					if(con == null) this.prepareStorage(false);
					PreparedStatement ps = con.prepareStatement("SELECT * FROM " + prefix + "jails");
					ResultSet set = ps.executeQuery();
					
					while(set.next()) {
						Jail j = new Jail(pl, set.getString("name"));
						
						j.setWorld(set.getString("world"));
						j.setMaxPoint(new int[] { set.getInt("top.x"), set.getInt("top.y"), set.getInt("top.z") });
						j.setMinPoint(new int[] { set.getInt("bottom.x"), set.getInt("bottom.y"), set.getInt("bottom.z") });
						j.setTeleportIn(new SimpleLocation(j.getWorldName(), set.getDouble("tps.in.x"),
								set.getDouble("tps.in.y"), set.getDouble("tps.in.z"),
								set.getFloat("tps.in.yaw"), set.getFloat("tps.in.pitch")));
						j.setTeleportFree(new SimpleLocation(set.getString("tps.free.world"), set.getDouble("tps.free.x"),
								set.getDouble("tps.free.y"), set.getDouble("tps.free.z"),
								set.getFloat("tps.free.yaw"), set.getFloat("tps.free.pitch")));
						pl.getJailManager().addJail(j, false);
					}
					
					set.close();
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while loading the jails, please check the error and fix what is wrong.");
				}
				
				try {
					if(con == null) this.prepareStorage(false);
					PreparedStatement ps = con.prepareStatement("SELECT * FROM " + prefix + "cells");
					ResultSet set = ps.executeQuery();
					
					List<Integer> remove = new LinkedList<Integer>(); //TODO: Set up something to remove these
					
					while(set.next()) {
						Jail j = pl.getJailManager().getJail(set.getString("jail"));
						
						if(j != null) {
							Cell c = new Cell(set.getString("name"));
							c.setTeleport(new SimpleLocation(j.getWorldName(),  set.getDouble("tp.x"),
									set.getDouble("tp.y"), set.getDouble("tp.z"),
									set.getFloat("tp.yaw"), set.getFloat("tp.pitch")));
							
							//TODO: Finish loading the cells and fix the sql requiring everything, whoops
							
							j.addCell(c, false);
						}else {
							remove.add(set.getInt("cellid"));
						}
					}
					
					set.close();
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while loading all of the cells, please check the error and fix what is wrong.");
				}
				
				pl.debug("Time Now (end): " + System.currentTimeMillis());
				break;
			default:
				//load the jails from flatfile
				if(flat.isConfigurationSection("jails")) {
					Set<String> jails = flat.getConfigurationSection("jails").getKeys(false);
					if(!jails.isEmpty()) {
						for(String name : jails) {
							loadJailFromFlatFile(name);
						}
					}
				}
				break;
		}
		
		int s = pl.getJailManager().getJails().size();
		pl.getLogger().info("Loaded " + s + (s == 1 ? " jail." : " jails."));
	}
	
	/**
	 * Saves the provided {@link Jail jail} to the storage system we are using.
	 * 
	 * @param j The jail to save.
	 */
	public void saveJail(Jail j) {
		switch(storage) {
			case 1:
			case 2:
				try {
					pl.debug("Starting at: " + System.currentTimeMillis());
					
					if(con == null) this.prepareStorage(false);
					PreparedStatement ps = con.prepareStatement("REPLACE INTO "
							+ prefix + "jails (`name`, `world`, `top.x`, `top.y`, `top.z`, `bottom.x`, `bottom.y`,"
							+ "`bottom.z`, `tps.in.x`, `tps.in.y`, `tps.in.z`, `tps.in.yaw`, `tps.in.pitch`,"
							+ "`tps.free.world`, `tps.free.x`, `tps.free.y`, `tps.free.z`, `tps.free.yaw`, `tps.free.pitch`)"
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					
					ps.setString(1, j.getName());
					ps.setString(2, j.getWorldName());
					ps.setInt(3, j.getMaxPoint().getBlockX());
					ps.setInt(4, j.getMaxPoint().getBlockY());
					ps.setInt(5, j.getMaxPoint().getBlockZ());
					ps.setInt(6, j.getMinPoint().getBlockX());
					ps.setInt(7, j.getMinPoint().getBlockY());
					ps.setInt(8, j.getMinPoint().getBlockZ());
					ps.setDouble(9, j.getTeleportIn().getX());
					ps.setDouble(10, j.getTeleportIn().getY());
					ps.setDouble(11, j.getTeleportIn().getZ());
					ps.setDouble(12, j.getTeleportIn().getYaw());
					ps.setDouble(13, j.getTeleportIn().getPitch());
					ps.setString(14, j.getTeleportFree().getWorld().getName());
					ps.setDouble(15, j.getTeleportFree().getX());
					ps.setDouble(16, j.getTeleportFree().getY());
					ps.setDouble(17, j.getTeleportFree().getZ());
					ps.setDouble(18, j.getTeleportFree().getYaw());
					ps.setDouble(19, j.getTeleportFree().getPitch());
					
					ps.executeUpdate();
					ps.close();
					con.commit();
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while saving the Jail '" + j.getName() + "' (not updating the prisoners), please check the error and fix what is wrong.");
				}
				
				try {
					if(con == null) this.prepareStorage(false);
					
					for(Cell c : j.getCells()) {
						if(c.hasPrisoner()) {
							Prisoner p = c.getPrisoner();
							PreparedStatement pPS = con.prepareStatement("REPLACE INTO `" + prefix + "prisoners` (`name`, `jail`, `cell`, `muted`, `time`,"
									+ "`offlinePending`, `toBeTransferred`, `jailer`, `reason`, `inventory`, `armor`, `previousLocation`, `previousGameMode`)"
									+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
							pPS.setString(1, p.getName());
							pPS.setString(2, j.getName());
							pPS.setString(3, c.getName());
							pPS.setBoolean(4, p.isMuted());
							pPS.setFloat(5, p.getRemainingTime());
							pPS.setBoolean(6, p.isOfflinePending());
							pPS.setBoolean(7, p.isToBeTransferred());
							pPS.setString(8, p.getReason());
							pPS.setBytes(9, p.getInventory().getBytes());
							pPS.setBytes(10, p.getArmor().getBytes());
							pPS.setString(11, p.getPreviousLocationString());
							pPS.setString(12, p.getPreviousGameMode().toString());
							
							pPS.executeUpdate();
							pPS.close();
						}
					}
					
					con.commit();
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while saving the cells of the Jail '" + j.getName() + "', please check the error and fix what is wrong.");
				}
				
				try {
					if(con == null) this.prepareStorage(false);
					
					for(Prisoner p : j.getPrisonersNotInCells()) {
						PreparedStatement pPS = con.prepareStatement("REPLACE INTO `" + prefix + "prisoners` (`name`, `jail`, `cell`, `muted`, `time`,"
								+ "`offlinePending`, `toBeTransferred`, `jailer`, `reason`, `inventory`, `armor`, `previousLocation`, `previousGameMode`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
						pPS.setString(1, p.getName());
						pPS.setString(2, j.getName());
						pPS.setString(3, "");
						pPS.setBoolean(4, p.isMuted());
						pPS.setFloat(5, p.getRemainingTime());
						pPS.setBoolean(6, p.isOfflinePending());
						pPS.setBoolean(7, p.isToBeTransferred());
						pPS.setString(8, p.getJailer());
						pPS.setString(9, p.getReason());
						pPS.setBytes(10, p.getInventory().getBytes());
						pPS.setBytes(11, p.getArmor().getBytes());
						pPS.setString(12, p.getPreviousLocationString());
						pPS.setString(13, p.getPreviousGameMode().toString());
						
						pPS.executeUpdate();
						pPS.close();
					}
					
					con.commit();
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while saving the prisoners of the Jail '" + j.getName() + "', please check the error and fix what is wrong.");
				}
				
				pl.debug("Ending at: " + System.currentTimeMillis());
				break;
			default:
				if(flat != null) {
					String node = "jails." + j.getName() + ".";
					
					//Corners
					flat.set(node + "world", j.getWorldName());
					flat.set(node + "top.x", j.getMaxPoint().getBlockX());
					flat.set(node + "top.y", j.getMaxPoint().getBlockY());
					flat.set(node + "top.z", j.getMaxPoint().getBlockZ());
					flat.set(node + "bottom.x", j.getMinPoint().getBlockX());
					flat.set(node + "bottom.y", j.getMinPoint().getBlockY());
					flat.set(node + "bottom.z", j.getMinPoint().getBlockZ());
					
					//Tele in
					flat.set(node + "tps.in.x", j.getTeleportIn().getX());
					flat.set(node + "tps.in.y", j.getTeleportIn().getY());
					flat.set(node + "tps.in.z", j.getTeleportIn().getZ());
					flat.set(node + "tps.in.yaw", j.getTeleportIn().getYaw());
					flat.set(node + "tps.in.pitch", j.getTeleportIn().getPitch());
					
					//Tele out
					flat.set(node + "tps.free.world", j.getTeleportFree().getWorld().getName());
					flat.set(node + "tps.free.x", j.getTeleportFree().getX());
					flat.set(node + "tps.free.y", j.getTeleportFree().getY());
					flat.set(node + "tps.free.z", j.getTeleportFree().getZ());
					flat.set(node + "tps.free.yaw", j.getTeleportFree().getYaw());
					flat.set(node + "tps.free.pitch", j.getTeleportFree().getPitch());
					
					//Set all the cells to nothing, then we save each of them so no cells are left behind
					flat.set(node + ".cells", null);
					for(Cell c : j.getCells()) {
						String cNode = node + ".cells." + c.getName() + ".";
						
						if(c.getTeleport() != null) {
							flat.set(cNode + "tp.x", c.getTeleport().getX());
							flat.set(cNode + "tp.y", c.getTeleport().getY());
							flat.set(cNode + "tp.z", c.getTeleport().getZ());
							flat.set(cNode + "tp.yaw", c.getTeleport().getYaw());
							flat.set(cNode + "tp.pitch", c.getTeleport().getPitch());
						}
						
						if(c.getChestLocation() != null) {
							flat.set(cNode + "chest.x", c.getChestLocation().getBlockX());
							flat.set(cNode + "chest.y", c.getChestLocation().getBlockY());
							flat.set(cNode + "chest.z", c.getChestLocation().getBlockZ());
						}
						
						String[] signs = new String[c.getSigns().size()];
						int count = 0;
						for(SimpleLocation loc : c.getSigns()) {
							signs[count] = loc.toString();
							count++;
						}
						
						flat.set(cNode + "signs", signs);
						
						if(c.getPrisoner() != null) {
							Prisoner p = c.getPrisoner();
							flat.set(cNode + "prisoner.name", p.getName());
							flat.set(cNode + "prisoner.muted", p.isMuted());
							flat.set(cNode + "prisoner.time", p.getRemainingTime());
							flat.set(cNode + "prisoner.offlinePending", p.isOfflinePending());
							flat.set(cNode + "prisoner.toBeTransferred", p.isToBeTransferred());
							flat.set(cNode + "prisoner.jailer", p.getJailer());
							flat.set(cNode + "prisoner.reason", p.getReason());
							flat.set(cNode + "prisoner.inventory", p.getInventory());
							flat.set(cNode + "prisoner.armor", p.getArmor());
							if(p.getPreviousLocationString() != null)
								flat.set(cNode + "prisoner.previousLocation", p.getPreviousLocationString());
							if(p.getPreviousGameMode() != null)
								flat.set(cNode + "prisoner.previousGameMode", p.getPreviousGameMode().toString());
						}
					}
					
					//Null all the prisoners out before we save them again, this way no prisoners are left behind
					flat.set(node + "prisoners", null);
					for(Prisoner p : j.getPrisonersNotInCells()) {
						String pNode = node + "prisoners." + p.getName() + ".";
						flat.set(pNode + "muted", p.isMuted());
						flat.set(pNode + "time", p.getRemainingTime());
						flat.set(pNode + "offlinePending", p.isOfflinePending());
						flat.set(pNode + "toBeTransferred", p.isToBeTransferred());
						flat.set(pNode + "jailer", p.getJailer());
						flat.set(pNode + "reason", p.getReason());
						flat.set(pNode + "inventory", p.getInventory());
						flat.set(pNode + "armor", p.getArmor());
						if(p.getPreviousLocationString() != null)
							flat.set(pNode + "previousLocation", p.getPreviousLocationString());
						if(p.getPreviousGameMode() != null)
							flat.set(pNode + "previousGameMode", p.getPreviousGameMode().toString());
					}
					
					try {
						flat.save(new File(pl.getDataFolder(), "data.yml"));
					} catch (IOException e) {
						pl.getLogger().severe("Unable to save the Jail data: " + e.getMessage());
					}
				}else {
					pl.getLogger().severe("Storage not enabled, could not save the jail " + j.getName());
				}
				break;
		}
	}
	
	private void loadJailFromFlatFile(String name) {
		String node = "jails." + name + ".";
		String cNode = node + "cells.";
		Jail j = new Jail(pl, name);
		
		j.setWorld(flat.getString(node + "world"));
		j.setMaxPoint(new int[] {flat.getInt(node + "top.x"), flat.getInt(node + "top.y"), flat.getInt(node + "top.z")});
		j.setMinPoint(new int[] {flat.getInt(node + "bottom.x"), flat.getInt(node + "bottom.y"), flat.getInt(node + "bottom.z")});
		
		j.setTeleportIn(new SimpleLocation(
				flat.getString(node + "world"),
				flat.getDouble(node + "tps.in.x"),
				flat.getDouble(node + "tps.in.y"),
				flat.getDouble(node + "tps.in.z"),
				(float) flat.getDouble(node + "tps.in.yaw"),
				(float) flat.getDouble(node + "tps.in.pitch")));
		j.setTeleportFree(new SimpleLocation(
				flat.getString(node + "tps.free.world"),
				flat.getDouble(node + "tps.free.x"),
				flat.getDouble(node + "tps.free.y"),
				flat.getDouble(node + "tps.free.z"),
				(float) flat.getDouble(node + "tps.free.yaw"),
				(float) flat.getDouble(node + "tps.free.pitch")));
		
		if(flat.isConfigurationSection(node + "cells")) {
			Set<String> cells = flat.getConfigurationSection(node + "cells").getKeys(false);
			if(!cells.isEmpty()) {
				for(String cell : cells) {
					Cell c = new Cell(cell);
					String cellNode = cNode + cell + ".";
					
					c.setTeleport(new SimpleLocation(j.getTeleportIn().getWorld().getName(),
							flat.getDouble(cellNode + "tp.x"),
							flat.getDouble(cellNode + "tp.y"),
							flat.getDouble(cellNode + "tp.z"),
							(float) flat.getDouble(cellNode + "tp.yaw"),
							(float) flat.getDouble(cellNode + "tp.pitch")));
					c.setChestLocation(new Location(j.getTeleportIn().getWorld(),
							flat.getInt(cellNode + "chest.x"),
							flat.getInt(cellNode + "chest.y"),
							flat.getInt(cellNode + "chest.z")));
					
					for(String sign : flat.getStringList(cellNode + "signs")) {
						String[] arr = sign.split(",");
						c.addSign(new SimpleLocation(arr[0],
								Double.valueOf(arr[1]),
								Double.valueOf(arr[2]),
								Double.valueOf(arr[3]),
								Float.valueOf(arr[4]),
								Float.valueOf(arr[5])));
					}
					
					if(flat.contains(cellNode + "prisoner")) {
						Prisoner p = new Prisoner(flat.getString(cellNode + "prisoner.name"),
										flat.getBoolean(cellNode + "prisoner.muted"),
										flat.getLong(cellNode + "prisoner.time"),
										flat.getString(cellNode + "prisoner.jailer"),
										flat.getString(cellNode + "prisoner.reason"));
						p.setOfflinePending(flat.getBoolean(cellNode + "prisoner.offlinePending"));
						p.setToBeTransferred(flat.getBoolean(cellNode + "prisoner.toBeTransferred"));
						p.setPreviousPosition(flat.getString(cellNode + "prisoner.previousLocation"));
						p.setPreviousGameMode(flat.getString(cellNode + "prisoner.previousGameMode"));
						p.setInventory(flat.getString(cellNode + "prisoner.inventory", ""));
						p.setArmor(flat.getString(cellNode + "prisoner.armor", ""));
						c.setPrisoner(p);
					}
					
					j.addCell(c, false);
				}
			}
		}
		
		if(flat.isConfigurationSection(node + "prisoners")) {
			Set<String> prisoners = flat.getConfigurationSection(node + "prisoners").getKeys(false);
			if(!prisoners.isEmpty()) {
				for(String prisoner : prisoners) {
					String pNode = node + "prisoners." + prisoner + ".";
					Prisoner pris = new Prisoner(prisoner,
							flat.getBoolean(pNode + "muted"),
							flat.getLong(pNode + "time"),
							flat.getString(pNode + "jailer"),
							flat.getString(pNode + "reason"));
					pris.setOfflinePending(flat.getBoolean(pNode + "offlinePending"));
					pris.setToBeTransferred(flat.getBoolean(pNode + "toBeTransferred"));
					pris.setPreviousPosition(flat.getString(pNode + "previousLocation"));
					pris.setPreviousGameMode(flat.getString(pNode + "previousGameMode"));
					pris.setInventory(flat.getString(pNode + "inventory", ""));
					pris.setArmor(flat.getString(pNode + "armor", ""));
					j.addPrisoner(pris);
				}
			}
		}
		
		if(pl.getServer().getWorld(j.getWorldName()) != null) {
			pl.getJailManager().addJail(j, false);
			pl.getLogger().info("Loaded jail " + j.getName() + " with " + j.getAllPrisoners().size() + " prisoners and " + j.getCellCount() + " cells.");
		} else
			pl.getLogger().severe("Failed to load the jail " + j.getName() + " as the world '" + j.getWorldName() + "' does not exist (is null). Did you remove this world?");
	}
	
	public void saveCell(Jail j, Cell c) {
		switch(storage) {
			case 1:
			case 2:
				try {
					if(con == null) this.prepareStorage(false);
					
					PreparedStatement cPS = con.prepareStatement("INSERT INTO `" + prefix + "cells` (`name`, `jail`, `tp.x`, `tp.y`, `tp.z`, `tp.yaw`,"
							+ "`tp.pitch`, `chest.x`, `chest.y`, `chest.z`, `signs`) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
					
					cPS.setString(1, c.getName());
					cPS.setString(2, j.getName());
					cPS.setDouble(3, c.getTeleport().getX());
					cPS.setDouble(4, c.getTeleport().getY());
					cPS.setDouble(5, c.getTeleport().getZ());
					cPS.setDouble(6, c.getTeleport().getYaw());
					cPS.setDouble(7, c.getTeleport().getPitch());
					
					if(c.hasChest()) {
						cPS.setInt(8, c.getChestLocation().getBlockX());
						cPS.setInt(9, c.getChestLocation().getBlockY());
						cPS.setInt(10, c.getChestLocation().getBlockZ());
					}else {
						cPS.setNull(8, java.sql.Types.INTEGER);
						cPS.setNull(9, java.sql.Types.INTEGER);
						cPS.setNull(10, java.sql.Types.INTEGER);
					}
					
					cPS.setString(11, c.getSignString());
					
					cPS.executeUpdate();
					cPS.close();
					
					if(c.hasPrisoner()) {
						Prisoner p = c.getPrisoner();
						PreparedStatement pPS = con.prepareStatement("REPLACE INTO `" + prefix + "prisoners` (`name`, `jail`, `cell`, `muted`, `time`,"
								+ "`offlinePending`, `toBeTransferred`, `jailer`, `reason`, `inventory`, `armor`, `previousLocation`, `previousGameMode`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
						pPS.setString(1, p.getName());
						pPS.setString(2, j.getName());
						pPS.setString(3, c.getName());
						pPS.setBoolean(4, p.isMuted());
						pPS.setFloat(5, p.getRemainingTime());
						pPS.setBoolean(6, p.isOfflinePending());
						pPS.setBoolean(7, p.isToBeTransferred());
						pPS.setString(8, p.getReason());
						pPS.setBytes(9, p.getInventory().getBytes());
						pPS.setBytes(10, p.getArmor().getBytes());
						pPS.setString(11, p.getPreviousLocationString());
						pPS.setString(12, p.getPreviousGameMode().toString());
						
						pPS.executeUpdate();
						pPS.close();
					}
					
					con.commit();
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while saving the cell '" + c.getName() + "' of the Jail '" + j.getName() + "', please check the error and fix what is wrong.");
				}
				
				break;
			default:
				this.saveJail(j);
				break;
		}
	}
	
	/**
	 * Removes the prisoner from the storage system.
	 * 
	 * @param j the jail which the prisoner is in.
	 * @param p the prisoner data
	 */
	public void removePrisoner(Jail j, Prisoner p) {
		this.removePrisoner(j, null, p);
	}
	
	/**
	 * Removes the prisoner from the storage system.
	 * 
	 * @param j the jail which the prisoner is in.
	 * @param c the cell which the prisoner is in, null if none
	 * @param p the prisoner data
	 */
	public void removePrisoner(Jail j, Cell c, Prisoner p) {
		switch(storage) {
			case 1:
			case 2:
				break;
			default:
				if(c == null)
					flat.set("jails." + j.getName() + ".prisoners." + p.getName(), null);
				else
					flat.set("jails." + j.getName() + "." + c.getName() + ".prisoner", null);
				
				try {
					flat.save(new File(pl.getDataFolder(), "data.yml"));
				} catch (IOException e) {
					pl.getLogger().severe("Unable to save the Jail data: " + e.getMessage());
				}
				break;
		}
	}
	
	/**
	 * Removes a jail from the storage system.
	 * 
	 * @param name of the jail to remove.
	 */
	public void removeJail(String name) {
		switch(storage) {
			case 1:
			case 2:
				break;
			default:
				flat.set("jails." + name, null);
				
				try {
					flat.save(new File(pl.getDataFolder(), "data.yml"));
				} catch (IOException e) {
					pl.getLogger().severe("Unable to remove the jail " + name +  " from the storage: " + e.getMessage());
				}
				break;
		}
	}
}
