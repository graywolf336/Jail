package test.java.com.graywolf336.jail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import test.java.com.graywolf336.jail.util.TestInstanceCreator;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.Util;
import com.graywolf336.jail.enums.Lang;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JailMain.class, PluginDescriptionFile.class })
public class TestJailLanguage {
    private static TestInstanceCreator creator;
    private static JailMain main;
    private UUID id = UUID.randomUUID();

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
    public void testActionsLanguage() {
        assertEquals("breaking a block", Lang.BLOCKBREAKING.get());
        assertEquals("placing a block", Lang.BLOCKPLACING.get());
        assertEquals("trying to use a command", Lang.COMMAND.get());
        assertEquals("trampling crops", Lang.CROPTRAMPLING.get());
        assertEquals("interacting with a block", Lang.INTERACTIONBLOCKS.get());
        assertEquals("interacting with an item", Lang.INTERACTIONITEMS.get());
        assertEquals("trying to escape", Lang.MOVING.get());
    }

    @Test
    public void testConfirmLanguage() {
        assertEquals(colorize("&cYou are already confirming something else, please type '&b/jail confirm&c' to confirm it."), Lang.ALREADY.get());
        assertEquals(colorize("&cYour confirmation expired already, retype what you are trying to confirm."), Lang.EXPIRED.get());
        assertEquals(colorize("&cYou are not confirming anything."), Lang.NOTHING.get());
        assertEquals(colorize("&cPlease type '&b/jail confirm&c' to confirm we should continue."), Lang.START.get());
    }

    @Test
    public void testGeneralLanguage() {
        assertEquals("all the jails", Lang.ALLJAILS.get());
        assertEquals(colorize("&cThe removal of cell cell_01 from jail cloud was unsuccessful because there is a prisoner in there still. Release or transfer before trying to remove the cell again."), Lang.CELLREMOVALUNSUCCESSFUL.get(new String[] { "cell_01", "cloud" }));
        assertEquals(colorize("&9Cell cell_01 has been successfully removed from the jail cloud."), Lang.CELLREMOVED.get(new String[] { "cell_01", "cloud" }));
        assertEquals(colorize("&9jailing"), Lang.JAILING.get());
        assertEquals(colorize("&cThe removal of the jail cloud was unsuccessful because there are prisoners in there, please release or transfer them first."), Lang.JAILREMOVALUNSUCCESSFUL.get(new String[] { "cloud" }));
        assertEquals(colorize("&9Jail cloud has been successfully deleted."), Lang.JAILREMOVED.get(new String[] { "cloud" }));
        assertEquals(colorize("&3Jail stick usage: &cdisabled"), Lang.JAILSTICKDISABLED.get());
        assertEquals(colorize("&3Jail stick usage: &aenabled"), Lang.JAILSTICKENABLED.get());
        assertEquals(colorize("&cThe usage of Jail Sticks has been disabled by the Administrator."), Lang.JAILSTICKUSAGEDISABLED.get());
        assertEquals(colorize("&cNo cell found by the name of cell_01 in the jail cloud."), Lang.NOCELL.get(new String[] { "cell_01", "cloud" }));
        assertEquals(colorize("&cNo cells found in the jail cloud."), Lang.NOCELLS.get("cloud"));
        assertEquals(colorize("&cNo jail found by the name of cloud."), Lang.NOJAIL.get("cloud"));
        assertEquals(colorize("&cThere are currently no jails."), Lang.NOJAILS.get());
        assertEquals(colorize("&cInsufficient permission."), Lang.NOPERMISSION.get());
        assertEquals(colorize("&cNumber format is incorrect."), Lang.NUMBERFORMATINCORRECT.get());
        assertEquals(colorize("&cA player context is required for this."), Lang.PLAYERCONTEXTREQUIRED.get());
        assertEquals(colorize("&cThat player is not online!"), Lang.PLAYERNOTONLINE.get());
        assertEquals(colorize("&9Jail data successfully reloaded."), Lang.PLUGINRELOADED.get());
        assertEquals(colorize("&cAll the prisoners from cloud have been cleared."), Lang.PRISONERSCLEARED.get("cloud"));
        assertEquals(colorize("&7[MM/dd/yyyy HH:mm:ss]: &9graywolf336 &fjailed by &9console &ffor &960 &fminutes with a reason of &9doing terrible coding&f. [" + id.toString() + "]"), Lang.RECORDENTRY.get(new String[] { "MM/dd/yyyy HH:mm:ss", "graywolf336", "console", "60", "doing terrible coding", id.toString() }));
        assertEquals(colorize("&9transferring"), Lang.TRANSFERRING.get());
        assertEquals(colorize("&cNo commands registered by the name of invalidcommand."), Lang.UNKNOWNCOMMAND.get("invalidcommand"));
    }

