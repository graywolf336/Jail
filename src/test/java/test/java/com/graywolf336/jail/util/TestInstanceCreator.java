package test.java.com.graywolf336.jail.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemFactory;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import org.junit.Assert;

import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.MockGateway;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import com.graywolf336.jail.JailMain;

@PrepareForTest({ CraftItemFactory.class })
public class TestInstanceCreator {
	private JailMain main;
	private Server mockServer;
	private Player mockPlayer;
	private CommandSender mockSender, mockPlayerSender;
	
	public static final File serverDirectory = new File("bin" + File.separator + "test" + File.separator + "server");
	public static final File worldsDirectory = new File("bin" + File.separator + "test" + File.separator + "server");
	public static final File pluginDirectory = new File(serverDirectory + File.separator + "plugins" + File.separator + "JailTest");
	
	public boolean setup() {
		try {
			pluginDirectory.mkdirs();
			Assert.assertTrue(pluginDirectory.exists());
			
			MockGateway.MOCK_STANDARD_METHODS = false;
			
			// Initialize the Mock server.
			mockServer = mock(Server.class);
			when(mockServer.getName()).thenReturn("TestBukkit");
			when(mockServer.getVersion()).thenReturn("Jail-Testing-0.0.1");
			when(mockServer.getBukkitVersion()).thenReturn("0.0.1");
			Logger.getLogger("Minecraft").setParent(Util.logger);
			when(mockServer.getLogger()).thenReturn(Util.logger);
			when(mockServer.getWorldContainer()).thenReturn(worldsDirectory);
			when(mockServer.getItemFactory()).thenReturn(CraftItemFactory.instance());
			
			main = PowerMockito.spy(new JailMain());
			
			PluginDescriptionFile pdf = PowerMockito.spy(new PluginDescriptionFile("Jail", "3.0.0-Test", "com.graywolf336.jail.JailMain"));
			when(pdf.getPrefix()).thenReturn("Jail");
			List<String> authors = new ArrayList<String>();
				authors.add("matejdro");
				authors.add("multidude");
				authors.add("graywolf336");
			doReturn(authors).when(pdf).getAuthors();
			doReturn(pdf).when(main).getDescription();
			doReturn(true).when(main).isEnabled();
			doReturn(Util.logger).when(main).getLogger();
			doReturn(mockServer).when(main).getServer();
			doReturn(pluginDirectory).when(main).getDataFolder();
			
			Field configFile = JavaPlugin.class.getDeclaredField("configFile");
			configFile.setAccessible(true);
			configFile.set(main, new File(pluginDirectory, "config.yml"));
			
			Field logger = JavaPlugin.class.getDeclaredField("logger");
			logger.setAccessible(true);
			logger.set(main, new PluginLogger(main));
			
			doReturn(getClass().getClassLoader().getResourceAsStream("config.yml")).when(main).getResource("config.yml");
			
			// Add Jail to the list of loaded plugins
			JavaPlugin[] plugins = new JavaPlugin[] { main };
			
			// Mock the Plugin Manager
			PluginManager mockPluginManager = PowerMockito.mock(PluginManager.class);
			when(mockPluginManager.getPlugins()).thenReturn(plugins);
			when(mockPluginManager.getPlugin("Jail")).thenReturn(main);
			when(mockPluginManager.getPermission(anyString())).thenReturn(null);
			
            // Give the server some worlds
            when(mockServer.getWorld(anyString())).thenAnswer(new Answer<World>() {
                public World answer(InvocationOnMock invocation) throws Throwable {
                    String arg;
                    try {
                        arg = (String) invocation.getArguments()[0];
                    } catch (Exception e) {
                        return null;
                    }
                    return MockWorldFactory.getWorld(arg);
                }
            });

            when(mockServer.getWorld(any(UUID.class))).thenAnswer(new Answer<World>() {
                public World answer(InvocationOnMock invocation) throws Throwable {
                    UUID arg;
                    try {
                        arg = (UUID) invocation.getArguments()[0];
                    } catch (Exception e) {
                        return null;
                    }
                    return MockWorldFactory.getWorld(arg);
                }
            });

            when(mockServer.getWorlds()).thenAnswer(new Answer<List<World>>() {
                public List<World> answer(InvocationOnMock invocation) throws Throwable {
                    return MockWorldFactory.getWorlds();
                }
            });
			
			when(mockServer.getPluginManager()).thenReturn(mockPluginManager);
			
			when(mockServer.createWorld(Matchers.isA(WorldCreator.class))).thenAnswer(
	            new Answer<World>() {
	                public World answer(InvocationOnMock invocation) throws Throwable {
	                    WorldCreator arg;
	                    try {
	                        arg = (WorldCreator) invocation.getArguments()[0];
	                    } catch (Exception e) {
	                        return null;
	                    }
	                    // Add special case for creating null worlds.
	                    // Not sure I like doing it this way, but this is a special case
	                    if (arg.name().equalsIgnoreCase("nullworld")) {
	                        return MockWorldFactory.makeNewNullMockWorld(arg.name(), arg.environment(), arg.type());
	                    }
	                    return MockWorldFactory.makeNewMockWorld(arg.name(), arg.environment(), arg.type());
	                }
	            });

            when(mockServer.unloadWorld(anyString(), anyBoolean())).thenReturn(true);
			
            // add mock scheduler
            BukkitScheduler mockScheduler = mock(BukkitScheduler.class);
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class), anyLong())).
            thenAnswer(new Answer<Integer>() {
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    Runnable arg;
                    try {
                        arg = (Runnable) invocation.getArguments()[1];
                    } catch (Exception e) {
                        return null;
                    }
                    arg.run();
                    return null;
                }});
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class))).
            thenAnswer(new Answer<Integer>() {
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    Runnable arg;
                    try {
                        arg = (Runnable) invocation.getArguments()[1];
                    } catch (Exception e) {
                        return null;
                    }
                    arg.run();
                    return null;
                }});
            when(mockServer.getScheduler()).thenReturn(mockScheduler);
            
			// Set server
			Field serverField = JavaPlugin.class.getDeclaredField("server");
			serverField.setAccessible(true);
			serverField.set(main, mockServer);
			
			// Init our command sender
			final Logger commandSenderLogger = Logger.getLogger("CommandSender");
			commandSenderLogger.setParent(Util.logger);
			mockSender = mock(CommandSender.class);
			doAnswer(new Answer<Void>() {
				public Void answer(InvocationOnMock invocation) throws Throwable {
					commandSenderLogger.info(ChatColor.stripColor((String) invocation.getArguments()[0]));
					return null;
				}
			}).when(mockSender).sendMessage(anyString());
			when(mockSender.getServer()).thenReturn(mockServer);
			when(mockSender.getName()).thenReturn("MockCommandSender");
			when(mockSender.isPermissionSet(anyString())).thenReturn(true);
			when(mockSender.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(true);
			when(mockSender.hasPermission(anyString())).thenReturn(true);
			when(mockSender.hasPermission(Matchers.isA(Permission.class))).thenReturn(true);
			when(mockSender.addAttachment(main)).thenReturn(null);
			when(mockSender.isOp()).thenReturn(true);
			
			// Init our player, who is op and who has all permissions (with name of graywolf336)
			mockPlayer = mock(Player.class);
			when(mockPlayer.getName()).thenReturn("graywolf336");
			when(mockPlayer.getDisplayName()).thenReturn("TheGrayWolf");
			when(mockPlayer.isPermissionSet(anyString())).thenReturn(true);
			when(mockPlayer.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(true);
			when(mockPlayer.hasPermission(anyString())).thenReturn(true);
			when(mockPlayer.hasPermission(Matchers.isA(Permission.class))).thenReturn(true);
			when(mockPlayer.isOp()).thenReturn(true);
			when(mockPlayer.getInventory()).thenReturn(new MockPlayerInventory());
			
			// Init our second command sender, but this time is an instance of a player
			mockPlayerSender = (CommandSender) mockPlayer;
			when(mockPlayerSender.getServer()).thenReturn(mockServer);
			when(mockPlayerSender.getName()).thenReturn("graywolf336");
			when(mockPlayerSender.isPermissionSet(anyString())).thenReturn(true);
			when(mockPlayerSender.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(true);
			when(mockPlayerSender.hasPermission(anyString())).thenReturn(true);
			when(mockPlayerSender.hasPermission(Matchers.isA(Permission.class))).thenReturn(true);
			when(mockPlayerSender.addAttachment(main)).thenReturn(null);
			when(mockPlayerSender.isOp()).thenReturn(true);
			
			Bukkit.setServer(mockServer);
			
			// Load Jail
			main.onLoad();
			
			// Enable it
			main.onEnable();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean tearDown() {
		try {
			Field serverField = Bukkit.class.getDeclaredField("server");
			serverField.setAccessible(true);
			serverField.set(Class.forName("org.bukkit.Bukkit"), null);
		} catch (Exception e) {
			Util.log(Level.SEVERE, "Error while trying to unregister the server from Bukkit. Has Bukkit changed?");
			e.printStackTrace();
			Assert.fail(e.getMessage());
			return false;
		}
		
		main.onDisable();
		
		
		deleteFolder(serverDirectory);
		
		return true;
	}
	
	public JailMain getMain() {
		return this.main;
	}
	
	public Server getServer() {
		return this.mockServer;
	}
	
	public CommandSender getCommandSender() {
		return this.mockSender;
	}
	
	public Player getPlayer() {
		return this.mockPlayer;
	}
	
	public CommandSender getPlayerCommandSender() {
		return this.mockPlayerSender;
	}
	
	private void deleteFolder(File folder) {
		File[] files = folder.listFiles();
		if(files != null) {
			for(File f: files) {
				if(f.isDirectory()) {
					deleteFolder(f);
				}else {
					f.delete();
				}
			}
		}
		
		folder.delete();
	}
}
