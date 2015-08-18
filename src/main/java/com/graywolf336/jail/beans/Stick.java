package com.graywolf336.jail.beans;

/**
 * Represents a Jail Stick, contains all the information.
 * 
 * @author graywolf336
 * @version 1.0.1
 * @since 3.0.0
 */
public class Stick {
    private String jail, reason;
    private long time;
    private double health;

    /**
     * Creates a new Jail Stick instance.
     * 
     * @param jail the player will be jailed in.
     * @param reason the player will be jailed for.
     * @param time the player will be jailed for.
     * @param health a player must have, at the least, before being able to be jailed with this stick, -1 disables this feature
     */
    public Stick(String jail, String reason, long time, double health) {
        this.jail = jail;
        this.reason = reason;
        this.time = time;
        this.health = health;
    }

    /**
     * Gets the name of the jail a player will be sent when jailed via this jail stick.
     * 
     * @return the name of the jail this stick puts people in
     */
    public String getJail() {
        return this.jail;
    }

    /**
     * Gets the reason a player will be jailed for when jailed via this stick.
     * 
     * @return the reason of jailing used by this stick
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Gets the amount of time a player has to serve when they are jailed via this stick.
     * 
     * @return the amount of time someone is jailed via this stick
     */
    public long getTime() {
        return this.time;
    }

    /** Gets the amount of health a player has to have before getting jailed via this stick.
     * 
     * <br /><br />
     * 
     * See here for reference: http://dev.bukkit.org/bukkit-plugins/jail/tickets/415/
     */
    public double getHealth() {
        return this.health;
    }

    @Override
    public String toString() {
        return time + "," + jail + "," + reason + "," + health;
    }
}
