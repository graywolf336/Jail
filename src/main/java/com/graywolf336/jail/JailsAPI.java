package com.graywolf336.jail;

import com.graywolf336.jail.interfaces.IJailStickManager;

/**
 * The static api interface for Jail plugin.
 *
 * <p />
 *
 * If you're looking for non-static methods, please see the
 * {@link JailMain} interface.
 *
 * @author graywolf336
 * @version 3.0.0
 * @since 3.0.0
 */
public class JailsAPI {
    private static JailMain pl;

    protected JailsAPI(JailMain plugin) {
        pl = plugin;
    }

    /**
     * The instance of the {@link JailManager} which contains all the jails and in return prisoners.
     *
     * @return instance of the {@link JailManager}
     * @see JailManager
     */
    public static JailManager getJailManager() {
        return pl.getJailManager();
    }

    /**
     * The instance of the {@link PrisonerManager} which handles all the jailing of players.
     *
     * @return instance of the {@link PrisonerManager}
     * @see PrisonerManager
     */
    public static PrisonerManager getPrisonerManager() {
        return pl.getPrisonerManager();
    }

    /**
     * Gets an instance of the {@link IJailStickManager} which handles all the jail sticks.
     *
     * @return an instance of the {@link IJailStickManager}
     * @see IJailStickManager
     */
    public static IJailStickManager getJailStickManager() {
        return pl.getJailStickManager();
    }

    /**
     * The instance of the {@link HandCuffManager} which handles all the handcuffing of players.
     *
     * @return instance of the {@link HandCuffManager}
     * @see HandCuffManager
     */
    public static HandCuffManager getHandCuffManager() {
        return pl.getHandCuffManager();
    }

    /**
     * The instance of the {@link JailVoteManager} which handles all the voting to jail players.
     *
     * @return instance of the {@link JailVoteManager}
     * @see JailVoteManager
     */
    public static JailVoteManager getJailVoteManager() {
        return pl.getJailVoteManager();
    }

    /**
     * Gets the amount of time from the provided time which the Jail plugin uses.
     *
     * @param time A string in the format "30minutes"
     * @return the amount of time
     * @throws Exception if the provided time string is in an invalid format
     */
    public static long getTimeFromString(String time) throws Exception {
        return Util.getTime(time);
    }
}
