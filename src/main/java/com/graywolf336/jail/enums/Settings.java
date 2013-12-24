package com.graywolf336.jail.enums;

public enum Settings {
	DEBUG("system.debug"),
	UPDATENOTIFICATIONS("system.updateNotifications"),
	JAILDEFAULTTIME("jailing.jail.defaultTime");
	
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
