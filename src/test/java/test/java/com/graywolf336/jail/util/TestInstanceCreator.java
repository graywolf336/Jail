package test.java.com.graywolf336.jail.util;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.constructor;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.junit.Assert;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.MockGateway;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.graywolf336.jail.JailMain;
import com.graywolf336.jail.beans.Jail;

@PrepareForTest({ CraftItemFactory.class })
public class TestInstanceCreator {
    private Random r;
    private JailMain main;
    private Server mockServer;
    private Player mockPlayer;
    private PlayerInventory mockPlayerInventory;
    private World mockWorld;
    private CommandSender mockSender, mockPlayerSender;
    private ConsoleCommandSender consoleSender;
    private ArrayList<UUID> players = new ArrayList<UUID>();

    public static final File serverDirectory = new File("bin" + File.separator + "test" + File.separator + "server");
    public static final File worldsDirectory = new File("bin" + File.separator + "test" + File.separator + "server");
    public static final File pluginDirectory = new File(serverDirectory + File.separator + "plugins" + File.separator + "JailTest");
    public static final File localeDirectory = new File(serverDirectory + File.separator + "plugins" + File.separator + "JailTest" + File.separator + "locales");

    @SuppressWarnings("deprecation")
    public boolean setup() {
        r = new Random();

        try {
            pluginDirectory.mkdirs();
            Assert.assertTrue(pluginDirectory.exists());
            localeDirectory.mkdirs();
            Assert.assertTrue(localeDirectory.exists());

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
            when(mockServer.isPrimaryThread()).thenReturn(true);

            mockWorld = MockWorldFactory.makeNewMockWorld("world", Environment.NORMAL, WorldType.NORMAL);

            suppress(constructor(JailMain.class));
            main = PowerMockito.spy(new JailMain());

            PluginDescriptionFile pdf = PowerMockito.spy(new PluginDescriptionFile("Jail", "3.0.0-SNAPSHOT-b0", "com.graywolf336.jail.JailMain"));
            when(pdf.getPrefix()).thenReturn("Jail");
            List<String> authors = new ArrayList<String>();
            authors.add("matejdro");
            authors.add("multidude");
            authors.add("graywolf336");
            when(pdf.getAuthors()).thenReturn(authors);
            when(main.getDescription()).thenReturn(pdf);
            when(main.getDataFolder()).thenReturn(pluginDirectory);
            when(main.isEnabled()).thenReturn(true);
            when(main.getLogger()).thenReturn(Util.logger);
            when(main.getServer()).thenReturn(mockServer);

            Field configFile = JavaPlugin.class.getDeclaredField("configFile");
            configFile.setAccessible(true);
            configFile.set(main, new File(pluginDirectory, "config.yml"));

            Field logger = JavaPlugin.class.getDeclaredField("logger");
            logger.setAccessible(true);
            logger.set(main, new PluginLogger(main));

            doReturn(getClass().getClassLoader().getResourceAsStream("config.yml")).when(main).getResource("config.yml");
            doReturn(getClass().getClassLoader().getResourceAsStream("locales/en.yml")).when(main).getResource("locales/en.yml");

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

            when(mockServer.createWorld(Matchers.isA(WorldCreator.class))).thenAnswer(new Answer<World>() {
                public World answer(InvocationOnMock invocation) throws Throwable {
                    WorldCreator arg;
                    try {
                        arg = (WorldCreator) invocation.getArguments()[0];
                    } catch (Exception e) {
                        return null;
                    }
                    // Add special case for creating null worlds.
                    // Not sure I like doing it this way, but this is a special
                    // case
                    if (arg.name().equalsIgnoreCase("nullworld"))
                        return MockWorldFactory.makeNewNullMockWorld(arg.name(), arg.environment(), arg.type());
                    return MockWorldFactory.makeNewMockWorld(arg.name(), arg.environment(), arg.type());
                }
            });

            when(mockServer.unloadWorld(anyString(), anyBoolean())).thenReturn(true);

            // add mock scheduler
            final BukkitTask bt = mock(BukkitTask.class);
            when(bt.getTaskId()).thenReturn(r.nextInt());
            when(bt.getOwner()).thenReturn(main);
            when(bt.isSync()).thenReturn(false);

            BukkitScheduler mockScheduler = mock(BukkitScheduler.class);
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class), anyLong())).thenAnswer(new Answer<BukkitTask>() {
                public BukkitTask answer(InvocationOnMock invocation) throws Throwable {
                    Runnable arg;
                    try {
                        arg = (Runnable) invocation.getArguments()[1];
                    } catch (Exception e) {
                        return bt;
                    }
                    arg.run();
                    return bt;
                }
            });
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class))).thenAnswer(new Answer<BukkitTask>() {
                public BukkitTask answer(InvocationOnMock invocation) throws Throwable {
                    Runnable arg;
                    try {
                        arg = (Runnable) invocation.getArguments()[1];
                    } catch (Exception e) {
                        return bt;
                    }
                    arg.run();
                    return bt;
                }
            });
            when(mockScheduler.runTaskTimerAsynchronously(any(Plugin.class), any(Runnable.class), anyLong(), anyLong())).thenAnswer(new Answer<BukkitTask>() {
                public BukkitTask answer(InvocationOnMock invocation) throws Throwable {
                    Runnable arg;
                    try {
                        arg = (Runnable) invocation.getArguments()[1];
                    } catch (Exception e) {
                        return bt;
                    }
                    arg.run();
                    return bt;
                }
            });
            when(mockServer.getScheduler()).thenReturn(mockScheduler);

            // Set server
            Field serverField = JavaPlugin.class.getDeclaredField("server");
            serverField.setAccessible(true);
            serverField.set(main, mockServer);

            // Init our command sender
            final Logger consoleSenderLogger = Logger.getLogger("ConsoleCommandSender");
            consoleSenderLogger.setParent(Util.logger);
            consoleSender = mock(ConsoleCommandSender.class);
            doAnswer(new Answer<Void>() {
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    consoleSenderLogger.info(ChatColor.stripColor((String) invocation.getArguments()[0]));
                    return null;
                }
            }).when(consoleSender).sendMessage(anyString());
            when(consoleSender.getServer()).thenReturn(mockServer);
            when(consoleSender.getName()).thenReturn("MockCommandSender");
            when(consoleSender.isPermissionSet(anyString())).thenReturn(true);
            when(consoleSender.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(true);
            when(consoleSender.hasPermission(anyString())).thenReturn(true);
            when(consoleSender.hasPermission(Matchers.isA(Permission.class))).thenReturn(true);
            when(consoleSender.addAttachment(main)).thenReturn(null);
            when(consoleSender.isOp()).thenReturn(true);
            when(mockServer.getConsoleSender()).thenReturn(consoleSender);

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

            // Mock the player's inventory
            // TODO: Mock the methods when needed
            mockPlayerInventory = mock(PlayerInventory.class);
            when(mockPlayerInventory.addItem(Matchers.isA(ItemStack.class))).thenReturn(new HashMap<Integer, ItemStack>());

            // Init our player, who is op and who has all permissions (with name
            // of graywolf336)
            mockPlayer = mock(Player.class);
            UUID playerId = UUID.fromString("062c14ba-4c47-4757-911b-bbf9a60dab7b");
            when(mockPlayer.getUniqueId()).thenReturn(playerId);
            when(mockPlayer.getName()).thenReturn("graywolf336");
            when(mockPlayer.getDisplayName()).thenReturn("TheGrayWolf");
            when(mockPlayer.isPermissionSet(anyString())).thenReturn(true);
            when(mockPlayer.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(true);
            when(mockPlayer.hasPermission(anyString())).thenReturn(true);
            when(mockPlayer.hasPermission(Matchers.isA(Permission.class))).thenReturn(true);
            when(mockPlayer.isOp()).thenReturn(true);
            when(mockPlayer.getInventory()).thenReturn(mockPlayerInventory);
            when(mockPlayer.getLocation()).thenReturn(new Location(mockWorld, 23, 70, -242));
            when(mockServer.getPlayer("graywolf336")).thenReturn(mockPlayer);
            when(mockServer.getPlayer(playerId)).thenReturn(mockPlayer);
            players.add(playerId);

            // Init our second command sender, but this time is an instance of a
            // player
            mockPlayerSender = mockPlayer;
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

            // Enable it and turn on debugging
            main.onEnable();
            main.setDebugging(true);

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

        MockWorldFactory.clearWorlds();

        deleteFolder(pluginDirectory);
        deleteFolder(worldsDirectory);
        deleteFolder(serverDirectory);
        return true;
    }

    @SuppressWarnings("deprecation")
    public boolean addMockPlayer(String name, UUID id) {
        if (players.contains(id))
            return false;
        else {
            Player anotherPlayer = mock(Player.class);
            when(anotherPlayer.getUniqueId()).thenReturn(id);
            when(anotherPlayer.getName()).thenReturn(name);
            when(anotherPlayer.getDisplayName()).thenReturn(name);
            when(anotherPlayer.isPermissionSet(anyString())).thenReturn(true);
            when(anotherPlayer.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(true);
            when(anotherPlayer.hasPermission(anyString())).thenReturn(true);
            when(anotherPlayer.hasPermission(Matchers.isA(Permission.class))).thenReturn(true);
            when(anotherPlayer.isOp()).thenReturn(true);
            when(anotherPlayer.getInventory()).thenReturn(mockPlayerInventory);
            when(anotherPlayer.getLocation()).thenReturn(new Location(mockWorld, 56, 85, -2420));
            when(mockServer.getPlayer(name)).thenReturn(anotherPlayer);
            when(mockServer.getPlayer(id)).thenReturn(anotherPlayer);
            players.add(id);
            return true;
        }
    }

    public boolean addJail(String name) {
        if (main.getJailManager().isValidJail(name))
            return false;
        else {
            Jail j = new Jail(main, name);
            j.setWorld("world");
            j.setMaxPoint(new int[] { r.nextInt(), r.nextInt(256), r.nextInt() });
            j.setMinPoint(new int[] { r.nextInt(), r.nextInt(54), r.nextInt() });
            j.setTeleportIn(new Location(main.getServer().getWorld("world"), r.nextInt(), r.nextInt(256), r.nextInt(), Float.valueOf("38.499817"), Float.valueOf("2.0000453")));
            j.setTeleportFree(new Location(main.getServer().getWorld("world"), r.nextInt(), r.nextInt(256), r.nextInt(), Float.valueOf("90.54981"), Float.valueOf("12.500043")));
            main.getJailManager().addJail(j, false);
            return true;
        }
    }

    public JailMain getMain() {
        return main;
    }

    public Server getServer() {
        return mockServer;
    }

    public CommandSender getCommandSender() {
        return mockSender;
    }

    public Player getPlayer() {
        return mockPlayer;
    }

    public CommandSender getPlayerCommandSender() {
        return mockPlayerSender;
    }

    public ConsoleCommandSender getConsoleSender() {
        return consoleSender;
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }

        folder.delete();
    }
}
