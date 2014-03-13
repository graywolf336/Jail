package com.graywolf336.jail;

import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.enums.Settings;

public class ScoreBoardManager {
	private JailMain pl;
	private ScoreboardManager man;
	private HashMap<String, Scoreboard> boards;
	private OfflinePlayer time;
	
	public ScoreBoardManager(JailMain plugin) {
		this.pl = plugin;
		this.man = plugin.getServer().getScoreboardManager();
		this.boards = new HashMap<String, Scoreboard>();
		this.time = plugin.getServer().getOfflinePlayer(Util.getColorfulMessage(pl.getConfig().getString(Settings.SCOREBOARDTIME.getPath())));
		
		//Start the task if it is enabled
		if(plugin.getConfig().getBoolean(Settings.SCOREBOARDENABLED.getPath())) {
			plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				public void run() {
					updatePrisonersTime();
				}
			}, 200L, 100L);
		}
	}
	
	/**
	 * Adds the jailing score board to the player if they don't have one, otherwise it just updates it.
	 * 
	 * @param player of whom to add the scoreboard to.
	 * @param pris data for the provided prisoner
	 */
	public void addScoreBoard(Player player, Prisoner pris) {
		if(!boards.containsKey(player.getName())) {
			boards.put(player.getName(), man.getNewScoreboard());
			Objective o = boards.get(player.getName()).registerNewObjective("test", "dummy");
			o.setDisplaySlot(DisplaySlot.SIDEBAR);
			o.setDisplayName(Util.getColorfulMessage(pl.getConfig().getString(Settings.SCOREBOARDTITLE.getPath())));
			o.getScore(time).setScore(pris.getRemainingTimeInMinutesInt());
			player.setScoreboard(boards.get(player.getName()));
		}else {
			updatePrisonersBoard(player, pris);
		}
	}
	
	/**
	 * Removes a player's jail scoreboard for their jail time and sets it to the main one.
	 * 
	 * @param player of whom to remove the scoreboard for.
	 */
	public void removeScoreBoard(Player player) {
		boards.remove(player.getName());
		//TODO: See if this works or if we need to set it to a new one
		player.setScoreboard(man.getMainScoreboard());
	}
	
	public void removeAllScoreboards() {
		HashMap<String, Scoreboard> temp = new HashMap<String, Scoreboard>(boards);
		
		for(String s : temp.keySet()) {
			Player p = pl.getServer().getPlayerExact(s);
			
			if(p != null) {
				p.setScoreboard(man.getMainScoreboard());
			}
			
			boards.remove(s);
		}
	}
	
	private void updatePrisonersTime() {
		for(Jail j : pl.getJailManager().getJails()) {
			for(Prisoner p : j.getAllPrisoners()) {
				if(pl.getServer().getPlayerExact(p.getName()) != null) {
					addScoreBoard(pl.getServer().getPlayerExact(p.getName()), p);
				}
			}
		}
	}
	
	/**
	 * Updates a player's time in the scoreboard. 
	 * 
	 * @param player of whom to update the scoreboard for.
	 * @param pris data for the player
	 */
	private void updatePrisonersBoard(Player player, Prisoner pris) {
		boards.get(player.getName()).getObjective("test").getScore(time).setScore(pris.getRemainingTimeInMinutesInt());
	}
}
