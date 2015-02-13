package com.graywolf336.jail.beans;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Represents a vote to jail someone.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class JailVote {
    private String name;
    private ArrayList<UUID> voters;
    private int yes, no;
    
    /**
     * Initiates a vote to jail someone, with the vote being for the given player.
     * 
     * @param player the name of the player the vote is for
     */
    public JailVote(String player) {
        this.name = player;
        this.voters = new ArrayList<UUID>();
        this.yes = 0;
        this.no = 0;
    }
    
    /** The name of the player the vote is for. */
    public String getPlayerName() {
        return this.name;
    }
    
    /**
     * Gets the player who the vote is for, from the player name.
     * 
     * @return the player who the vote is for to jail
     */
    @SuppressWarnings("deprecation")
    public Player getPlayer() {
        return Bukkit.getPlayer(name);
    }
    
    /** Checks if the player with the given id has voted already. */
    public boolean hasVoted(UUID id) {
        return this.voters.contains(id);
    }
    
    /**
     * Method to vote yes, providing the uuid of the person doing the voting.
     * 
     * @param id the uuid of the voter
     * @return true if it was success, false if they already voted
     */
    public boolean voteYes(UUID id) {
        if(this.voters.contains(id)) {
            return false;
        }else {
            this.voters.add(id);
            this.yes++;
            return true;
        }
    }
    
    /**
     * Method to vote no, providing the uuid of the person doing the voting.
     * 
     * @param id the uuid of the voter
     * @return true if it was success, false if they already voted
     */
    public boolean voteNo(UUID id) {
        if(this.voters.contains(id)) {
            return false;
        }else {
            this.voters.add(id);
            this.no++;
            return true;
        }
    }
    
    /** Get the amount of yes votes. */
    public int getYesVotes() {
        return this.yes;
    }
    
    /** Get the amount of no votes. */
    public int getNoVotes() {
        return this.no;
    }
}
