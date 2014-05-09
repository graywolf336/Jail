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
	
	public JailTimer(JailMain plugin) {
		this.pl = plugin;
		
		this.lastTime = System.currentTimeMillis();
		if(pl.getConfig().getBoolean(Settings.USEBUKKITTIMER.getPath())) {
			pl.getLogger().info("Using the Bukkit Scheduler.");
			pl.getServer().getScheduler().scheduleSyncRepeatingTask(pl, new TimeEvent(), 20, 20);
		}else {
			pl.getLogger().info("Using the Java Timer.");
			timer = new Timer(1000, new ActionListener () {
				public void actionPerformed (ActionEvent event) {
					pl.getServer().getScheduler().scheduleSyncDelayedTask(pl, new TimeEvent());
				};
			});
			
			timer.start();
		}
	}
	
	/** Returns the instance of this timer. */
	public Timer getTimer() {
		return this.timer;
	}
	
	class TimeEvent implements Runnable {
		public void run() {
			long timePassed;
			
			//Let's check if more than 10 seconds has passed since the
			//last time we checked
			if (System.currentTimeMillis() - lastTime >= 10000) {
				//set the time passed to the current time minus the last time we checked
				timePassed = System.currentTimeMillis() - lastTime;
				lastTime = System.currentTimeMillis();
			}else {
				//Less than 10 seconds has past when we last ran this
				//so let's wait till the next round before we do this
				return;
			}
			
			for(Jail j : pl.getJailManager().getJails()) {
				for(Prisoner p : j.getAllPrisoners().values()) {
					//only execute this code if the prisoner's time is more than 0 milliseconds
					if(p.getRemainingTime() > 0) {
						Player player = pl.getServer().getPlayer(p.getUUID());
						
						//Check if the player is offline
						if(player == null) {
							//if they are offline AND the config has counting down the time
							//while the prisoner is offline, then let's do it
							if(pl.getConfig().getBoolean(Settings.COUNTDOWNTIMEOFFLINE.getPath())) {
								//Set their remaining time but if it is less than zero, set it to zero
								p.setRemainingTime(Math.max(0, p.getRemainingTime() - timePassed));
								if(p.getRemainingTime() == 0) pl.getPrisonerManager().releasePrisoner(player, p);
							}
						}else {
							//The prisoner isn't offline, so let's count down
							//Set their remaining time but if it is less than zero, set it to zero
							p.setRemainingTime(Math.max(0, p.getRemainingTime() - timePassed));
							if(p.getRemainingTime() == 0) pl.getPrisonerManager().releasePrisoner(player, p);
							
							//Now, let's set and check their afk time
							//add the time passed to their current afk time
							try {
								long afk = Util.getTime(pl.getConfig().getString(Settings.MAXAFKTIME.getPath()));
								if(afk > 0) {
									p.setAFKTime(p.getAFKTime() + timePassed);
									if(p.getAFKTime() > afk) {
										p.setAFKTime(0);
										player.kickPlayer(pl.getJailIO().getLanguageString(LangString.AFKKICKMESSAGE));
									}
								}
							} catch (Exception e) {
								pl.getLogger().severe("Error while processing the max afk time: " + e.getMessage());
							}
						}
					}
				}
				
				//Save all the prisoners and jails after we're done
				pl.getJailIO().saveJail(j);
			}
		}
	}
}
