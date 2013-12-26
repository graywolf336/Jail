package com.graywolf336.jail.enums;

public enum LangString {
	/** The message sent when we broadcast/log the message for time below -1. */
	BROADCASTMESSAGEFOREVER,
	/** The message sent when we broadcast/log the message for any time above -1. */
	BROADCASTMESSAGEFORMINUTES,
	/** The message sent when trying to jail someone who can't be jailed by permission. */
	CANTBEJAILED,
	/** The message sent when someone is jailed without a reason. */
	DEFAULTJAILEDREASON,
	/** The message sent when players are jailed without a reason. */
	JAILED,
	/** The message sent when players are jailed with a reason. */
	JAILEDWITHREASON,
	/** The message sent when players are released from jail. */
	UNJAILED;
}
