package test.java.com.graywolf336.jail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import test.java.com.graywolf336.jail.util.TestInstanceCreator;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.beans.CachePrisoner;
import com.graywolf336.jail.beans.Prisoner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JailMain.class, PluginDescriptionFile.class })
public class BenchmarkTest extends AbstractBenchmark {
    private static TestInstanceCreator creator;
    private static JailMain main;
    private static UUID use;
    private static Random r;

    @BeforeClass
    public static void setUp() throws Exception {
        creator = new TestInstanceCreator();
        assertNotNull("The instance creator is null.", creator);
        assertTrue(creator.setup());
        main = creator.getMain();
        assertNotNull("The JailMain class is null.", main);
        assertTrue("The adding of a jail failed.", creator.addJail("testingJail"));
        assertFalse("There are no jails.", main.getJailManager().getJails().isEmpty());

        for(int i = 0; i < 1000; i++) {
            if(i == 555)
                use = UUID.randomUUID();
            main.getPrisonerManager().prepareJail(main.getJailManager().getJail("testingJail"), null, null, new Prisoner(i == 555 ? use.toString() : UUID.randomUUID().toString(), "mockPlayer" + i, true, 100000L, "testJailer", "Test jailing " + i));
        }

        //This puts the cache object into the cache for the move event and others to use (move in this test)
        main.getJailManager().addCacheObject(new CachePrisoner(main.getJailManager().getJailPlayerIsIn(use), main.getJailManager().getPrisoner(use)));
        r = new Random();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        creator.tearDown();
        main = null;
    }

    @BenchmarkOptions(benchmarkRounds = 1000, warmupRounds = 0)
    @Test
    public void testPrisonerSizeAndJailed() {
        assertEquals("Prisoners not jailed?", 1000, main.getJailManager().getAllPrisoners().size());
        assertTrue("Prisoner 555 is not jailed", main.getJailManager().isPlayerJailed(use));
    }

    @SuppressWarnings("deprecation")
    @BenchmarkOptions(benchmarkRounds = 5000, warmupRounds = 0)
    @Test
    public void testPlayerMoveEvent() {
        Player p = mock(Player.class);
        when(p.getUniqueId()).thenReturn(use);
        when(p.getName()).thenReturn("mockPlayer555");
        when(p.teleport(any(Location.class))).thenReturn(true);

        Location from = new Location(main.getServer().getWorld("world"), 15, 64, -239);
        Location to = new Location(main.getServer().getWorld("world"), r.nextInt(), r.nextInt(), r.nextInt());
        PlayerMoveEvent e = new PlayerMoveEvent(p, from, to);

        main.getPlayerMoveListener().moveProtection(e);
    }
}
