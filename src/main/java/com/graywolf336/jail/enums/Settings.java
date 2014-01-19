package com.graywolf336.jail.enums;

public enum Settings {
	BROADCASTJAILING("jailing.jail.broadcastJailing"),
	BLOCKBREAKPENALTY("jailing.during.blockBreakPenalty"),
	BLOCKBREAKPROTECTION("jailing.during.blockBreakProtection"),
	BLOCKBREAKWHITELIST("jailing.during.blockBreakWhiteList"),
	BLOCKPLACEPENALTY("jailing.during.blockPlacePenalty"),
	BLOCKPLACEPROTECTION("jailing.during.blockPlaceProtection"),
	BLOCKPLACEWHITELIST("jailing.during.blockPlaceWhiteList"),
	COMMANDSONJAIL("jailing.jail.commands"),
	COMMANDSONRELEASE("jailing.release.commands"),
	COUNTDOWNTIMEOFFLINE("jailing.during.countDownTimeWhileOffline"),
	DEBUG("system.debug"),
	DEFAULTJAIL("jailing.jail.defaultJail"),
	DELETEINVENTORY("jailing.jail.deleteInventory"),
	EXPLOSIONPROTECTION("jailing.during.explosionProtection"),
	IGNORESLEEPINGSTATE("jailing.during.ignoreSleeping"),
	JAILDEFAULTTIME("jailing.jail.defaultTime"),
	JAILEDGAMEMODE("jailing.jail.gameMode"),
	JAILEDSTOREINVENTORY("jailing.jail.storeInventory"),
	LOGJAILING("jailing.jail.logToConsole"),
	MAXAFKTIME("jailing.during.maxAFKTime"),
	MAXFOODLEVEL("jailing.during.maxFoodLevel"),
	MINFOODLEVEL("jailing.during.minFoodLevel"),
	RECIEVEMESSAGES("jailing.during.recieveMessages"),
	RELEASETOPREVIOUSPOSITION("jailing.release.backToPreviousPosition"),
	RESTOREPREVIOUSGAMEMODE("jailing.release.restorePreviousGameMode"),
	TELEPORTONRELEASE("jailing.release.teleport"),
	UPDATENOTIFICATIONS("system.updateNotifications"),
	USEBUKKITTIMER("system.useBukkitTimer");
	
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
