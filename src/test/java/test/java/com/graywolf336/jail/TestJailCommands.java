package test.java.com.graywolf336.jail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

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
import com.graywolf336.jail.beans.Prisoner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JailMain.class, PluginDescriptionFile.class })
public class TestJailCommands {
    private static TestInstanceCreator creator;
    private static JailMain main;
    private static final String name = "TestJailCommands";
    
    @BeforeClass
    public static void setUp() throws Exception {
        creator = new TestInstanceCreator();
        assertNotNull("The instance creator is null.", creator);
        assertTrue(creator.setup());
        main = creator.getMain();
        assertNotNull("The JailMain class is null.", main);
        assertTrue("The adding of a jail failed.", creator.addJail(name));
        assertTrue("The jail " + name + " is not a valid jail.", main.getJailManager().isValidJail(name));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        creator.tearDown();
        main = null;
    }
    
    @Test
    public void testJailTimeCommand() throws Exception {
        Long time = Util.getTime("30m");
        assertEquals("The util didn't return the proper time.", 1800000L, time, 0);
        Prisoner p = new Prisoner(creator.getPlayer().getUniqueId().toString(), creator.getPlayer().getName(), true, time, "UnitTeting", "Testing out the jail time command.");
        main.getPrisonerManager().prepareJail(main.getJailManager().getJail(name), null, creator.getPlayer(), p);
        assertTrue("The player " + creator.getPlayer().getName() + " is not jailed, checked by UUID.", main.getJailManager().isPlayerJailed(creator.getPlayer().getUniqueId()));
        assertTrue("The player " + creator.getPlayer().getName() + " is not jailed, checked by username.", main.getJailManager().isPlayerJailedByLastKnownUsername(creator.getPlayer().getName()));
        
        //jail time show graywolf336
        assertTrue("The command failed.", main.onCommand(creator.getCommandSender(), null, "jail", new String[] { "time", "show", creator.getPlayer().getName() }));
        verify(creator.getCommandSender(), atLeast(1)).sendMessage(ChatColor.DARK_GREEN + "graywolf336 has 30 minutes remaining.");
        
        //jail time add graywolf336 30m
        assertTrue("The command failed.", main.onCommand(creator.getCommandSender(), null, "jail", new String[] { "time", "add", creator.getPlayer().getName(), "30m" }));
        verify(creator.getCommandSender(), atLeast(1)).sendMessage(ChatColor.DARK_GREEN + "graywolf336 has 60 minutes remaining.");
        
        //jail time remove graywolf336 10m
        assertTrue("The command failed.", main.onCommand(creator.getCommandSender(), null, "jail", new String[] { "time", "remove", creator.getPlayer().getName(), "10m" }));
        verify(creator.getCommandSender(), atLeast(1)).sendMessage(ChatColor.DARK_GREEN + "graywolf336 has 50 minutes remaining.");
        
        //jail time set graywolf336 25m
        assertTrue("The command failed.", main.onCommand(creator.getCommandSender(), null, "jail", new String[] { "time", "set", creator.getPlayer().getName(), "25m" }));
        verify(creator.getCommandSender(), atLeast(1)).sendMessage(ChatColor.DARK_GREEN + "graywolf336 has 25 minutes remaining.");
    }
}
