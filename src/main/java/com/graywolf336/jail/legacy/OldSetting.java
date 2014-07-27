package com.graywolf336.jail.legacy;

import java.util.ArrayList;
import java.util.Arrays;

public enum OldSetting {

    Debug("Debug", false),//done
    BroadcastJailMessage("Broadcast Jail Message", false),//done
    AllowUpdateNotifications("AllowUpdateNotifications", true),//done
    ExecutedCommandsOnJail("ExecutedCommandsOnJail", new ArrayList<String>()),//done
    ExecutedCommandsOnRelease("ExecutedCommandsOnRelease", new ArrayList<String>()),//done
    AutomaticMute("AutomaticMute", false),//done
    StoreInventory("StoreInventory", true),//done
    CanPrisonerOpenHisChest("CanPrisonerOpenHisChest", true),//done
    LogJailingIntoConsole("LogJailingIntoConsole", false),//done
    CountdownTimeWhenOffline("CountdownTimeWhenOffline", false),//done
    ReleaseBackToPreviousPosition("ReleaseBackToPreviousPosition", false),//done
    IgnorePrisonersSleepingState("IgnorePrisonersSleepingState", true),//done
    TeleportPrisonerOnRelease("TeleportPrisonerOnRelease", true),//done
    DefaultJailTime("DefaultJailTime", -1),//done
    UseBukkitSchedulerTimer("UseBukkitSchedulerTimer", true),//done
    JailCheckPrisonersPerPage("JailCheckPrisonersPerPage", 15),//TODO

    //JailStick
    EnableJailStick("EnableJailStick", false),//done
    JailStickParameters("JailStickParameters", "280,5,10,,police;50,5,20,,admin"),//done

    //Protections
    EnableBlockDestroyProtection("Protections.EnableBlockDestroyProtection", true),//done
    BlockDestroyPenalty("Protections.BlockDestroyPenalty", 15),//done
    BlockDestroyProtectionExceptions("Protections.BlockDestroyProtectionExceptions", Arrays.asList(new String[]{"59"})),//done
    EnableBlockPlaceProtection("Protections.EnableBlockPlaceProtection", true),//done
    BlockPlacePenalty("Protections.BlockPlacePenalty", 10),//done
    BlockPlaceProtectionExceptions("Protections.BlockPlaceProtectionExceptions", Arrays.asList(new String[]{"59"})),//done
    EnablePlayerMoveProtection("Protections.EnablePlayerMoveProtection", true),//done
    PlayerMoveProtectionPenalty("Protections.PlayerMoveProtectionPenalty", 30),//done
    PlayerMoveProtectionAction("Protections.PlayerMoveProtectionAction", "teleport"),//TODO
    WhitelistedCommands("Protections.WhitelistedCommands", new ArrayList<String>()),//done
    CommandProtectionPenalty("Protections.CommandProtectionPenalty", 10),//done
    InteractionPenalty("Protections.InteractionPenalty", 10),//done
    EnableExplosionProtection("Protections.EnableExplosionProtection", true),//done
    EnablePVPProtection("Protections.EnablePVPProtection", true),//TODO: ??? I haven't even tested this out!!!!
    EnableChangingPermissions("Protections.EnableChangingPermissions", false),//TODO
    PrisonersPermissionsGroups("Protections.PrisonersPermissionsGroups", Arrays.asList("prisoners")),//TODO
    RestorePermissionsToEscapedPrisoners("Protections.RestorePermissionsToEscapedPrisoners", true),//TODO
    EnableFoodControl("Protections.EnableFoodControl", true),//done
    FoodControlMinimumFood("Protections.FoodControlMinimumFood", 10),//done
    FoodControlMaximumFood("Protections.FoodControlMaximumFood", 20),//done
    PrisonersRecieveMessages("Protections.PlayerRecievesMessages", true), //done

    //JailPay
    PricePerMinute("JailPay.PricePerMinute", 10),//done
    PriceForInfiniteJail("JailPay.PriceForInfiniteJail", 9999),//done
    JailPayCurrency("JailPay.Currency", 0),//done

    //Guards
    GuardHealth("Guards.GuardHealth", 8),//TODO
    GuardArmor("Guards.GuardArmor", 0),//TODO
    GuardDamage("Guards.GuardDamage", 2),//TODO
    NumbefOfGuards("Guards.NumberOfGuards", 3),//TODO
    GuardInvincibility("Guards.GuardInvincibility", false),//TODO
    GuardAttackSpeedPercent("Guards.GuardAttackSpeedPercent", 100),//TODO
    RespawnGuards("Guards.RespawnGuards", true),//TODO
    GuardTeleportDistance("Guards.GuardTeleportDistance", 10),//TODO
    GuardTypes("Guards.GuardTypes", Arrays.asList(new String[] { "Zombie", "Silverfish" })),//TODO

    //Database
    UseMySQL("Database.UseMySQL", false),//done
    MySQLConn("Database.MySQLConn", "jdbc:mysql://localhost:3306/minecraft"),//done
    MySQLUsername("Database.MySQLUSername", "root"),//done
    MySQLPassword("Database.MySQLPassword", "password"),//done

    //Jail Vote
    VoteJailEnabled("Jail Vote.Enabled", true),//TODO
    VoteJailTime("Jail Vote.Time", 5),//TODO
    VoteJailReason("Jail Vote.Reason", "Jailed by players"),//TODO
    VoteJailVoteTime("Jail Vote.Vote Time", 60),//TODO

    //Jail swearing
    EnableJailSwear("Jail Swear.Enabled", false),//TODO
    JailSwearTime("Jail Swear.Time", 10),//TODO
    BannedWords("Jail Swear.Banned Words", Arrays.asList(new String[] {"shit", "crap", "fuck", "cunt"}));//TODO

    private String name;
    private Object def;

    private OldSetting(String Name, Object Def) {
        name = Name;
        def = Def;
    }

    /** The string of the node the value is stored at in the config. */
    public String getString() {
        return name;
    }

    /** The default value for this config. */
    public Object getDefault() {
        return def;
    }
}
