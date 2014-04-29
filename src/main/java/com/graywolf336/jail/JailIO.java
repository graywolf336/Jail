package com.graywolf336.jail;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
	private FileConfiguration flat, lang, records;
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
		if(!pl.inDebug()) pl.getLogger().info("Storage type selected: " + st);
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
					Connection sqliteConnection = DriverManager.getConnection("jdbc:sqlite:" +  new File(pl.getDataFolder().getPath(), "jail3.sqlite").getPath());
					sqliteConnection.setAutoCommit(true);
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
					mysqlConnection.setAutoCommit(true);
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
				records = YamlConfiguration.loadConfiguration(new File(pl.getDataFolder(), "records.yml"));
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
				try {
					if(!con.isValid(10)) this.prepareStorage(false);
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Unable to get a Sql connection, please see the error above and fix the problem.");
					return null;
				}
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
					//TODO: yeah big time!
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
					
					//pl.debug(jailCreateCmd);
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
					
					//pl.debug(cellCreateCmd);
					st.executeUpdate(cellCreateCmd);
					
					String prisCreateCmd = "CREATE TABLE IF NOT EXISTS `" + prefix + "prisoners` ("
							  + "`uuid` VARCHAR(36) NOT NULL COMMENT 'The UUID of the prisoner.',"
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
							  + "PRIMARY KEY (`uuid`),"
							  + "UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC))"
							  + "COMMENT = 'Contains all the prisoners, in cells and jails.';";
					
					//pl.debug(prisCreateCmd);
					st.executeUpdate(prisCreateCmd);
					
					String proCreateCmd = "CREATE TABLE IF NOT EXISTS `" + prefix + "records` ("
							  + "`recordid` INT NOT NULL AUTO_INCREMENT COMMENT 'Auto generated number for the records database.',"
							  + "`uuid` VARCHAR(36) NOT NULL COMMENT 'The UUID of the prisoner.',"
							  + "`username` VARCHAR(16) NOT NULL COMMENT 'The username of the prisoner.',"
							  + "`jailer` VARCHAR(250) NOT NULL COMMENT 'The name of the person who jailed the prisoner.',"
							  + "`date` VARCHAR(32) NOT NULL COMMENT 'A string of the date.',"
							  + "`time` INT NOT NULL COMMENT 'The milliseconds they were jailed for.',"
							  + "`reason` VARCHAR(250) NOT NULL COMMENT 'The reason they were jailed for.',"
							  + "PRIMARY KEY (`recordid`),"
							  + "UNIQUE INDEX `recordid_UNIQUE` (`recordid` ASC))"
							  + "COMMENT = 'Holds a history of all the times prisoners have been jailed.'";
					
					//pl.debug(proCreateCmd);
					st.executeUpdate(proCreateCmd);
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
	 * Loads the jails, this should <strong>only</strong> be called after {@link #prepareStorage(boolean)}.
	 */
	public void loadJails() {
		switch(storage) {
			case 1:
			case 2:
				//load the jails from mysql and sqlite
				long st = System.currentTimeMillis();
				
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
				
				//This list contains an integer which refers to the cellid column in sql
				//this list only gets populated if there are cells which reference a jail
				//that doesn't exist anymore
				List<Integer> cellsToRemove = new LinkedList<Integer>();
				
				int cs = 0;
				try {
					if(con == null) this.prepareStorage(false);
					PreparedStatement ps = con.prepareStatement("SELECT * FROM " + prefix + "cells");
					ResultSet set = ps.executeQuery();
					
					while(set.next()) {
						Jail j = pl.getJailManager().getJail(set.getString("jail"));
						
						if(j != null) {
							Cell c = new Cell(set.getString("name"));
							c.setTeleport(new SimpleLocation(j.getWorldName(),  set.getDouble("tp.x"), set.getDouble("tp.y"), set.getDouble("tp.z"),
									set.getFloat("tp.yaw"), set.getFloat("tp.pitch")));
							
							c.setChestLocation(new Location(j.getWorld(), set.getInt("chest.x"), set.getInt("chest.y"), set.getInt("chest.z")));
							
							String cSigns = set.getString("signs");
							if(!cSigns.isEmpty()) {
								String[] signs = cSigns.split(";");
								for(String s : signs) {
									String[] co = s.split(",");
									c.addSign(new SimpleLocation(co[0], co[1], co[2], co[3]));
								}
							}
							
							
							j.addCell(c, false);
							cs++;
						}else {
							cellsToRemove.add(set.getInt("cellid"));
						}
					}
					
					pl.debug("There are " + cellsToRemove.size() + " cells we need to remove due to being invalid.");
					
					set.close();
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while loading all of the cells, please check the error and fix what is wrong.");
				}
				
				//Remove the invalid prisoners
				if(cellsToRemove.size() != 0) {
					String names = "";
					for(int c : cellsToRemove) {
						if(names.isEmpty()) names = "'" + c + "'";
						else names += "," + "'" + c + "'";
					}
					
					try {
						PreparedStatement cds = con.prepareStatement("delete from " + prefix + "cells where cellid in (" + names + ");");
						
						pl.debug("Deleting old cells: 'delete from " + prefix + "cells where cellid in (" + names + ");'");
						
						int count = cds.executeUpdate();
						pl.getLogger().info("Deleted " + count + " old cells which referenced a jail no longer valid: " + names);
						cds.close();
					} catch (SQLException e) {
						e.printStackTrace();
						pl.getLogger().severe("---------- Jail Error!!! ----------");
						pl.getLogger().severe("Error while deleting the old cells which don't have a valid jail, please check the error and fix what is wrong.");
					}
				}
				
				pl.getLogger().info("Loaded " + cs + (cs == 1 ? " cell." : " cells."));
				
				//This list contains a string which refers to the name of the prisoner in sql
				//this list only gets populated if there are prisoners which reference a jail
				//that doesn't exist anymore
				List<String> prisonersToRemove = new LinkedList<String>();
				
				int pc = 0;
				try {
					if(con == null) this.prepareStorage(false);
					PreparedStatement ps = con.prepareStatement("SELECT * FROM " + prefix + "prisoners");
					ResultSet set = ps.executeQuery();
					
					while(set.next()) {
						Jail j = pl.getJailManager().getJail(set.getString("jail"));
						
						if(j != null) {
							String cellname = set.getString("cell");
							Cell c = j.getCell(cellname);
							
							Prisoner p = new Prisoner(set.getString("uuid"), set.getString("name"), set.getBoolean("muted"), set.getLong("time"), set.getString("jailer"), set.getString("reason"));
							p.setOfflinePending(set.getBoolean("offlinePending"));
							p.setToBeTransferred(set.getBoolean("toBeTransferred"));
							Blob inv = set.getBlob("inventory");
							p.setInventory(new String(inv.getBytes(1, (int) inv.length())));
							Blob ar = set.getBlob("armor");
							p.setArmor(new String(ar.getBytes(1, (int)ar.length())));
							p.setPreviousPosition(set.getString("previousLocation"));
							p.setPreviousGameMode(set.getString("previousGameMode"));
							
							if(cellname == null || cellname.isEmpty()) {
								j.addPrisoner(p);
							}else if(c != null) {
								c.setPrisoner(p);
							}else {
								//the prisoner is assigned to a cell which doesn't exist, so just put them into the jail
								j.addPrisoner(p);
							}
							
							pc++;
						} else {
							//if the jail doesn't exist, do the same as the cells
							prisonersToRemove.add(set.getString("name"));
						}
					}
					
					set.close();
					ps.close();
					
					pl.debug("There are " + prisonersToRemove.size() + " prisoners we need to remove due to being invalid.");
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while loading all of the prisoners, please check the error and fix what is wrong.");
				}
				
				//Remove the invalid prisoners
				if(prisonersToRemove.size() != 0) {
					String names = "";
					for(String s : prisonersToRemove) {
						if(names.isEmpty()) names = "'" + s + "'";
						else names += "," + "'" + s + "'";
					}
					
					try {
						PreparedStatement pds = con.prepareStatement("delete from " + prefix + "prisoners where name in (" + names + ");");
						
						pl.debug("Deleting old prisoners: 'delete from " + prefix + "prisoners where name in (" + names + ");'");
						
						int count = pds.executeUpdate();
						pl.getLogger().info("Deleted " + count + " old prisoners which referenced a jail no longer valid: " + names);
						pds.close();
					} catch (SQLException e) {
						e.printStackTrace();
						pl.getLogger().severe("---------- Jail Error!!! ----------");
						pl.getLogger().severe("Error while deleting the old prisoners which don't have a valid jail, please check the error and fix what is wrong.");
					}
				}
				
				pl.getLogger().info("Loaded " + pc + (pc == 1 ? " prisoner." : " prisoners."));
				
				pl.debug("Took " + (System.currentTimeMillis() - st) + " millis.");
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
		
		int js = pl.getJailManager().getJails().size();
		pl.getLogger().info("Loaded " + js + (js == 1 ? " jail." : " jails."));
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
						Prisoner p = new Prisoner(flat.getString(cellNode + "prisoner.uuid"),
										flat.getString(cellNode + "prisoner.name"),
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
				for(String uuid : prisoners) {
					String pNode = node + "prisoners." + uuid + ".";
					Prisoner pris = new Prisoner(uuid,
							flat.getString(pNode + "prisoner"),
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
	
	/**
	 * Saves the provided {@link Jail jail} to the storage system we are using.
	 * 
	 * @param j The jail to save.
	 */
	public void saveJail(Jail j) {
		switch(storage) {
			case 1:
			case 2:
				long st = System.currentTimeMillis();
				
				try {
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
							PreparedStatement pPS = con.prepareStatement("REPLACE INTO `" + prefix + "prisoners` (`uuid`, `name`, `jail`, `cell`, `muted`, `time`,"
									+ "`offlinePending`, `toBeTransferred`, `jailer`, `reason`, `inventory`, `armor`, `previousLocation`, `previousGameMode`)"
									+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
							pPS.setString(1, p.getUUID().toString());
							pPS.setString(2, p.getName());
							pPS.setString(3, j.getName());
							pPS.setString(4, c.getName());
							pPS.setBoolean(5, p.isMuted());
							pPS.setFloat(6, p.getRemainingTime());
							pPS.setBoolean(7, p.isOfflinePending());
							pPS.setBoolean(8, p.isToBeTransferred());
							pPS.setString(9, p.getJailer());
							pPS.setString(10, p.getReason());
							pPS.setBytes(11, p.getInventory().getBytes());
							pPS.setBytes(12, p.getArmor().getBytes());
							pPS.setString(13, p.getPreviousLocationString());
							pPS.setString(14, p.getPreviousGameMode().toString());
							
							pPS.executeUpdate();
							pPS.close();
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while saving the cells of the Jail '" + j.getName() + "', please check the error and fix what is wrong.");
				}
				
				try {
					if(con == null) this.prepareStorage(false);
					
					for(Prisoner p : j.getPrisonersNotInCells()) {
						PreparedStatement pPS = con.prepareStatement("REPLACE INTO `" + prefix + "prisoners` (`uuid`, `name`, `jail`, `cell`, `muted`, `time`,"
								+ "`offlinePending`, `toBeTransferred`, `jailer`, `reason`, `inventory`, `armor`, `previousLocation`, `previousGameMode`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
						pPS.setString(1, p.getUUID().toString());
						pPS.setString(2, p.getName());
						pPS.setString(3, j.getName());
						pPS.setString(4, "");
						pPS.setBoolean(5, p.isMuted());
						pPS.setFloat(6, p.getRemainingTime());
						pPS.setBoolean(7, p.isOfflinePending());
						pPS.setBoolean(8, p.isToBeTransferred());
						pPS.setString(9, p.getJailer());
						pPS.setString(10, p.getReason());
						pPS.setBytes(11, p.getInventory().getBytes());
						pPS.setBytes(12, p.getArmor().getBytes());
						pPS.setString(13, p.getPreviousLocationString());
						pPS.setString(14, p.getPreviousGameMode().toString());
						
						pPS.executeUpdate();
						pPS.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while saving the prisoners of the Jail '" + j.getName() + "', please check the error and fix what is wrong.");
				}
				
				pl.debug("Took " + (System.currentTimeMillis() - st) + " millis.");
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
					flat.set(node + "cells", null);
					for(Cell c : j.getCells()) {
						String cNode = node + "cells." + c.getName() + ".";
						
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
							flat.set(cNode + "prisoner.uuid", p.getUUID().toString());
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
						String pNode = node + "prisoners." + p.getUUID().toString() + ".";
						flat.set(pNode + "name", p.getName());
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
						PreparedStatement pPS = con.prepareStatement("REPLACE INTO `" + prefix + "prisoners` (`uuid`, `name`, `jail`, `cell`, `muted`, `time`,"
								+ "`offlinePending`, `toBeTransferred`, `jailer`, `reason`, `inventory`, `armor`, `previousLocation`, `previousGameMode`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
						pPS.setString(1, p.getUUID().toString());
						pPS.setString(2, p.getName());
						pPS.setString(3, j.getName());
						pPS.setString(4, c.getName());
						pPS.setBoolean(5, p.isMuted());
						pPS.setFloat(6, p.getRemainingTime());
						pPS.setBoolean(7, p.isOfflinePending());
						pPS.setBoolean(8, p.isToBeTransferred());
						pPS.setString(9, p.getReason());
						pPS.setBytes(10, p.getInventory().getBytes());
						pPS.setBytes(11, p.getArmor().getBytes());
						pPS.setString(12, p.getPreviousLocationString());
						pPS.setString(13, p.getPreviousGameMode().toString());
						
						pPS.executeUpdate();
						pPS.close();
					}
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
				try {
					PreparedStatement pp = con.prepareStatement("delete from `" + prefix + "prisoners` where uuid = ? limit 1;");
					pp.setString(1, p.getUUID().toString());
					
					pl.debug("Removing " + p.getName() + " from MySQL database.");
					
					pp.executeUpdate();
					pp.close();
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while removing the prisoner '" + p.getName() + "' from the database, please check the error and fix what is wrong.");
				}
				break;
			default:
				if(c == null)
					flat.set("jails." + j.getName() + ".prisoners." + p.getUUID().toString(), null);
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
	 * Removes the provided cell from the jail.
	 * 
	 * @param j instance of the jail the cell is in
	 * @param c instance of the cell we are removing
	 */
	public void removeCell(Jail j, Cell c) {
		//Clear the inventory before we delete it
		if(c.hasChest()) c.getChest().getInventory().clear();
		
		//transfer the prisoner if it has one
		if(c.hasPrisoner()) {
			pl.getLogger().warning("Removing of cell '" + c.getName() + "' from the jail '" + j.getName() + "' failed as it has a prisoner.");
			return;
		}
		
		switch(storage) {
			case 1:
			case 2:
				try {
					PreparedStatement p = con.prepareStatement("delete from `" + prefix + "cells` where name = ? and jail = ? limit 1;");
					p.setString(1, c.getName());
					p.setString(2, j.getName());
					
					p.executeUpdate();
					p.close();
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while removing the cell '" + c.getName() + "' from the Jail '" + j.getName() + "', please check the error and fix what is wrong.");
				}
				break;
			default:
				if(flat != null) {
					flat.set("jails." + j.getName() + "cells." + c.getName(), null);
					
					try {
						flat.save(new File(pl.getDataFolder(), "data.yml"));
					} catch (IOException e) {
						e.printStackTrace();
						pl.getLogger().severe("---------- Jail Error!!! ----------");
						pl.getLogger().severe("Removing of the cell '" + c.getName() + "' from the jail '" + j.getName() + "' errored out while on saving.");
					}
				}
				break;
		}
	}
	
	/**
	 * Removes a jail from the storage system.
	 * 
	 * @param j the jail instance to remove.
	 */
	public void removeJail(Jail j) {
		String name = j.getName();
		
		switch(storage) {
			case 1:
			case 2:
				for(Cell c : j.getCells()) {
					removeCell(j, c);
				}
				
				try {
					PreparedStatement p = con.prepareStatement("delete from `" + prefix + "jails` where name = ?");
					p.setString(1, name);
					
					p.executeUpdate();
					p.close();
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while removing the Jail '" + j.getName() + "', please check the error and fix what is wrong.");
				}
				break;
			default:
				flat.set("jails." + name, null);
				
				try {
					flat.save(new File(pl.getDataFolder(), "data.yml"));
				} catch (IOException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Removing of the jail '" + j.getName() + "' errored out while on saving.");
				}
				break;
		}
	}
	
	/**
	 * Adds an entry to the database/file for the user, logging when they was jailed.
	 * 
	 * @param uuid of the player
	 * @param username of the player
	 * @param jailer who jailed them
	 * @param date string of when they are jailed
	 * @param time of the player's sentence
	 * @param reason the player is jailed
	 */
	public void addRecordEntry(String uuid, String username, String jailer, String date, long time, String reason) {
		switch(storage) {
			case 1:
				break;
			case 2:
				try {
					PreparedStatement p = con.prepareStatement("insert into `" + prefix + "records` (`uuid`, `username`, `jailer`, `date`, `time`,  `reason`) VALUES (?,?,?,?,?,?);");
					p.setString(1, uuid);
					p.setString(2, username);
					p.setString(3, jailer);
					p.setString(4, date);
					p.setLong(5, time);
					p.setString(6, reason);
					
					p.executeUpdate();
					p.close();
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while adding a record entry for '" + username + "', please check the error and fix what is wrong.");
				}
				break;
			default:
				if(records == null) records = YamlConfiguration.loadConfiguration(new File(pl.getDataFolder(), "records.yml"));
				
				List<String> previous = records.getStringList(uuid);
				previous.add(this.getLanguageString(LangString.RECORDENTRY, new String[] { date, username, jailer, String.valueOf(time), reason, uuid }));
				
				records.set(username, previous);
				
				try {
					records.save(new File(pl.getDataFolder(), "records.yml"));
				} catch (IOException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Saving the records.yml file failed while putting an entry in for '" + username + "'.");
				}
				break;
		}
	}
	
	/**
	 * Gets all the record entries for the given player.
	 * 
	 * @param username of the prisoner to get.
	 * @return A List of strings containing the record entries.
	 */
	public List<String> getRecordEntries(String uuid) {
		List<String> entries = new ArrayList<String>();
		
		switch(storage) {
			case 1:
				break;
			case 2:
				try {
					PreparedStatement ps = con.prepareStatement("SELECT * FROM " + prefix + "records where uuid = ?");
					ps.setString(1, uuid);
					ResultSet set = ps.executeQuery();
					
					while(set.next()) {
						entries.add(this.getLanguageString(LangString.RECORDENTRY,
								new String[] { set.getString("date"), set.getString("username"), set.getString("jailer"), String.valueOf(set.getLong("time")), set.getString("reason") }));
					}
					
					set.close();
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
					pl.getLogger().severe("---------- Jail Error!!! ----------");
					pl.getLogger().severe("Error while getting all the record entries for '" + uuid + "', please check the error and fix what is wrong.");
				}
				break;
			default:
				if(records == null) records = YamlConfiguration.loadConfiguration(new File(pl.getDataFolder(), "records.yml"));
				
				entries = records.getStringList(uuid);
				break;
		}
		
		return entries;
	}
}
