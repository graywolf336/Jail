package test.java.com.graywolf336.jail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import test.java.com.graywolf336.jail.util.TestInstanceCreator;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.command.commands.jewels.Jailing;
import com.graywolf336.jail.command.commands.jewels.Transfer;
import com.lexicalscope.jewel.cli.CliFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JailMain.class, PluginDescriptionFile.class })
public class TestJailCommandInfo {
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
    public void testInvalidCommand() {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn("thisisnotavalidcommand");
        String[] args = {};

        CommandSender sender = creator.getCommandSender();

        assertTrue(main.onCommand(sender, command, "thisisnotavalidcommand", args));
        verify(sender).sendMessage(ChatColor.RED + "No commands registered by the name of thisisnotavalidcommand.");
    }

    @Test
    public void testForPlayerContext() {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn("jail");
        String[] args = { "create", "testJail" };

        CommandSender sender = creator.getCommandSender();

        assertTrue(main.onCommand(sender, command, "jail", args));
        verify(sender).sendMessage(ChatColor.RED + "A player context is required for this.");
    }

    @Test
    public void testMinimumArgs() {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn("jail");
        String[] args = { "create" };

        CommandSender sender = creator.getPlayerCommandSender();

        assertTrue(main.onCommand(sender, command, "jail", args));
        verify(sender, atLeast(1)).sendMessage("/jail create [name]"); // If you change which command we test against, then change this
    }

    @Test
    public void testMaximumArgs() {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn("jail");
        String[] args = { "create", "testing", "badarg", "reallyterribleone" };

        CommandSender sender = creator.getPlayerCommandSender();

        assertTrue(main.onCommand(sender, command, "jail", args));
        verify(sender, atLeast(1)).sendMessage("/jail create [name]"); // If you change which command we test against, then change this
    }

    @Test
    public void testSuccessfulJailCreateCommand() {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn("jail");
        String[] args = { "create", "testJail" };

        CommandSender sender = creator.getPlayerCommandSender();

        assertTrue(main.onCommand(sender, command, "jail", args));
        verify(sender).sendMessage(ChatColor.AQUA + "----------Jail Zone Creation----------");
        verify(sender).sendMessage(ChatColor.GREEN + "First, you must select jail cuboid. Select the first point of the cuboid by right clicking on the block with your wooden sword. DO NOT FORGET TO MARK THE FLOOR AND CEILING TOO!");
        verify(sender).sendMessage(ChatColor.AQUA + "--------------------------------------");
    }

    @Test
    public void testJailingCommand() {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn("jail");
        String[] args = { "graywolf336", "-t", "30", "-j", "den", "-c", "cell_01", "-m", "-r", "He", "was", "a", "very", "bad", "boy." };

        CommandSender sender = creator.getPlayerCommandSender();
        assertTrue(main.onCommand(sender, command, "jail", args));
        verify(sender).sendMessage(ChatColor.RED + "There are currently no jails.");
    }

    @Test
    public void testJailJewel() {
        String[] args = { "--player", "graywolf336", "-c", "testing", "-r", "This", "is", "a", "reason" };
        Jailing j = CliFactory.parseArguments(Jailing.class, args);

        assertEquals("graywolf336", j.getPlayer());
        assertTrue("The cell wasn't provided.", j.isCell());
        assertEquals("testing", j.getCell());

        assertTrue("A reason wasn't provided.", j.isReason());
        StringBuilder sb = new StringBuilder();
        for(String s : j.getReason()) {
            sb.append(s).append(' ');
        }

        sb.deleteCharAt(sb.length() - 1);

        assertEquals("This is a reason", sb.toString());
    }

    @Test
    public void testJailWithAnyCellJewel() {
        String[] args = { "--player", "graywolf336", "-a", "-r", "This", "is", "a", "reason" };
        Jailing j = CliFactory.parseArguments(Jailing.class, args);

        assertEquals("graywolf336", j.getPlayer());
        assertTrue("The any cell option wasn't provided.", j.isAnyCell());
        assertTrue("The any cell is false when it should be true.", j.getAnyCell());

        assertTrue("A reason wasn't provided.", j.isReason());
        StringBuilder sb = new StringBuilder();
        for(String s : j.getReason()) {
            sb.append(s).append(' ');
        }

        sb.deleteCharAt(sb.length() - 1);

        assertEquals("This is a reason", sb.toString());
    }

    @Test
    public void testTransferForJailAndCell() {
        String[] args = { "-p", "graywolf336", "-j", "hardcore", "-c", "cell_n01" };
        Transfer t = CliFactory.parseArguments(Transfer.class, args);

        assertTrue("The player wasn't provided.", t.isPlayer());
        assertEquals("The player parsed is not what we expected.", "graywolf336", t.getPlayer());
        assertTrue("The jail wasn't provided.", t.isJail());
        assertEquals("The jail parsed is not what we expected.", "hardcore", t.getJail());
        assertTrue("The cell wasn't provided.", t.isCell());
        assertEquals("The cell parsed is not what we expected.", "cell_n01", t.getCell());
    }

    @Test
    public void testTransferForNoCell() {
        String[] args = { "-p", "graywolf336", "-j", "hardcore" };
        Transfer t = CliFactory.parseArguments(Transfer.class, args);

        assertTrue("The player wasn't provided.", t.isPlayer());
        assertEquals("The player parsed is not what we expected.", "graywolf336", t.getPlayer());
        assertTrue("The jail wasn't provided.", t.isJail());
        assertEquals("The jail parsed is not what we expected.", "hardcore", t.getJail());
        assertFalse("The cell was provided?", t.isCell());
        assertNull("The cell is not null?", t.getCell());
    }
}
