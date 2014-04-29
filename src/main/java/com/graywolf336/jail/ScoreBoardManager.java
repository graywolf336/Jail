package com.graywolf336.jail;

import java.util.HashMap;
import java.util.UUID;

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
	private HashMap<UUID, Scoreboard> boards;
	private OfflinePlayer time;
	
	public ScoreBoardManager(JailMain plugin) {
		this.pl = plugin;
		this.man = plugin.getServer().getScoreboardManager();
		this.boards = new HashMap<UUID, Scoreboard>();
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
			boards.put(player.getUniqueId(), man.getNewScoreboard());
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
		boards.remove(player.getUniqueId());
		//TODO: See if this works or if we need to set it to a new one
		player.setScoreboard(man.getMainScoreboard());
	}
	
	/** Removes all of the scoreboards from the prisoners. */
	public void removeAllScoreboards() {
		HashMap<UUID, Scoreboard> temp = new HashMap<UUID, Scoreboard>(boards);
		
		for(UUID id : temp.keySet()) {
			Player p = pl.getServer().getPlayer(id);
			
			if(p != null) {
				p.setScoreboard(man.getMainScoreboard());
			}
			
			boards.remove(id);
		}
	}
	
	/** Updates the prisoners time on their scoreboard. */
	private void updatePrisonersTime() {
		for(Jail j : pl.getJailManager().getJails()) {
			for(Prisoner p : j.getAllPrisoners()) {
				if(pl.getServer().getPlayer(p.getUUID()) != null) {
					addScoreBoard(pl.getServer().getPlayer(p.getUUID()), p);
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
