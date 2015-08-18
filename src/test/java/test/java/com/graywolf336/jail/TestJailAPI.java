package test.java.com.graywolf336.jail;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import test.java.com.graywolf336.jail.util.TestInstanceCreator;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.JailsAPI;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JailMain.class, PluginDescriptionFile.class })
public class TestJailAPI {
    private static TestInstanceCreator creator;
    private static JailMain main;

    @BeforeClass
    public static void setUp() throws Exception {
        creator = new TestInstanceCreator();
        assertNotNull("The instance creator is null.", creator);
        assertTrue(creator.setup());
        main = creator.getMain();
        assertNotNull("The JailMain class is null.", main);
        assertTrue("The adding of a jail failed.", creator.addJail("TestJailAPI"));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        creator.tearDown();
        main = null;
    }

    @Test
    public void testManagersAreThere() {
        assertNotNull(main.getHandCuffManager());
        assertNotNull(main.getJailManager());
        assertNotNull(main.getPrisonerManager());
        assertNotNull(main.getJailVoteManager());
    }
    
    @Test
    public void verifyManagersAreTheSame() {
        assertThat("The HandCuff Manager references are different.", JailsAPI.getHandCuffManager(), is(main.getHandCuffManager()));
        assertThat("The Jail Manager references are different.", JailsAPI.getJailManager(), is(main.getJailManager()));
        assertThat("The Prisoner Manager references are different.", JailsAPI.getPrisonerManager(), is(main.getPrisonerManager()));
        assertThat("The Jail Stick Manager references are different.", JailsAPI.getJailStickManager(), is(main.getJailStickManager()));
        assertThat("The Jail Vote Manager references are different.", JailsAPI.getJailVoteManager(), is(main.getJailVoteManager()));
    }

    @Test
    public void testHandCuffManagerAPI() {
        UUID id = UUID.randomUUID();
        Location loc = new Location(main.getServer().getWorld("world"), 11.469868464778077, 65.0, -239.27944647045672, Float.valueOf("38.499817"), Float.valueOf("2.0000453"));
        assertFalse("The test id of someone is already handcuffed.", JailsAPI.getHandCuffManager().isHandCuffed(id));

        JailsAPI.getHandCuffManager().addHandCuffs(id, loc);
        assertTrue(JailsAPI.getHandCuffManager().isHandCuffed(id));
        assertThat(JailsAPI.getHandCuffManager().getLocation(id), is(loc));

        JailsAPI.getHandCuffManager().removeHandCuffs(id);
        assertFalse(JailsAPI.getHandCuffManager().isHandCuffed(id));
        assertNull(JailsAPI.getHandCuffManager().getLocation(id));
    }
    
    @Test
    public void testJailManagerAPI() {
        assertThat("The Jail Manager reference to the plugin is different.", JailsAPI.getJailManager().getPlugin(), is(main));
        assertNotNull("The getJails method returned a null value.", JailsAPI.getJailManager().getJails());
        assertEquals("There isn't a Jail in the returned Jails.", 1, JailsAPI.getJailManager().getJails().size());
        assertThat("Jail Names aren't equal..", new String[] { "TestJailAPI" }, is(JailsAPI.getJailManager().getJailNames()));
        assertTrue("The adding of a new jail, furtherTesting, wasn't successful.", creator.addJail("furtherTesting"));
        assertTrue("The added jail, furtherTesting, doesn't exist.", JailsAPI.getJailManager().isValidJail("furtherTesting"));
        JailsAPI.getJailManager().removeJail("furtherTesting");
        assertFalse("The jail furtherTesting wasn't successfully removed.", JailsAPI.getJailManager().isValidJail("furtherTesting"));
        assertNull("The jail furtherTesting is not null.", JailsAPI.getJailManager().getJail("furtherTesting"));
        assertNotNull("The jail TestJailAPI is null.", JailsAPI.getJailManager().getJail("TestJailAPI"));
        assertNotNull("An empty string returned a null value even though there is one jail.", JailsAPI.getJailManager().getJail(""));
        assertNotNull("The first jail is null from the console sender.", JailsAPI.getJailManager().getNearestJail(creator.getCommandSender()));
        assertNotNull("The nearest jail from the player sender is null.", JailsAPI.getJailManager().getNearestJail(creator.getPlayerCommandSender()));
        assertNotNull("The cells object returned is null.", JailsAPI.getJailManager().getAllCells());
        assertTrue("There are some cells even though there shouldn't be.", JailsAPI.getJailManager().getAllCells().isEmpty());
    }
}
