package test.java.com.graywolf336.jail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import test.java.com.graywolf336.jail.util.TestInstanceCreator;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.enums.Settings;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JailMain.class, PluginDescriptionFile.class })
public class TestJailDefaultConfig {
    private static TestInstanceCreator creator;
    private static JailMain main;

    @BeforeClass
    public static void setUp() throws Exception {
        creator = new TestInstanceCreator();
        assertNotNull("The instance creator is null.", creator);
        assertTrue(creator.setup());
        main = creator.getMain();
        assertNotNull("The JailMain class is null.", main);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        creator.tearDown();
        main = null;
    }

    @Test
    public void testSystemDefaultConfig() {
        assertEquals("The config version is not 3.", 3, main.getConfig().getInt(Settings.CONFIGVERSION.getPath()));
        assertTrue("Default debugging is off.", main.getConfig().getBoolean(Settings.DEBUG.getPath()));
        assertEquals("Default language is not 'en'.", "en", main.getConfig().getString(Settings.LANGUAGE.getPath()));
        assertEquals("Default updating channel is not bukkit.", "bukkit", main.getConfig().getString(Settings.UPDATECHANNEL.getPath()));
        assertTrue("Default updating notifications is false.", main.getConfig().getBoolean(Settings.UPDATENOTIFICATIONS.getPath()));
        assertEquals("Default updating time checking is not 1h.", "1h", main.getConfig().getString(Settings.UPDATETIME.getPath()));
        assertTrue("Default usage of bukkit timer is false.", main.getConfig().getBoolean(Settings.USEBUKKITTIMER.getPath()));
    }

    @Test
    public void testStorageDefaultConfig() {
        assertEquals("The default storage system is not flatfile.", "flatfile", main.getConfig().getString("storage.type"));
        assertEquals("The default mysql host is not localhost.", "localhost", main.getConfig().getString("storage.mysql.host"));
        assertEquals("The default mysql port is not 3306.", 3306, main.getConfig().getInt("storage.mysql.port"));
        assertEquals("The default mysql username is not root.", "root", main.getConfig().getString("storage.mysql.username"));
        assertEquals("The default mysql password is not password.", "password", main.getConfig().getString("storage.mysql.password"));
        assertEquals("The default mysql database is not jailDatabase.", "jailDatabase", main.getConfig().getString("storage.mysql.database"));
        assertEquals("The default mysql prefix is not j3_.", "j3_", main.getConfig().getString("storage.mysql.prefix"));
    }

