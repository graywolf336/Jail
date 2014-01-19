package com.graywolf336.jail.enums;

public enum LangString {
	//actions section
	
	/** Section for when they break a block. */
	BLOCKBREAKING ("actions"),
	/** Section for when they place a block. */
	BLOCKPLACING ("actions"),
	
	//Jailing section
	
	/** The message displayed when players are kicked for being afk. */
	AFKKICKMESSAGE ("jailing"),
	/** The message sent when we broadcast/log the message for time below -1. */
	BROADCASTMESSAGEFOREVER ("jailing"),
	/** The message sent when we broadcast/log the message for any time above -1. */
	BROADCASTMESSAGEFORMINUTES ("jailing"),
	/** The message sent when trying to jail someone who can't be jailed by permission. */
	CANTBEJAILED ("jailing"),
	/** The message sent when someone is jailed without a reason. */
	DEFAULTJAILEDREASON ("jailing"),
	/** The message sent when players are jailed without a reason. */
	JAILED ("jailing"),
	/** The message sent when players are jailed with a reason. */
	JAILEDWITHREASON ("jailing"),
	/** The message sent when players are jailed and they try to talk. */
	MUTED ("jailing"),
	/** The message sent to the prisoner when they try to do something but it is protected. */
	PROTECTIONMESSAGE ("jailing"),
	/** The message sent when players are released from jail. */
	UNJAILED ("jailing"),
	
	//Handcuffing section
	
	/** The message sent to the sender when trying to handcuff someone who can't be. */
	CANTBEHANDCUFFED ("handcuffing"),
	/** The message sent to the sender whenever they try to handcuff someone who is in jail. */
	CURRENTLYJAILEDHANDCUFF ("handcuffing", "currentlyjailed"),
	/** The message sent to the sender when the player doesn't have any handcuffs. */
	NOTHANDCUFFED ("handcuffing"),
	/** The message sent to the handcuff on a successful handcuffing. */
	HANDCUFFSON ("handcuffing"),
	/** The message sent when players are handcuffed. */
	HANDCUFFED ("handcuffing"),
	/** The message sent to the player who has release handcuffs. */
	HANDCUFFSRELEASED ("handcuffing"),
	/** The message sent when the player has his/her handcuffs removed. */
	UNHANDCUFFED ("handcuffing"),
	
	//General section, used by different parts
	
	/** The message sent whenever the sender/player doesn't have permission. */
	NOPERMISSION ("general"),
	/** The message sent whenever something is done that needs a player but doesn't have it. */
	PLAYERCONTEXTREQUIRED ("general"),
	/** The message sent whenever an online player is required but not found. */
	PLAYERNOTONLINE ("general"),
	/** The message sent whenever someone does a command we don't know. */
	UNKNOWNCOMMAND ("general");
	
	private String section, name;
	
	LangString(String section) {
		this.section = section;
	}
	
	LangString(String section, String name) {
		this.section = section;
		this.name = name;
	}
	
	/** Gets the section in the language file this is located at. */
	public String getSection() {
		return this.section;
	}
	
	/** Returns the name of this enum if a custom one isn't present. */
	public String getName() {
		return (name == null ? this.toString().toLowerCase() : name);
	}
}
