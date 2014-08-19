package com.graywolf336.jail.enums;

public enum Settings {
    AUTOMATICMUTE("jailing.jail.automaticMute"),
    BROADCASTJAILING("jailing.jail.broadcastJailing"),
    BLOCKBREAKPENALTY("jailing.during.blockBreakPenalty"),
    BLOCKBREAKPROTECTION("jailing.during.blockBreakProtection"),
    BLOCKBREAKWHITELIST("jailing.during.blockBreakWhiteList"),
    BLOCKPLACEPENALTY("jailing.during.blockPlacePenalty"),
    BLOCKPLACEPROTECTION("jailing.during.blockPlaceProtection"),
    BLOCKPLACEWHITELIST("jailing.during.blockPlaceWhiteList"),
    CELLSIGNLINES("jailing.during.cellsign"),
    CLOTHINGENABLED("jailing.jail.clothing.enabled"),
    CLOTHINGHELMET("jailing.jail.clothing.helmet"),
    CLOTHINGCHEST("jailing.jail.clothing.chest"),
    CLOTHINGLEGS("jailing.jail.clothing.legs"),
    CLOTHINGBOOTS("jailing.jail.clothing.boots"),
    COMMANDSONJAIL("jailing.jail.commands"),
    COMMANDSONRELEASE("jailing.release.commands"),
    COMMANDPENALTY("jailing.during.commandPenalty"),
    COMMANDPROTECTION("jailing.during.commandProtection"),
    COMMANDWHITELIST("jailing.during.commandWhitelist"),
    CONFIGVERSION("system.version"),
    COUNTDOWNTIMEOFFLINE("jailing.during.countDownTimeWhileOffline"),
    CROPTRAMPLINGPENALTY("jailing.during.cropTramplingPenalty"),
    CROPTRAMPLINGPROTECTION("jailing.during.cropTramplingProtection"),
    DEBUG("system.debug"),
    DEFAULTJAIL("jailing.jail.default.jail"),
    DEFAULTTIME("jailing.jail.default.time"),
    ENDERMENPROTECTION("jails.endermenProtection"),
    EXPLOSIONPROTECTION("jails.explosionProtection"),
    FOODCONTROL("jailing.during.foodControl.enabled"),
    FOODCONTROLMAX("jailing.during.foodControl.max"),
    FOODCONTROLMIN("jailing.during.foodControl.min"),
    IGNORESLEEPINGSTATE("jailing.during.ignoreSleeping"),
    JAILSTICKENABLED("jailstick.enabled"),
    JAILSTICKSTICKS("jailstick.sticks"),
    JAILEDGAMEMODE("jailing.jail.gameMode"),
    JAILEDINVENTORYBLACKLIST("jailing.jail.inventory.blacklist"),
    JAILEDSTOREINVENTORY("jailing.jail.inventory.store"),
    JAILPAYENABLED("jailpay.enabled"),
    JAILPAYITEM("jailpay.item"),
    JAILPAYPRICEPERMINUTE ("jailpay.pricePerMinute"),
    JAILPAYPRICEINFINITE ("jailpay.priceInfinite"),
    LANGUAGE("system.language"),
    LOGJAILINGTOCONSOLE("jailing.jail.log.console"),
    LOGJAILINGTOPROFILE("jailing.jail.log.profile"),
    MAXAFKTIME("jailing.during.maxAFKTime"),
    MOVEPENALTY("jailing.during.movePenalty"),
    MOVEPROTECTION("jailing.during.moveProtection"),
    PREVENTINTERACTIONBLOCKS("jailing.during.preventInteractionBlocks"),
    PREVENTINTERACTIONBLOCKSPENALTY("jailing.during.preventInteractionBlocksPenalty"),
    PREVENTINTERACTIONITEMS("jailing.during.preventInteractionItems"),
    PREVENTINTERACTIONITEMSPENALTY("jailing.during.preventInteractionItemsPenalty"),
    PRISONEROPENCHEST("jailing.during.openChest"),
    RECIEVEMESSAGES("jailing.during.recieveMessages"),
    RELEASETOPREVIOUSPOSITION("jailing.release.backToPreviousPosition"),
    RESTOREPREVIOUSGAMEMODE("jailing.release.restorePreviousGameMode"),
    SCOREBOARDENABLED("jailing.during.scoreboard.enabled"),
    SCOREBOARDTITLE("jailing.during.scoreboard.title"),
    SCOREBOARDTIME("jailing.during.scoreboard.time"),
    TELEPORTONRELEASE("jailing.release.teleport"),
    UPDATECHANNEL("system.updates.channel"),
    UPDATENOTIFICATIONS("system.updates.notification"),
    UPDATETIME("system.updates.time"),
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
