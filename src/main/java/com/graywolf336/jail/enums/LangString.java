package com.graywolf336.jail.enums;

public enum LangString {
	//actions section
	
	/** Section for when they break a block. */
	BLOCKBREAKING ("actions"),
	/** Section for when they place a block. */
	BLOCKPLACING ("actions"),
	/** Section for when they try to do a command that isn't whitelisted. */
	COMMAND ("actions"),
	/** Section for when a player tramples a crop and protection is enabled. */
	CROPTRAMPLING ("actions"),
	/** Section for when a player interacts with a block that is blacklisted. */
	INTERACTIONBLOCKS ("actions"),
	/** Section for when a player interacts with an item that is blacklisted. */
	INTERACTIONITEMS ("actions"),
	/** Section for when a player moves outside of the jail. */
	MOVING ("actions"),
	
	//Jailing section
	
	/** The message displayed when players are kicked for being afk. */
	AFKKICKMESSAGE ("jailing"),
	/** The message sent when jailing someone that is already jailed. */
	ALREADYJAILED ("jailing"),
	/** The message sent when we broadcast/log the message for time below -1. */
	BROADCASTMESSAGEFOREVER ("jailing"),
	/** The message sent when we broadcast/log the message for any time above -1. */
	BROADCASTMESSAGEFORMINUTES ("jailing"),
	/** The message sent to the broadcast/log the unjailing of someone. */
	BROADCASTUNJAILING ("jailing"),
	/** The message sent to the sender when trying to jail someone and a plugin cancels it but doesn't leave a message why. */
	CANCELLEDBYANOTHERPLUGIN ("jailing"),
	/** The message sent when trying to jail someone who can't be jailed by permission. */
	CANTBEJAILED ("jailing"),
	/** The message sent to the sender when they are trying to jail into a cell which is not empty. */
	CELLNOTEMPTY ("jailing"),
	/** The message sent when someone is jailed without a reason. */
	DEFAULTJAILEDREASON ("jailing"),
	/** The message sent when someone is unjailed yet they never came online and so they were forcefully unjailed. */
	FORCEUNJAILED ("jailing"),
	/** The message sent when players are jailed without a reason. */
	JAILED ("jailing"),
	/** The message sent when players are jailed with a reason. */
	JAILEDWITHREASON ("jailing"),
	/** The message sent when players are jailed and they try to talk. */
	MUTED ("jailing"),
	/** The message sent when the sender tries to jail someone in a cell and there aren't any cells to suggest. */
	NOEMPTYCELLS ("jailing"),
	/** The message sent to the sender when they list all the prisoners in a jail which has no prisoners. */
	NOPRISONERS ("jailing"),
	/** The message sent when a player is not jailed and the sender is trying to see/do something about it. */
	NOTJAILED ("jailing"),
	/** The message sent to the sender when they mute a prisoner. */
	NOWMUTED ("jailing"),
	/** The message sent to the sender when they unmute a prisoner. */
	NOWUNMUTED ("jailing"),
	/** The message sent to the jailer when they jail someone offline. */
	OFFLINEJAIL ("jailing"),
	/** The message sent to the jailer when they jail someone who is online. */
	ONLINEJAIL ("jailing"),
	/** The message sent to the prisoner when they try to do something but it is protected. */
	PROTECTIONMESSAGE ("jailing"),
	/** The message sent to the prisoner when they try to do something and it is protected, but no penalty. */
	PROTECTIONMESSAGENOPENALTY ("jailing"),
	/** The message sent to the sender when they need to provide a player. */
	PROVIDEAPLAYER ("jailing"),
	/** The message sent to the sender when they need to provide a jail. */
	PROVIDEAJAIL ("jailing"),
	/** The message sent to the sender of a command when suggesting a cell. */
	SUGGESTEDCELL ("jailing"),
	/** The message sent to the sender when they teleport someone to a jail's teleport in location. */
	TELEIN ("jailing"),
	/** The message sent to the sender when they teleport someone to a jail's teleport out location. */
	TELEOUT ("jailing"),
	/** The message sent to the sender when they transfer all a jail's prisoners to another jail. */
	TRANSFERALLCOMPLETE ("jailing"),
	/** The message sent to the sender when they transfer someone to a jail and a cell. */
	TRANSFERCOMPLETECELL ("jailing"),
	/** The message sent to the sender when they transfer someone to a jail. */
	TRANSFERCOMPLETENOCELL ("jailing"),
	/** The message sent to the player when they get transferred to a new jail. */
	TRANSFERRED ("jailing"),
	/** The message sent when players are released from jail. */
	UNJAILED ("jailing"),
	/** The message went when an offline player is unjailed. */
	WILLBEUNJAILED ("jailing"),
	
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
	
	/** Part message of any messages which require 'all the jails' or such. */
	ALLJAILS ("general"),
	/** The message sent to the sender whenever they try to remove a cell but was unsuccessful due to a prisoner. */
	CELLREMOVALUNSUCCESSFUL ("general"),
	/** The message sent whenever a cell is successfully removed. */
	CELLREMOVED ("general"),
	/** The simple word jailing to be put in other parts. */
	JAILING ("jailing"),
	/** THe message sent to the sender when they try to remove a jail but there are still prisoners in there. */
	JAILREMOVALUNSUCCESSFUL ("general"),
	/** The message sent whenever a jail is successfully removed. */
	JAILREMOVED ("general"),
	/** Message sent when doing something that requires a cell but the given name of a cell doesn't exist. */
	NOCELL ("general"),
	/** Message sent when needing a cell or something and there are no cells. */
	NOCELLS ("general"),
	/** The message sent whenever the sender does something which the jail does not found. */
	NOJAIL ("general"),
	/** The message sent whenever the sender does something and there are no jails. */
	NOJAILS ("general"),
	/** The message sent whenever the sender/player doesn't have permission. */
	NOPERMISSION ("general"),
	/** The message sent whenever the sender/player supplies a number format that is incorrect. */
	NUMBERFORMATINCORRECT ("general"),
	/** The message sent whenever something is done that needs a player but doesn't have it. */
	PLAYERCONTEXTREQUIRED ("general"),
	/** The message sent whenever an online player is required but not found. */
	PLAYERNOTONLINE ("general"),
	/** The message sent to the sender when the plugin data has been reloaded. */
	PLUGINRELOADED ("general"),
	/** The message sent whenever the prisoners are cleared from jail(s). */
	PRISONERSCLEARED ("general"),
	/** The simple word transferring to be put in other parts. */
	TRANSFERRING ("general"),
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
