package com.graywolf336.jail.legacy;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.beans.Cell;
import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.beans.SimpleLocation;

public class OldInputOutput {
	private JailMain pl;
    private Connection connection;
    private YamlConfiguration global;
    
    public OldInputOutput(JailMain plugin, YamlConfiguration global) {
    	this.pl = plugin;
    	this.global = global;
    }
    
    public synchronized Connection getConnection() throws SQLException {
    	if (connection == null) connection = createConnection();
    	if(OldSettings.getGlobalBoolean(global, OldSetting.UseMySQL)) {
    		if(!connection.isValid(10)) connection = createConnection();
        }
    	return connection;
    }

    private Connection createConnection() {
        try {
            if (OldSettings.getGlobalBoolean(global, OldSetting.UseMySQL)) {
                Class.forName("com.mysql.jdbc.Driver");
                Connection ret = DriverManager.getConnection(OldSettings.getGlobalString(global, OldSetting.MySQLConn), OldSettings.getGlobalString(global, OldSetting.MySQLUsername), OldSettings.getGlobalString(global, OldSetting.MySQLPassword));
                ret.setAutoCommit(false);
                return ret;
            } else {
                Class.forName("org.sqlite.JDBC");
                Connection ret = DriverManager.getConnection("jdbc:sqlite:" +  new File(pl.getDataFolder().getPath(), "jail.sqlite").getPath());
                ret.setAutoCommit(false);
                return ret;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
   public synchronized void freeConnection() throws SQLException {
		Connection conn = getConnection();
        if(conn != null) {
            try {
            	conn.close();
            	conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void LoadJails() throws SQLException {
		PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM jail_zones");
		ResultSet set = ps.executeQuery();
		while (set.next()) {
			String name = set.getString("name").toLowerCase();
			double X1 = set.getDouble("X1");
			double Y1 = set.getDouble("Y1");
			double Z1 = set.getDouble("Z1");
			double X2 = set.getDouble("X2");
			double Y2 = set.getDouble("Y2");
			double Z2 = set.getDouble("Z2");
			double teleX = set.getDouble("teleX");
			double teleY = set.getDouble("teleY");
			double teleZ = set.getDouble("teleZ");
			double freeX = set.getDouble("freeX");
			double freeY = set.getDouble("freeY");
			double freeZ = set.getDouble("freeZ");
			String teleWorld = set.getString("teleWorld");
			String freeWorld = set.getString("freeWorld");
			
			Jail j = new Jail(pl, name);
			j.setWorld(teleWorld);
			j.setMaxPoint(new Location(pl.getServer().getWorld(teleWorld), X1, Y1, Z1));
			j.setMinPoint(new Location(pl.getServer().getWorld(teleWorld), X2, Y2, Z2));
			j.setTeleportIn(new SimpleLocation(teleWorld, teleX, teleY, teleZ));
			j.setTeleportFree(new SimpleLocation(freeWorld, freeX, freeY, freeZ));
			
			pl.getJailManager().addJail(j, false);
		}
		
		set.close();
		ps.close();
    }
    
    public void LoadPrisoners() throws SQLException {
    	if(pl.getJailManager().getJails().size() != 0) {
    		PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM jail_prisoners");
    		ResultSet set = ps.executeQuery();
    		while (set.next()) {
    			Jail j = pl.getJailManager().getJail(set.getString("JailName"));
    			String name = set.getString("PlayerName").toLowerCase();
    			
    			String transferDest = set.getString("TransferDest");
    			boolean transfer = false;
    			if(!transferDest.isEmpty()) {
    				j = pl.getJailManager().getJail(transferDest);
    				transfer = true;
    			}
    			
    			//In the event the jail is null (jail didn't transfer or the prisoner was jailed offline and no jail specified
    			if(j == null) {
    				j = pl.getJailManager().getJails().iterator().next();
    			}
    			
    			Prisoner p = new Prisoner(pl.getServer().getOfflinePlayer(name).getUniqueId().toString(), name, set.getBoolean("muted"), (long) set.getInt("RemainTime"), set.getString("Jailer"), set.getString("reason"));
    			p.setOfflinePending(set.getBoolean("Offline"));
    			p.setToBeTransferred(transfer);
    			
    			j.addPrisoner(p);
    			//String permissions = set.getString("Permissions"); TODO
    		}
    		
    		set.close();
    		ps.close();
    	}else {
    		pl.getLogger().warning("No prisoners were transferred from the old database since there aren't any jails defined.");
    	}
    }
    
    public void LoadCells() throws SQLException {
		Connection conn;
		PreparedStatement ps = null;
		ResultSet set = null;
		
		conn = getConnection();
		ps = conn.prepareStatement("SELECT * FROM jail_cells");
		set = ps.executeQuery();
		while (set.next()) {
			String jailname = set.getString("JailName");
			String teleport = set.getString("Teleport");
			String sign = set.getString("Sign");
			String chest = set.getString("Chest");
			String player = set.getString("Player");
			String name = set.getString("Name");
			
			if(name.isEmpty()) {
				pl.getLogger().warning("We need a cell name in Jail 3.0, refusing to load a cell due to this.");
			}else if(jailname.isEmpty()) {
				pl.getLogger().warning("Refusing to load a cell (" + name + ") as it does not contain a reference to a valid Jail.");
			}else {
				Jail j = pl.getJailManager().getJail(jailname);
				
				if(j == null) {
					pl.getLogger().warning("Refusing to load a cell (" + name + ") as it references a Jail which doesn't exist.");
				}else if(j.isValidCell(name)) {
					pl.getLogger().warning("Refusing to load a duplicate named cell, " + name + ", as another one exists with that same name.");
				} else {
					Cell c = new Cell(name);
					
					if(!teleport.isEmpty()) {
						String[] l = teleport.split(",");
						c.setTeleport(new SimpleLocation(j.getWorldName(), l[0], l[1], l[2]));
					}
					
					if(!chest.isEmpty()) {
						String[] ch = chest.split(",");
						c.setChestLocation(new Location(j.getWorld(), Double.valueOf(ch[0]), Double.valueOf(ch[1]), Double.valueOf(ch[2])));
					}
					
					if(!sign.isEmpty()) {
						for(String s : sign.split(";")) {
							pl.debug(s);
							String[] si = s.split(",");
							c.addSign(new SimpleLocation(j.getWorldName(), si[0], si[1], si[2]));
						}
					}
					
					//Load the prisoner if he is a valid prisoner
					if(!player.isEmpty()) {
						Prisoner p = j.getPrisonerByLastKnownName(name);
						
						if(p != null) {
							c.setPrisoner(p);
						}
					}
					
					j.addCell(c, false);
				}
			}
		}
		
		set.close();
		ps.close();
    }
    
    public void DeleteZone(String z) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement("DELETE FROM jail_zones WHERE name = ?");
		ps.setString(1, z);
		ps.executeUpdate();
		conn.commit();
		ps.close();
    }

    public void DeleteCell(int x, int y, int z) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement("DELETE FROM jail_cells WHERE Teleport = ?");
		ps.setString(1, String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z));
		ps.executeUpdate();
		conn.commit();
		
		ps.close();
    }
    
    public void DeletePrisoner(String p) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement("DELETE FROM jail_prisoners WHERE PlayerName = ?");
		ps.setString(1, p);
		ps.executeUpdate();
		conn.commit();
		
		ps.close();
    }
}
