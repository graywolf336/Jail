package com.graywolf336.jail.enums;

public enum JailVoteResult {
    /** The result when the PrePrisonerJailedEvent is cancelled. */
    EVENTCANCELLED,
    /** The result when the jailing results in an exception for some reason. */
    JAILEDEXCEPTION,
    /** The result when the jail which is picked is not enabled. */
    JAILNOTENABLED,
    /** The result when the player who the vote is for is no longer online. */
    NOTONLINE,
    /** The result when there aren't enough yes votes as configured. */
    NOTENOUGHYESVOTES,
    /** The result when there are no jails. */
    NOJAIL,
    /** The result when there are more no votes than yes votes. */
    NO,
    /** The result when the votes are tied, resulting in no jailing. */
    TIED,
    /** The result when there are enough yes votes over no votes for the vote to be successful. */
    YES
}
