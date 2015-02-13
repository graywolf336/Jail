package com.graywolf336.jail;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.graywolf336.jail.beans.Jail;
import com.graywolf336.jail.beans.JailVote;
import com.graywolf336.jail.beans.Prisoner;
import com.graywolf336.jail.enums.JailVoteResult;
import com.graywolf336.jail.enums.Lang;
import com.graywolf336.jail.enums.Settings;
import com.graywolf336.jail.events.PrePrisonerJailedEvent;

/**
 * Manages all the votes to jail players.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class JailVoteManager {
    private JailMain pl;
    private HashMap<String, JailVote> votes;
    private HashMap<String, Integer> tasks;
    private String timerDesc, reason;
    private long timerTicks, jailTime;
    private int minYes;
    
    /**
     * Creates a new instance of this Jail Vote manager.
     *  
     * @throws Exception When it can't load the time correctly
     */
    protected JailVoteManager(JailMain plugin) throws Exception {
        this.pl = plugin;
        this.votes = new HashMap<String, JailVote>();
        this.tasks = new HashMap<String, Integer>();
        this.reason = plugin.getConfig().getString(Settings.JAILVOTEREASON.getPath());
        
        String timer = plugin.getConfig().getString(Settings.JAILVOTETIMER.getPath());
        this.timerDesc = Util.getDurationBreakdown(Util.getTime(timer));
        this.timerTicks = Util.getTime(timer, TimeUnit.SECONDS) * 20;
        this.jailTime = Util.getTime(plugin.getConfig().getString(Settings.JAILVOTETIME.getPath()));
        this.minYes = plugin.getConfig().getInt(Settings.JAILMINYESVOTES.getPath());
    }
    
    /** Gets all the votes to jail someone. */
    public HashMap<String, JailVote> getVotes() {
        return this.votes;
    }
    
    /**
     * Gets the jail vote for the given player name, returning null if it doesn't exist.
     * 
     * @param name of the player to get the jail vote for
     * @return {@link JailVote} for the player
     */
    public JailVote getVoteForPlayer(String name) {
        return this.votes.get(name);
    }
    
    /**
     * Adds a vote to jail someone.
     * 
     * @param v the {@link JailVote} to add.
     * @return true if the vote was added, false if a vote already exists for that player name.
     */
    public boolean addVote(JailVote v) {
        if(this.votes.containsKey(v.getPlayerName())) {
            return false;
        }else {
            this.votes.put(v.getPlayerName(), v);
            return true;
        }
    }
    
    /**
     * Adds a vote for the given player's name.
     * 
     * @param name the name of the player the vote is for.
     * @param id the uuid of the player voting.
     * @param which whether they are voting yes or no, true if yes false if no.
     * @return True if the vote was successful, false if it was unsuccessful.
     */
    public boolean addVote(String name, UUID id, boolean which) {
        if(this.votes.containsKey(name)) {
            pl.debug(id.toString() + " voted " + (which ? "yes" : "no") + " to jail " + name);
            if(which) {
                return this.votes.get(name).voteYes(id);
            }else {
                return this.votes.get(name).voteNo(id);
            }
        }else {
            return false;
        }
    }
    
    public boolean hasVotedAlready(String name, UUID id) {
        if(this.votes.containsKey(name)) {
            return this.votes.get(name).hasVoted(id);
        }else {
            return false;
        }
    }
    
    /**
     * Checks if a player is voted for or not.
     * 
     * @param name of the player to check for.
     * @return true if they were voted for, false if not.
     */
    public boolean isVotedFor(String name) {
        return this.votes.containsKey(name);
    }
    
    /** Returns the nice formatted time of how long a vote is open. */
    public String getTimerLengthDescription() {
        return this.timerDesc;
    }
    
    /** Returns the minimum amount of yes votes required to jail someone. */
    public int getMinimumYesVotes() {
        return this.minYes;
    }
    
    /** Gets the current running tasks ids. */
    public HashMap<String, Integer> getRunningTasks() {
        return this.tasks;
    }
    
    /**
     * Schedules the calculation of whether a jail vote should jail or not jail.
     * 
     * @param name the name of the person who is being voted to be jailed
     */
    public void scheduleCalculating(final String name) {
        int taskId = pl.getServer().getScheduler().runTaskLater(pl, new Runnable() {
            public void run() {
                doTheVoteCalculation(votes.get(name));
                tasks.remove(name);
            }
        }, timerTicks).getTaskId();
        
        this.tasks.put(name, taskId);
    }
    
    /**
     * Calculates the votes, determining whether there are enough votes to jail the person or not.
     * 
     * @param v the {@link JailVote} to do the calculation of.
     */
    public JailVoteResult doTheVoteCalculation(JailVote v) {
        JailVoteResult result;
        
        if(v.getPlayer() == null) {
            pl.getServer().broadcastMessage(Lang.VOTEPLAYERNOLONGERONLINE.get(v.getPlayerName()));
            result = JailVoteResult.NOTONLINE;
        }else {
            if(v.getYesVotes() > v.getNoVotes()) {
                if(v.getYesVotes() >= getMinimumYesVotes()) {
                    Prisoner p = new Prisoner(v.getPlayer().getUniqueId().toString(), v.getPlayerName(), pl.getConfig().getBoolean(Settings.AUTOMATICMUTE.getPath()), jailTime, "[JailVote]", reason);
                    
                    //Get the jail name, check for the nearest one
                    String jailName = pl.getConfig().getString(Settings.DEFAULTJAIL.getPath());
                    if(jailName.equalsIgnoreCase("nearest")) {
                        Jail j = pl.getJailManager().getNearestJail(v.getPlayer());
                        if(j != null) {
                            jailName = j.getName();
                        }
                    }
                    
                    //Get the jail instance from the name of jail in the params.
                    Jail j = pl.getJailManager().getJail(jailName);
                    if(j == null) {
                        pl.getLogger().warning("Jail Vote Failed: no jail was found?");
                        result = JailVoteResult.NOJAIL;
                    }else if(!j.isEnabled()) {
                        pl.getLogger().warning("Jail Vote Failed: " + Lang.WORLDUNLOADED.get(j.getName()));
                        result = JailVoteResult.JAILNOTENABLED;
                    }else {
                        //call the event
                        PrePrisonerJailedEvent event = new PrePrisonerJailedEvent(j, null, p, v.getPlayer(), v.getPlayer() == null, p.getJailer());
                        pl.getServer().getPluginManager().callEvent(event);

                        //check if the event is cancelled
                        if(event.isCancelled()) {
                            pl.getLogger().warning("Jail Vote Failed: The PrePrisonerJailedEvent was cancelled for some reason.");
                            result = JailVoteResult.EVENTCANCELLED;
                        }else {
                            try {
                                pl.getPrisonerManager().prepareJail(j, null, v.getPlayer(), p);
                                result = JailVoteResult.YES;
                            } catch (Exception e) {
                                e.printStackTrace();
                                pl.getLogger().warning("Jail Vote Failed: The preparing the jail failed.");
                                result = JailVoteResult.JAILEDEXCEPTION;
                            }
                        }
                    }
                }else {
                    pl.getServer().broadcastMessage(Lang.VOTESNOTENOUGHYES.get(new String[] { v.getPlayerName(), String.valueOf(v.getYesVotes()), String.valueOf(minYes) }));
                    result = JailVoteResult.NOTENOUGHYESVOTES;
                }
            }else if(v.getYesVotes() == v.getNoVotes()) {
                pl.getServer().broadcastMessage(Lang.VOTESTIED.get(v.getPlayerName()));
                result = JailVoteResult.TIED;
            }else {
                pl.getServer().broadcastMessage(Lang.VOTESSAIDNO.get(v.getPlayerName()));
                result = JailVoteResult.NO;
            }
        }
        
        votes.remove(v.getPlayerName());
        return result;
    }
}