    @Test
    public void testJailingLanguage() {
        assertEquals(colorize("&cYou can not be afk while being jailed."), Lang.AFKKICKMESSAGE.get());
        assertEquals(colorize("&cgraywolf336 is already jailed."), Lang.ALREADYJAILED.get("graywolf336"));
        assertEquals(colorize("&9graywolf336 has been jailed forever for: &cSome cool reason."), Lang.BROADCASTMESSAGEFOREVER.get(new String[] { "graywolf336", "Some cool reason." }));
        assertEquals(colorize("&9graywolf336 has been jailed for 60 minutes for: &cSome cool reason."), Lang.BROADCASTMESSAGEFORMINUTES.get(new String[] { "graywolf336", "60", "Some cool reason."}));
        assertEquals(colorize("&9graywolf336 has been unjailed by console."), Lang.BROADCASTUNJAILING.get(new String[] { "graywolf336", "console" }));
        assertEquals(colorize("&cJailing graywolf336 was cancelled by another plugin and they did not leave you a message."), Lang.CANCELLEDBYANOTHERPLUGIN.get("graywolf336"));
        assertEquals(colorize("&cThat player can not be jailed."), Lang.CANTBEJAILED.get());
        assertEquals(colorize("&cThe destination cell, cell_n01, already has a prisoner in it."), Lang.CELLNOTEMPTY.get("cell_n01"));
        assertEquals("Al Capone", Lang.DEFAULTJAILER.get());
        assertEquals("Breaking the rules.", Lang.DEFAULTJAILEDREASON.get());
        assertEquals(colorize("&cgraywolf336 was forcefully unjailed."), Lang.FORCEUNJAILED.get("graywolf336"));
        assertEquals(colorize("&cYou have been jailed!"), Lang.JAILED.get());
        assertEquals(colorize("&cYou have been jailed for: terrible coding"), Lang.JAILEDWITHREASON.get("terrible coding"));
        assertEquals(colorize("&cStop talking, you are in jail."), Lang.MUTED.get());
        assertEquals(colorize("&cNo empty cells were found for cloud, all them appear to be full."), Lang.NOEMPTYCELLS.get("cloud"));
        assertEquals(colorize("&9cloud is empty and contains no prisoners."), Lang.NOPRISONERS.get("cloud"));
        assertEquals(colorize("&cgraywolf336 is not jailed."), Lang.NOTJAILED.get("graywolf336"));
        assertEquals(colorize("&9graywolf336 is now muted."), Lang.NOWMUTED.get("graywolf336"));
        assertEquals(colorize("&9graywolf336 is now unmuted."), Lang.NOWUNMUTED.get("graywolf336"));
        assertEquals(colorize("&2graywolf336 is offline and will be jailed when they next come online for 60 minutes."), Lang.OFFLINEJAIL.get(new String[] { "graywolf336", "60" }));
        assertEquals(colorize("&2graywolf336 was jailed for 60 minutes."), Lang.ONLINEJAIL.get(new String[] { "graywolf336", "60" }));
        assertEquals(colorize("&2graywolf336 has 60 minutes remaining."), Lang.PRISONERSTIME.get(new String[] { "graywolf336", "60" }));
        assertEquals(colorize("&c60 minutes have been added to your time for block breaking."), Lang.PROTECTIONMESSAGE.get(new String[] { "60", "block breaking" }));
        assertEquals(colorize("&cProtection enabled for block breaking, do not do it again."), Lang.PROTECTIONMESSAGENOPENALTY.get("block breaking"));
        assertEquals(colorize("&cPlease provide a player when jailing &cthem."), Lang.PROVIDEAPLAYER.get("jailing"));
        assertEquals(colorize("&cPlease provide a jail to jail &cthem to."), Lang.PROVIDEAJAIL.get("jail"));
        assertEquals(colorize("&cgraywolf336 has resisted arrest thanks to having more health than you can jail at."), Lang.RESISTEDARRESTJAILER.get("graywolf336"));
        assertEquals(colorize("&cYou have resisted arrest due to having more health than graywolf336 can jail at."), Lang.RESISTEDARRESTPLAYER.get("graywolf336"));
        assertEquals(colorize("&2You have been jailed with a reason of 'terrible coding' by graywolf336 and have 60 minutes remaining (1h0m0s)."), Lang.STATUS.get(new String[] { "terrible coding", "graywolf336", "60", Util.getDurationBreakdown(3600000) }));
        assertEquals(colorize("&cAn empty cell in the same jail, cloud, was found: cell_n02"), Lang.SUGGESTEDCELL.get(new String[] { "cloud", "cell_n02" }));
        assertEquals(colorize("&9Teleported graywolf336 to cloud's teleport in location."), Lang.TELEIN.get(new String[] { "graywolf336", "cloud" }));
        assertEquals(colorize("&9Teleported graywolf336 to cloud's teleport out location."), Lang.TELEOUT.get(new String[] { "graywolf336", "cloud" }));
        assertEquals(colorize("&2Successfully transferred all the prisoners from cloud to battle."), Lang.TRANSFERALLCOMPLETE.get(new String[] { "cloud", "battle" }));
        assertEquals(colorize("&cTransferring graywolf336 was cancelled by another plugin and they did not leave you a message."), Lang.TRANSFERCANCELLEDBYANOTHERPLUGIN.get("graywolf336"));
        assertEquals(colorize("&2Successfully transferred cloud to battle in the cell cell_n01."), Lang.TRANSFERCOMPLETECELL.get(new String[] { "cloud", "battle", "cell_n01" }));
        assertEquals(colorize("&2Successfully transferred cloud to battle."), Lang.TRANSFERCOMPLETENOCELL.get(new String[] { "cloud", "battle" }));
        assertEquals(colorize("&9You have been transferred to cloud."), Lang.TRANSFERRED.get("cloud"));
        assertEquals(colorize("&2You have been released! Please respect the server rules."), Lang.UNJAILED.get());
        assertEquals(colorize("&2graywolf336 has been released from jail."), Lang.UNJAILSUCCESS.get("graywolf336"));
        assertEquals(colorize("&2graywolf336 will be released the next time they log on."), Lang.WILLBEUNJAILED.get("graywolf336"));
        assertEquals(colorize("&4cloud is located in a world that is not loaded, jailing to it is disabled."), Lang.WORLDUNLOADED.get("cloud"));
        assertEquals(colorize("&4You are jailed in a jail that is located in a world which is currently unloaded, try again later."), Lang.WORLDUNLOADEDKICK.get());
        assertEquals(colorize("&2You are not jailed."), Lang.YOUARENOTJAILED.get());
    }

