package com.graywolf336.jail.beans;

import com.graywolf336.jail.enums.Confirmation;

/**
 * Holds data for when a player is confirming a command.
 * 
 * @author graywolf336
 * @version 1.0.0
 * @since 3.0.0
 */
public class ConfirmPlayer {
    private String name;
    private String[] args;
    private Confirmation confirm;
    private Long expires;

    public ConfirmPlayer(String name, String[] args, Confirmation confirm) {
        this.name = name;
        this.args = args;
        this.confirm = confirm;
        this.expires = System.currentTimeMillis() + 5000L;
    }

    /** Returns the name of the thing needing to confirm. */
    public String getName() {
        return this.name;
    }

    /** Returns the initial arguments they sent with their command. */
    public String[] getArguments() {
        return this.args;
    }

    /** Returns what they are {@link Confirmation confirming}. */
    public Confirmation getConfirming() {
        return this.confirm;
    }

    /** Returns the time in milliseconds their confirmation time frame expires. */
    public Long getExpiryTime() {
        return this.expires;
    }
}
