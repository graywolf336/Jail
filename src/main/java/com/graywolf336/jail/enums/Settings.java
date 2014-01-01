package com.graywolf336.jail.enums;

public enum Settings {
	BROADCASTJAILING("jailing.jail.broadcastJailing"),
	COMMANDSONJAIL("jailing.jail.commands"),
	COMMANDSONRELEASE("jailing.release.commands"),
	DEBUG("system.debug"),
	DEFAULTJAIL("jailing.jail.defaultJail"),
	DELETEINVENTORY("jailing.jail.deleteInventory"),
	IGNORESLEEPINGSTATE("jailing.during.ignoreSleeping"),
	JAILDEFAULTTIME("jailing.jail.defaultTime"),
	JAILEDGAMEMODE("jailing.jail.gameMode"),
	JAILEDSTOREINVENTORY("jailing.jail.storeInventory"),
	LOGJAILING("jailing.jail.logToConsole"),
	MAXFOODLEVEL("jailing.during.maxFoodLevel"),
	MINFOODLEVEL("jailing.during.minFoodLevel"),
	RELEASETOPREVIOUSPOSITION("jailing.release.backToPreviousPosition"),
	RESTOREPREVIOUSGAMEMODE("jailing.release.restorePreviousGameMode"),
	TELEPORTONRELEASE("jailing.release.teleport"),
	UPDATENOTIFICATIONS("system.updateNotifications");
	
	private String path;
	
	private Settings(String path) {
		this.path = path;
	}
	
	/**
	 * Gets the path this setting is in config.
	 * @return The path where this setting resides in the config.
	 */
	public String getPath() {
		return this.path;
	}
}
