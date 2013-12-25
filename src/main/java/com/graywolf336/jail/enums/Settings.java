package com.graywolf336.jail.enums;

public enum Settings {
	BROADCASTJAILING("jailing.jail.broadcastJailing"),
	DEBUG("system.debug"),
	DEFAULTJAIL("jailing.jail.defaultJail"),
	JAILDEFAULTTIME("jailing.jail.defaultTime"),
	LOGJAILING("jailing.jail.logToConsole"),
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
