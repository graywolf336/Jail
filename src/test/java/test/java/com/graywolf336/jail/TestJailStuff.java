package test.java.com.graywolf336.jail;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import test.java.com.graywolf336.jail.util.TestInstanceCreator;

import com.graywolf336.jail.JailMain;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ JailMain.class, PluginDescriptionFile.class })
public class TestJailStuff {
	private TestInstanceCreator creator;
	private JailMain main;

	@Before
	public void setUp() throws Exception {
		creator = new TestInstanceCreator();
		creator.setup();
		main = creator.getMain();
	}

	@After
	public void tearDown() throws Exception {
		creator.tearDown();
	}

	@Test
	public void testForJails() {
		assertNotNull("The JailIO is null.", main.getJailIO());
		assertNotNull("The JailManager is null.", main.getJailManager());
		assertNotNull("The HashSet for jails return is null.", main.getJailManager().getJails());
		assertThat(main, is(main.getJailManager().getPlugin()));
	}
	
	@Test
	public void testDefaultConfig() {
		assertEquals("The config version is not 3.", 3, main.getConfig().getInt("system.configVersion"));
		assertFalse("Default debugging is on.", main.getConfig().getBoolean("system.debug"));
		assertTrue("Default updating notifications is false.", main.getConfig().getBoolean("system.updateNotifications"));
		
		//Storage system
		assertEquals("The default storage system is not flatfile.", "flatfile", main.getConfig().getString("storage.type"));
		assertEquals("The default mysql host is not localhost.", "localhost", main.getConfig().getString("storage.mysql.host"));
		assertEquals("The default mysql port is not 3306.", 3306, main.getConfig().getInt("storage.mysql.port"));
		assertEquals("The default mysql username is not root.", "root", main.getConfig().getString("storage.mysql.username"));
		assertEquals("The default mysql password is not password.", "password", main.getConfig().getString("storage.mysql.password"));
		
		//Settings pertaining to being jailed
		assertFalse("Default setting for counting down time while prisoner is offline is true.", main.getConfig().getBoolean("jailing.during.countDownTimeWhileOffline"));
		assertTrue("Default setting for ignoring sleeping is false.", main.getConfig().getBoolean("jailing.during.ignoreSleeping"));
		assertTrue("Default setting for opening a chest is false.", main.getConfig().getBoolean("jailing.during.openChest"));
		
		//Settings pertaining to when jailed
		assertTrue("Default setting for automatically muting is false.", main.getConfig().getBoolean("jailing.jail.automaticMute"));
		assertFalse("Default setting for broadcasting a jailing is true.", main.getConfig().getBoolean("jailing.jail.broadcastJailing"));
		assertEquals("Default setting for commands contains information.", 0, main.getConfig().getList("jailing.jail.commands").size());
		assertEquals("Default setting for default jail is not 'nearest' but something else.", "nearest", main.getConfig().getString("jailing.jail.defaultJail"));
		assertEquals("Default setting for time is not 30 minutes.", "30m", main.getConfig().getString("jailing.jail.defaultTime"));
		assertFalse("Default setting for deleting inventory is true.", main.getConfig().getBoolean("jailing.jail.deleteInventory"));
		assertTrue("Default setting for logging to console when someone is jailed is false.", main.getConfig().getBoolean("jailing.jail.logToConsole"));
		assertFalse("Default setting for logging to prisoner's profile is true.", main.getConfig().getBoolean("jailing.jail.logToProfile"));
		assertTrue("Default setting for storing prisoner's inventory upon jailing is false.", main.getConfig().getBoolean("jailing.jail.storeInventory"));
		
		//Settings pertaining to after a prisoner is released
		assertFalse("Default setting for releasing back to previous position is true.", main.getConfig().getBoolean("jailing.release.backToPreviousPosition"));
		assertEquals("Default setting for commands contains information.", 0, main.getConfig().getList("jailing.release.commands").size());
		assertTrue("Default setting for teleporting them out of the jail is false.", main.getConfig().getBoolean("jailing.release.teleport"));
	}
}