    @Test
    public void testDuringJailingDefaultConfig() {
        // block breaking section
        assertEquals("Default setting for block break penalty is not 5m.", "5m", main.getConfig().getString(Settings.BLOCKBREAKPENALTY.getPath()));
        assertTrue("Default setting for block break proection is false.", main.getConfig().getBoolean(Settings.BLOCKBREAKPROTECTION.getPath()));

        List<String> blockbreakwhite = main.getConfig().getStringList(Settings.BLOCKBREAKWHITELIST.getPath());
        assertTrue("Default block breaking doesn't contain crops.", blockbreakwhite.contains("wheat"));
        assertTrue("Default block breaking doesn't contain carrot.", blockbreakwhite.contains("carrot"));
        assertTrue("Default block breaking doesn't contain potato.", blockbreakwhite.contains("potato"));

        // block placing section
        assertEquals("Default setting for block place penalty is not 5m.", "5m", main.getConfig().getString(Settings.BLOCKPLACEPENALTY.getPath()));
        assertTrue("Default setting for block place proection is false.", main.getConfig().getBoolean(Settings.BLOCKPLACEPROTECTION.getPath()));

        List<String> blockplacewhite = main.getConfig().getStringList(Settings.BLOCKPLACEWHITELIST.getPath());
        assertTrue("Default block placing whitelist doesn't contain crops.", blockplacewhite.contains("wheat"));
        assertTrue("Default block placing whitelist doesn't contain carrot.", blockplacewhite.contains("carrot"));
        assertTrue("Default block placing whitelist doesn't contain potato.", blockplacewhite.contains("potato"));

        // command protection section
        assertEquals("Default setting for command penalty is not 5m.", "5m", main.getConfig().getString(Settings.COMMANDPENALTY.getPath()));
        assertTrue("Default setting for command proection is false.", main.getConfig().getBoolean(Settings.COMMANDPROTECTION.getPath()));

        List<String> commandwhite = main.getConfig().getStringList(Settings.COMMANDWHITELIST.getPath());
        assertTrue("Default command whitelist doesn't contain /ping.", commandwhite.contains("/ping"));
        assertTrue("Default command whitelist doesn't contain /list.", commandwhite.contains("/list"));
        assertTrue("Default command whitelist doesn't contain /jail status.", commandwhite.contains("/jail status"));
        assertTrue("Default command whitelist doesn't contain /jail pay.", commandwhite.contains("/jail pay"));

        assertFalse("Default setting for counting down time while prisoner is offline is true.", main.getConfig().getBoolean(Settings.COUNTDOWNTIMEOFFLINE.getPath()));
        assertEquals("Default setting for crop trampling penalty is not 5m.", "5m", main.getConfig().getString(Settings.CROPTRAMPLINGPENALTY.getPath()));
        assertTrue("Default setting for crop trampling proection is false.", main.getConfig().getBoolean(Settings.CROPTRAMPLINGPROTECTION.getPath()));
        assertTrue("Default setting for food control enabled is false.", main.getConfig().getBoolean(Settings.FOODCONTROL.getPath()));
        assertEquals("Default setting for food control max is not 20.", 20, main.getConfig().getInt(Settings.FOODCONTROLMAX.getPath()));
        assertEquals("Default setting for food control min is not 10.", 10, main.getConfig().getInt(Settings.FOODCONTROLMIN.getPath()));
        assertTrue("Default setting for ignoring sleeping is false.", main.getConfig().getBoolean(Settings.IGNORESLEEPINGSTATE.getPath()));
        assertEquals("Default setting max afk time is not 10 minutes.", "10m", main.getConfig().getString(Settings.MAXAFKTIME.getPath()));
        assertEquals("Default setting move penalty is not 10 minutes.", "10m", main.getConfig().getString(Settings.MOVEPENALTY.getPath()));
        assertTrue("Default setting for move protection is false.", main.getConfig().getBoolean(Settings.MOVEPROTECTION.getPath()));
        assertTrue("Default setting for opening a chest is false.", main.getConfig().getBoolean(Settings.PRISONEROPENCHEST.getPath()));

        // interaction blocks protection section
        List<String> interactionBlocks = main.getConfig().getStringList(Settings.PREVENTINTERACTIONBLOCKS.getPath());
        assertTrue("Default interaction blocks whitelist doesn't contain wooden_door.", interactionBlocks.contains("oak_door"));
        assertTrue("Default interaction blocks whitelist doesn't contain iron_door_block.", interactionBlocks.contains("iron_door"));
        assertEquals("Default setting for preventing interaction blocks penalty is not 5m.", "5m", main.getConfig().getString(Settings.PREVENTINTERACTIONBLOCKSPENALTY.getPath()));

        // interaction items protection section
        assertTrue("Default interaction items whitelist isn't empty.", main.getConfig().getStringList(Settings.PREVENTINTERACTIONITEMS.getPath()).isEmpty());
        assertEquals("Default setting for preventing interaction blocks penalty is not 5m.", "5m", main.getConfig().getString(Settings.PREVENTINTERACTIONBLOCKSPENALTY.getPath()));

        assertTrue("Default setting for recieving messages is false.", main.getConfig().getBoolean(Settings.RECIEVEMESSAGES.getPath()));
        assertTrue("Default setting for scoreboard being enabled is false.", main.getConfig().getBoolean(Settings.SCOREBOARDENABLED.getPath()));
        assertEquals("Default setting for the scoreboard title is not Jail Info.", "Jail Info", main.getConfig().getString(Settings.SCOREBOARDTITLE.getPath()));
        assertEquals("Default setting for the scoreboard time language is not &aTime:", "&aTime:", main.getConfig().getString(Settings.SCOREBOARDTIME.getPath()));
    }