    @Test
    public void testJailPayLanguage() {
        assertEquals(colorize("&cYou are jailed and as a result you can not pay for others."), Lang.PAYCANTPAYWHILEJAILED.get());
        assertEquals(colorize("&9You have to pay 50 mirs per minute which equates to 1000 mirs."), Lang.PAYCOST.get(new String[] { "50", "mirs", "1000" }));
        assertEquals(colorize("&9Since you are jailed forever, you have to pay 1020203 mirs."), Lang.PAYCOSTINFINITE.get(new String[] { "1020203", "mirs" }));
        assertEquals(colorize("&cYou can not pay negative amounts."), Lang.PAYNONEGATIVEAMOUNTS.get());
        assertEquals(colorize("&cSorry, money won't help this time."), Lang.PAYNOTENABLED.get());
        assertEquals(colorize("&cYou don't have that much money!"), Lang.PAYNOTENOUGHMONEY.get());
        assertEquals(colorize("&cYou have not provided enough money to cover the sentence!"), Lang.PAYNOTENOUGHMONEYPROVIDED.get());
        assertEquals(colorize("&2You have just payed 503030 and released yourself from jail!"), Lang.PAYPAIDRELEASED.get("503030"));
        assertEquals(colorize("&2You have just payed 50000 and released graywolf336 from jail!"), Lang.PAYPAIDRELEASEDELSE.get(new String[] { "50000", "graywolf336" }));
        assertEquals(colorize("&2You have just payed 50000 and lowered your sentence to 10 minutes!"), Lang.PAYPAIDLOWEREDTIME.get(new String[] { "50000", "10" }));
        assertEquals(colorize("&2You have just payed 500 and lowered graywolf336's sentence to 15 minutes!"), Lang.PAYPAIDLOWEREDTIMEELSE.get(new String[] { "500", "graywolf336", "15" }));
    }

    @Test
    public void tesetHandCuffingLanguage() {
        assertEquals(colorize("&9graywolf336 &ccan not be handcuffed."), Lang.CANTBEHANDCUFFED.get("graywolf336"));
        assertEquals(colorize("&9graywolf336 &cis currently jailed, you can not handcuff a prisoner."), Lang.CURRENTLYJAILEDHANDCUFF.get("graywolf336"));
        assertEquals(colorize("&9graywolf336 &cdoes not have any handcuffs to remove!"), Lang.NOTHANDCUFFED.get("graywolf336"));
        assertEquals(colorize("&9graywolf336 &ahas been handcuffed!"), Lang.HANDCUFFSON.get("graywolf336"));
        assertEquals(colorize("&cYou have been handcuffed."), Lang.HANDCUFFED.get());
        assertEquals(colorize("&9graywolf336 &ahas been released from their handcuffs."), Lang.HANDCUFFSRELEASED.get("graywolf336"));
        assertEquals(colorize("&aYour handcuffs have been removed."), Lang.UNHANDCUFFED.get());
    }

    private String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
