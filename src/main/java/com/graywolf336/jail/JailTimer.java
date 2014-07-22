package com.graywolf336.jail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.bukkit.entity.Player;

import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.enums.LangString;
import com.graywolf336.jail.enums.Settings;

/**
 * Contains all the logic for counting down the time of the prisoners time.
 * 
 * @author graywolf336
 * @since 2.x.x
 * @version 3.0.0
 *
 */
public class JailTimer {
	private JailMain pl;
	private Timer timer;
	private Long lastTime;
	private Long afkTime = 0L;
	
	public JailTimer(JailMain plugin) {
		this.pl = plugin;
		try {
			afkTime = Util.getTime(pl.getConfig().getString(Settings.MAXAFKTIME.getPath()));
		} catch (Exception e) {
			pl.getLogger().severe("Error while processing the max afk time: " + e.getMessage());
		}
		
		this.lastTime = System.currentTimeMillis();
		if(pl.getConfig().getBoolean(Settings.USEBUKKITTIMER.getPath())) {
			pl.getLogger().info("Using the Bukkit Scheduler.");
			pl.getServer().getScheduler().runTaskTimerAsynchronously(pl, new TimeEvent(), 200, 200);
		}else {
			pl.getLogger().info("Using the Java Timer.");
			timer = new Timer(10000, new ActionListener () {
				public void actionPerformed (ActionEvent event) {
					pl.getServer().getScheduler().scheduleSyncDelayedTask(pl, new TimeEvent());
				};
			});
			
			timer.start();
		}
		
		//Save all the jail information every minute, not every 10 seconds
		pl.getServer().getScheduler().runTaskTimerAsynchronously(pl, new Runnable() {
			public void run() {
				for(Jail j : pl.getJailManager().getJails()) {
					pl.getJailIO().saveJail(j);
				}
			}
		}, 1200L, 1200L);
	}
	
	/** Returns the instance of this timer. */
	public Timer getTimer() {
		return this.timer;
	}
	
	class TimeEvent implements Runnable {
		public void run() {
			long timePassed = System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();
			
			for(Jail j : pl.getJailManager().getJails()) {
				for(Prisoner p : j.getAllPrisoners().values()) {
					//only execute this code if the prisoner's time is more than 0 milliseconds
					//and they don't have any offline pending things
					if(p.getRemainingTime() > 0 && !p.isOfflinePending()) {
						Player player = pl.getServer().getPlayer(p.getUUID());
						
						//Check if the player is offline
						if(player == null) {
							//if they are offline AND the config has counting down the time
							//while the prisoner is offline, then let's do it
							if(pl.getConfig().getBoolean(Settings.COUNTDOWNTIMEOFFLINE.getPath())) {
								//Set their remaining time but if it is less than zero, set it to zero
								p.setRemainingTime(Math.max(0, p.getRemainingTime() - timePassed));
								if(p.getRemainingTime() == 0) pl.getPrisonerManager().schedulePrisonerRelease(p);
							}
						}else {
							if(afkTime > 0) {
								p.setAFKTime(p.getAFKTime() + timePassed);
								if(p.getAFKTime() > afkTime) {
									p.setAFKTime(0);
									player.kickPlayer(pl.getJailIO().getLanguageString(LangString.AFKKICKMESSAGE));
								}
							}
							
							//The prisoner isn't offline, so let's count down
							//Set their remaining time but if it is less than zero, set it to zero
							p.setRemainingTime(Math.max(0, p.getRemainingTime() - timePassed));
							if(p.getRemainingTime() == 0) pl.getPrisonerManager().schedulePrisonerRelease(p);
						}
					}
				}
			}
		}
	}
}