    @Test
    public void testJailingDefaultConfig() {
        assertFalse("Default setting for allowing jailing offline players is true.", main.getConfig().getBoolean(Settings.ALLOWJAILINGNEVERPLAYEDBEFOREPLAYERS.getPath()));
        assertTrue("Default setting for automatically jailing in cells is false.", main.getConfig().getBoolean(Settings.AUTOMATICCELL.getPath()));
        assertTrue("Default setting for automatically muting is false.", main.getConfig().getBoolean(Settings.AUTOMATICMUTE.getPath()));
        assertFalse("Default setting for broadcasting a jailing is true.", main.getConfig().getBoolean(Settings.BROADCASTJAILING.getPath()));
        assertTrue("Default setting for jail clothing is not enabled.", main.getConfig().getBoolean(Settings.CLOTHINGENABLED.getPath()));
        assertEquals("Default setting for jail clothing's helmet is not leather_helmet~175,105,33.", "leather_helmet~175,105,33", main.getConfig().getString(Settings.CLOTHINGHELMET.getPath()));
        assertEquals("Default setting for jail clothing's chestplate is not leather_chestplate~175,105,33.", "leather_chestplate~175,105,33", main.getConfig().getString(Settings.CLOTHINGCHEST.getPath()));
        assertEquals("Default setting for jail clothing's legs is not leather_leggings~175,105,33.", "leather_leggings~175,105,33", main.getConfig().getString(Settings.CLOTHINGLEGS.getPath()));
        assertEquals("Default setting for jail clothing's boots is not leather_boots~175,105,33.", "leather_boots~175,105,33", main.getConfig().getString(Settings.CLOTHINGBOOTS.getPath()));
        assertTrue("Default setting for commands is not empty.", main.getConfig().getStringList(Settings.COMMANDSONJAIL.getPath()).isEmpty());
        assertEquals("Default setting for default jail is not 'nearest' but something else.", "nearest", main.getConfig().getString(Settings.DEFAULTJAIL.getPath()));
        assertEquals("Default setting for time is not 30 minutes.", "30m", main.getConfig().getString(Settings.DEFAULTTIME.getPath()));
        assertEquals("Default setting for the prisoner's default gamemode is not adventure.", "adventure", main.getConfig().getString(Settings.JAILEDGAMEMODE.getPath()));
        assertTrue("Default setting for inventory storing blacklist is not empty.", main.getConfig().getStringList(Settings.JAILEDINVENTORYBLACKLIST.getPath()).isEmpty());
        assertTrue("Default setting for inventory storing is false.", main.getConfig().getBoolean(Settings.JAILEDSTOREINVENTORY.getPath()));
        assertTrue("Default setting for logging to console when someone is jailed is false.", main.getConfig().getBoolean(Settings.LOGJAILINGTOCONSOLE.getPath()));
        assertTrue("Default setting for logging to prisoner's profile is false.", main.getConfig().getBoolean(Settings.LOGJAILINGTOPROFILE.getPath()));
    }

    @Test
    public void testReleaseDefaultConfig() {
        assertFalse("Default setting for releasing back to previous position is true.", main.getConfig().getBoolean(Settings.RELEASETOPREVIOUSPOSITION.getPath()));
        assertTrue("Default setting for commands is not empty.", main.getConfig().getStringList(Settings.COMMANDSONRELEASE.getPath()).isEmpty());
        assertFalse("Default setting for restoring previous gamemode is true.", main.getConfig().getBoolean(Settings.RESTOREPREVIOUSGAMEMODE.getPath()));
        assertTrue("Default setting for teleporting them out of the jail is false.", main.getConfig().getBoolean(Settings.TELEPORTONRELEASE.getPath()));
    }

    @Test
    public void testJailsDefaultConfig() {
        assertTrue("Default setting for protecting jails from endermen is false.", main.getConfig().getBoolean(Settings.ENDERMENPROTECTION.getPath()));
        assertTrue("Default setting for protecting jails from explosions is false.", main.getConfig().getBoolean(Settings.EXPLOSIONPROTECTION.getPath()));
    }

    @Test
    public void testJailPayDefaultConfig() {
        assertFalse("Default setting for jail pay enabled is true, meaning Jail Pay somehow got enabled in testing.", main.getConfig().getBoolean(Settings.JAILPAYENABLED.getPath()));
        assertEquals("Default setting for jail pay item is not air.", "air", main.getConfig().getString(Settings.JAILPAYITEM.getPath()));
        assertEquals("Default setting for jail pay price per minute is not 1.5.", 1.5, main.getConfig().getDouble(Settings.JAILPAYPRICEPERMINUTE.getPath()), 0);
        assertEquals("Default setting for jail pay price for infinite is not 10000.", 10000, main.getConfig().getDouble(Settings.JAILPAYPRICEINFINITE.getPath()), 0);
    }

    @Test
    public void testJailStickDefaultConfig() {
        assertTrue("Default setting for jail stick enabled is false.", main.getConfig().getBoolean(Settings.JAILSTICKENABLED.getPath()));
        List<String> jailSticks = main.getConfig().getStringList(Settings.JAILSTICKSTICKS.getPath());
        assertTrue("Default setting for jail sticks doesn't contain a stick with item of stick.", jailSticks.contains("stick,30m,,Running away,-1"));
        assertTrue("Default setting for jail sticks doesn't contain a stick with item of blaze_rod.", jailSticks.contains("blaze_rod,15m,,Having too much fun,6"));
    }
}
