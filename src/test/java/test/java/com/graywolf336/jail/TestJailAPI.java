package test.java.com.graywolf336.jail;

import static org.hamcrest.CoreMatchers.is;
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
}
