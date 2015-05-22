package com.graywolf336.jail.enums;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {
    // actions section
    /** Section for when they break a block. */
    BLOCKBREAKING("actions"),
    /** Section for when they place a block. */
    BLOCKPLACING("actions"),
    /** Section for when they try to do a command that isn't whitelisted. */
    COMMAND("actions"),
    /** Section for when a player tramples a crop and protection is enabled. */
    CROPTRAMPLING("actions"),
    /** Section for when a player interacts with a block that is blacklisted. */
    INTERACTIONBLOCKS("actions"),
    /** Section for when a player interacts with an item that is blacklisted. */
    INTERACTIONITEMS("actions"),
    /** Section for when a player moves outside of the jail. */
    MOVING("actions"),

    // Jailing section
    /** The message displayed when players are kicked for being afk. */
    AFKKICKMESSAGE("jailing"),
    /** The message sent when jailing someone that is already jailed. */
    ALREADYJAILED("jailing"),
    /** The message sent when we broadcast/log the message for time below -1. */
    BROADCASTMESSAGEFOREVER("jailing"),
    /** The message sent when we broadcast/log the message for any time above -1. */
    BROADCASTMESSAGEFORMINUTES("jailing"),
    /** The message sent to the broadcast/log the unjailing of someone. */
    BROADCASTUNJAILING("jailing"),
    /** The message sent to the sender when trying to jail someone and a plugin cancels it but doesn't leave a message why. */
    CANCELLEDBYANOTHERPLUGIN("jailing"),
    /** The message sent when trying to jail someone who can't be jailed by permission. */
    CANTBEJAILED("jailing"),
    /** The message sent to the sender when they are trying to jail into a cell which is not empty. */
    CELLNOTEMPTY("jailing"),
    /** The jailer set whenever a jailer is not provided. */
    DEFAULTJAILER("jailing"),
    /** The message sent when someone is jailed without a reason. */
    DEFAULTJAILEDREASON("jailing"),
    /** The message sent when someone is unjailed yet they never came online and so they were forcefully unjailed. */
    FORCEUNJAILED("jailing"),
    /** The message sent when players are jailed without a reason. */
    JAILED("jailing"),
    /** The message sent when players are jailed with a reason. */
    JAILEDWITHREASON("jailing"),
    /** The message sent when players are jailed and they try to talk. */
    MUTED("jailing"),
    /** The message sent when the sender tries to jail someone in a cell and there aren't any cells to suggest. */
    NOEMPTYCELLS("jailing"),
    /** The message sent to the sender when they list all the prisoners in a jail which has no prisoners. */
    NOPRISONERS("jailing"),
    /** The message sent when a player is not jailed and the sender is trying to see/do something about it. */
    NOTJAILED("jailing"),
    /** The message sent to the sender when they mute a prisoner. */
    NOWMUTED("jailing"),
    /** The message sent to the sender when they unmute a prisoner. */
    NOWUNMUTED("jailing"),
    /** The message sent to the jailer when they jail someone offline. */
    OFFLINEJAIL("jailing"),
    /** The message sent to the jailer when they jail someone who is online. */
    ONLINEJAIL("jailing"),
    /** The message sent when finding out how much time a prisoner has. */
    PRISONERSTIME("jailing"),
    /** The message sent to the prisoner when they try to do something but it is protected. */
    PROTECTIONMESSAGE("jailing"),
    /** The message sent to the prisoner when they try to do something and it is protected, but no penalty. */
    PROTECTIONMESSAGENOPENALTY("jailing"),
    /** The message sent to the sender when they need to provide a player. */
    PROVIDEAPLAYER("jailing"),
    /** The message sent to the sender when they need to provide a jail. */
    PROVIDEAJAIL("jailing"),
    /** The message sent to someone trying to jail someone else with a jail stick that requires health below a certain amount. */
    RESISTEDARRESTJAILER("jailing"),
    /** The message sent to the person someone else is trying to jail that requires their health below a certain amount. */
    RESISTEDARRESTPLAYER("jailing"),
    /** The message sent when to a prisoner about their status in jail. */
    STATUS("jailing"),
    /** The message sent to the sender of a command when suggesting a cell. */
    SUGGESTEDCELL("jailing"),
    /** The message sent to the sender when they teleport someone to a jail's teleport in location. */
    TELEIN("jailing"),
    /** The message sent to the sender when they teleport someone to a jail's teleport out location. */
    TELEOUT("jailing"),
    /** The message sent to the sender when they transfer all a jail's prisoners to another jail. */
    TRANSFERALLCOMPLETE("jailing"),
    /** The message sent when another plugin cancels the transferring but doesn't provide a reason why. */
    TRANSFERCANCELLEDBYANOTHERPLUGIN("jailing"),
    /** The message sent to the sender when they transfer someone to a jail and a cell. */
    TRANSFERCOMPLETECELL("jailing"),
    /** The message sent to the sender when they transfer someone to a jail. */
    TRANSFERCOMPLETENOCELL("jailing"),
    /** The message sent to the player when they get transferred to a new jail. */
    TRANSFERRED("jailing"),
    /** The message sent when players are released from jail. */
    UNJAILED("jailing"),
    /** The message sent to the person who released a prisoner from jail. */
    UNJAILSUCCESS("jailing"),
    /** The message went when an offline player is unjailed. */
    WILLBEUNJAILED("jailing"),
    /** The message sent when trying to jail a player in an unloaded world. */
    WORLDUNLOADED("jailing"),
    /** The message sent when a player joins and is jailed in a world that is unloaded. */
    WORLDUNLOADEDKICK("jailing"),
    /** The message sent to the sender when they check their jail status and they aren't jailed. */
    YOUARENOTJAILED("jailing"),

    // Handcuffing section
    /** The message sent to the sender when trying to handcuff someone who can't be. */
    CANTBEHANDCUFFED("handcuffing"),
    /** The message sent to the sender whenever they try to handcuff someone who is in jail. */
    CURRENTLYJAILEDHANDCUFF("handcuffing", "currentlyjailed"),
    /** The message sent to the sender when the player doesn't have any handcuffs. */
    NOTHANDCUFFED("handcuffing"),
    /** The message sent to the handcuff on a successful handcuffing. */
    HANDCUFFSON("handcuffing"),
    /** The message sent when players are handcuffed. */
    HANDCUFFED("handcuffing"),
    /** The message sent to the player who has release handcuffs. */
    HANDCUFFSRELEASED("handcuffing"),
    /** The message sent when the player has his/her handcuffs removed. */
    UNHANDCUFFED("handcuffing"),

    // General section, used by different parts
    /** Part message of any messages which require 'all the jails' or such. */
    ALLJAILS("general"),
    /** The one line on signs when the cell is empty. */
    CELLEMPTYSIGN("general"),
    /** The message sent to the sender whenever they try to remove a cell but was unsuccessful due to a prisoner. */
    CELLREMOVALUNSUCCESSFUL("general"),
    /** The message sent whenever a cell is successfully removed. */
    CELLREMOVED("general"),
    /** The line on a cell's sign when the prisoner is jailed forever. */
    JAILEDFOREVERSIGN("general"),
    /** The simple word jailing to be put in other parts. */
    JAILING("general"),
    /** The message sent to the sender when they try to remove a jail but there are still prisoners in there. */
    JAILREMOVALUNSUCCESSFUL("general"),
    /** The message sent whenever a jail is successfully removed. */
    JAILREMOVED("general"),
    /** The message sent whenever a player toggles using jail stick to disabled. */
    JAILSTICKDISABLED("general"),
    /** The message sent whenever a player toggles using jail stick to enabled. */
    JAILSTICKENABLED("general"),
    /** The message sent whenever a player tries to toggle using jail stick but the config has it disabled. */
    JAILSTICKUSAGEDISABLED("general"),
    /** Message sent when doing something that requires a cell but the given name of a cell doesn't exist. */
    NOCELL("general"),
    /** Message sent when needing a cell or something and there are no cells. */
    NOCELLS("general"),
    /** The message sent whenever the sender does something which the jail does not found. */
    NOJAIL("general"),
    /** The message sent whenever the sender does something and there are no jails. */
    NOJAILS("general"),
    /** The message sent whenever the sender/player doesn't have permission. */
    NOPERMISSION("general"),
    /** The message sent whenever the sender/player supplies a number format that is incorrect. */
    NUMBERFORMATINCORRECT("general"),
    /** The message sent whenever something is done that needs a player but doesn't have it. */
    PLAYERCONTEXTREQUIRED("general"),
    /** The message sent whenever an online player is required but not found. */
    PLAYERNOTONLINE("general"),
    /** The message sent to the sender when the plugin data has been reloaded. */
    PLUGINRELOADED("general"),
    /** The message sent to the sender of a command when the plugin didn't start correct. */
    PLUGINNOTLOADED("general"),
    /** The message sent whenever the prisoners are cleared from jail(s). */
    PRISONERSCLEARED("general"),
    /** The format we should use when entering a record into flatfile or showing it. */
    RECORDENTRY("general"),
    /** The message format sent saying how many times a user has been jailed. */
    RECORDTIMESJAILED("general"),
    /** The format of the time entry we should use for the record entries. */
    TIMEFORMAT("general"),
    /** The simple word transferring to be put in other parts. */
    TRANSFERRING("general"),
    /** The message sent whenever someone does a command we don't know. */
    UNKNOWNCOMMAND("general"),

    // Jail pay
    /** The message sent when the jail pay portion is not enabled. */
    PAYNOTENABLED("jailpay", "notenabled"),
    /** The message sent when finding out how much it costs. */
    PAYCOST("jailpay", "cost"),
    /** The message sent when finding out how much it costs and they are jailed forever. */
    PAYCOSTINFINITE("jailpay", "costinfinite"),
    /** The message sent when someone tries to pay a negative amount. */
    PAYNONEGATIVEAMOUNTS("jailpay", "nonegativeamounts"),
    /** The message sent when someone is jailed and tries to pay for someone else. */
    PAYCANTPAYWHILEJAILED("jailpay", "cantpayforotherswhilejailed"),
    /** The message sent whenever someone tries to pay an amount they don't have. */
    PAYNOTENOUGHMONEY("jailpay", "notenoughmoney"),
    /** The message sent when they try to pay an amount but it isn't enough for the jailing sentence. */
    PAYNOTENOUGHMONEYPROVIDED("jailpay", "notenoughmoneyprovided"),
    /** The message sent when they pay and get released. */
    PAYPAIDRELEASED("jailpay", "paidreleased"),
    /** The message sent when they pay for someone else and release them. */
    PAYPAIDRELEASEDELSE("jailpay", "paidreleasedelse"),
    /** The message sent when they pay and lower their time. */
    PAYPAIDLOWEREDTIME("jailpay", "paidloweredtime"),
    /** The message sent when they pay and lower someone else's time. */
    PAYPAIDLOWEREDTIMEELSE("jailpay", "paidloweredtimeelse"),

    // Jail vote
    /** The header sent when broadcasting a new jail vote. */
    VOTEBROADCASTHEADER("jailvote.broadcast", "header"),
    /** The footer sent when broadcasting a new jail vote. */
    VOTEBROADCASTFOOTER("jailvote.broadcast", "footer"),
    /** Line1 of the broadcast message sent when a new jail vote is happening. */
    VOTEBROADCASTLINE1("jailvote.broadcast", "line1"),
    /** Line2 of the broadcast message sent when a new jail vote is happening. */
    VOTEBROADCASTLINE2("jailvote.broadcast", "line2"),
    /** Line3 of the broadcast message sent when a new jail vote is happening. */
    VOTEBROADCASTLINE3("jailvote.broadcast", "line3"),
    /** Line4 of the broadcast message sent when a new jail vote is happening. */
    VOTEBROADCASTLINE4("jailvote.broadcast", "line4"),
    /** The message sent when someone tries to vote for a player when a vote isn't running. */
    VOTENOVOTEFORTHATPLAYER("jailvote", "novotegoingforthatplayer"),
    /** The message sent to a player who tries to start a vote to jail someone and doesn't have permission. */
    VOTENOPERMISSIONTOSTART("jailvote", "nopermissiontostartvote"),
    /** The message sent when jail vote is not enabled. */
    VOTENOTENABLED("jailvote", "notenabled"),
    /** The message sent whenever someone's vote is not successful. */
    VOTEUNSUCCESSFUL("jailvote", "voteunsuccessful"),
    /** The message sent whenever a player successfully votes no. */
    VOTENOSUCCESS("jailvote", "votenosuccessful"),
    /** The message sent whenever a player successfully votes yes. */
    VOTEYESSUCCESS("jailvote", "voteyessuccessful"),
    /** The message broadcasted whenever a vote is tied. */
    VOTESTIED("jailvote", "votestied"),
    /** The message broadcasted whenever there are more no votes. */
    VOTESSAIDNO("jailvote", "morenovotes"),
    /** The message broadcasted whenever there aren't the minimum yes votes. */
    VOTESNOTENOUGHYES("jailvote", "notenoughyes"),
    /** The message broadcasted whenever the player the vote is for is no longer online. */
    VOTEPLAYERNOLONGERONLINE("jailvote", "playernolongeronline"),
    /** The message sent when a player tries to vote again for someone. */
    VOTEALREADYVOTEDFOR("jailvote", "alreadyvotedfor"),

    // Confirming action messages.
    /** The message sent when the sender is already confirming something. */
    ALREADY("confirm"),
    /** The message sent when their confirmation has expired. */
    EXPIRED("confirm"),
    /** The message sent to the sender when they tried to confirm something but don't have anything to confirm. */
    NOTHING("confirm"),
    /** The message sent to the sender when they type something and need to confirm it. */
    START("confirm");

    private String                   section, name, path;
    private static YamlConfiguration lang;

    Lang(String section) {
        this.section = section;
        this.name = toString().toLowerCase();
        this.path = "language." + this.section + "." + this.name;
    }

    Lang(String section, String name) {
        this.section = section;
        this.name = name;
        this.path = "language." + this.section + "." + this.name;
    }

    /**
     * Sets the {@link YamlConfiguration} instance to use.
     *
     * @param file
     *            of the language to use.
     */
    public static void setFile(YamlConfiguration file) {
        lang = file;
    }

    /** Gets the {@link YamlConfiguration} instance. */
    public static YamlConfiguration getFile() {
        return lang;
    }

    /** Writes any new language settings to the language file in storage. */
    public static boolean writeNewLanguage(YamlConfiguration newLang) {
        boolean anything = false;

        for(Lang l : values()) {
            if(!lang.contains(l.path)) {
                lang.set(l.path, newLang.getString(l.path));
                anything = true;
            }
        }

        return anything;
    }

    /** Returns the message in the language, no variables are replaced. */
    public String get() {
        return get(new String[] {});
    }

    /** Returns the message in the language, no variables are replaced. */
    public String get(Lang langString) {
        return get(new String[] { langString.get() });
    }

    /**
     * Returns the message in the language, with the provided variables being replaced.
     *
     * @param variables
     *            All the variables to replace, in order from 0 to however many.
     * @return The message as a colorful message or an empty message if that
     *         isn't defined in the language file.
     */
    public String get(String... variables) {
        String message = lang.getString(path);

        if (message == null) return "";

        for (int i = 0; i < variables.length; i++) {
            message = message.replaceAll("%" + i + "%", variables[i]);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
