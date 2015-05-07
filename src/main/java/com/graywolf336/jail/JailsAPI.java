package com.graywolf336.jail;

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
     * The instance of the {@link JailStickManager} which handles all the jail sticks.
     * 
     * @return instance of the {@link JailStickManager}
     * @see JailStickManager
     */
    public static JailStickManager getJailStickManager() {
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
}
