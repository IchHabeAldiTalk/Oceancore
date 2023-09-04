package de.oceanblocks.oceancore;

import de.oceanblocks.oceancore.commands.PluginLoaderCommand;
import de.oceanblocks.oceancore.driver.DriverAPI;
import de.oceanblocks.oceancore.driver.MySQL;
import de.oceanblocks.oceancore.listener.ListenerUtil;
import de.oceanblocks.oceancore.perms.LuckPermsAPIHook;
import de.oceanblocks.oceancore.cache.uPlayerCache;
import de.oceanblocks.oceancore.util.Metrics;
import lombok.Getter;
import lombok.SneakyThrows;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.List;

public final class Oceancore extends JavaPlugin {

    @Getter
    public static Oceancore instance;

    @Getter
    private uPlayerCache uPlayerCache;

    @Getter
    private LuckPerms luckPerms;

    @Getter
    private LuckPermsAPIHook luckPermsApiHook;

    @Getter
    private ListenerUtil listenerUtil;

    @Getter
    private String version;

    @Getter
    private final int pluginID = 13492;

    @Getter
    private boolean isFloodgate = false;

    @Getter
    private FloodgateApi floodgateApi;

    @Getter
    private static MySQL cloudSQL;

    @Getter
    private static MySQL nickSQL;

    @Getter
    private static final DriverAPI driverAPI = new DriverAPI();

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        version = getDescription().getVersion();

        Metrics metrics = new Metrics(this, pluginID);

        System.out.println("Booting PluginCore " + version);

        if (!Bukkit.getPluginManager().isPluginEnabled("SimpleCloud-Plugin")) {
            System.out.println("Cloud not find SimpleCloud-Plugin. Ignoring it and disabling integration. \n" +
                    "IMPORTANT: Some features may cause errors / are not working probably!");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("floodgate")) {
            isFloodgate = true;
            floodgateApi = FloodgateApi.getInstance();
        } else {
            getLogger().warning("Could not find floodgate. Disabling bedrock uuid support.");
        }

        uPlayerCache = new uPlayerCache();
        uPlayerCache.loadCache();

        //registerListeners();
        registerListeners(this);
        registerCommands();

        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            luckPerms = LuckPermsProvider.get();
            System.out.println("Enabled LuckPerms integration.");
            luckPermsApiHook = new LuckPermsAPIHook();
        } else {
            System.out.println("Could not find LuckPerms. Ignoring it and disabling integration.");
        }

        listenerUtil = new ListenerUtil();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @SneakyThrows
    private void registerListeners(JavaPlugin plugin) {
        Reflections reflections = new Reflections("net.extraherz.plugincore.listener");
        reflections.getSubTypesOf(Listener.class).forEach(clazz -> {
            System.out.println("Try to register class with name " + clazz.getSimpleName());
            try {
                plugin.getServer().getPluginManager().registerEvents(clazz.newInstance(), plugin);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private void registerCommands() {
        List<String> commands = Arrays.asList("coreplug");
        for (String command : commands) {
            if (getCommand(command) == null) {
                getLogger().info("Could not load command /" + command + ". Check if you have created the command and inserted it into plugin.yml");
                return;
            }
            getCommand("coreplug").setExecutor(new PluginLoaderCommand());
        }
    }

    private void loadMySQL() {
        cloudSQL = new MySQL("127.0.0.1", "OceanCloud", "q1W7oD!Sn]3*5gVO", "OceanCloud", "3306", false);
        cloudSQL.connect();
        cloudSQL.createTable("CREATE TABLE IF NOT EXISTS FriendSystem (UUID VARCHAR(64) UNIQUE, USERNAME VARCHAR(100), FRIENDS VARCHAR(5000), REQUESTS VARCHAR(1000), SETTINGS VARCHAR(1000), LASTLOGIN VARCHAR(255), FIRSTLOGIN VARCHAR(255));");
        cloudSQL.createTable("CREATE TABLE IF NOT EXISTS OnlinePlayers (UUID VARCHAR(64) UNIQUE, Online VARCHAR(20));");
        cloudSQL.createTable("CREATE TABLE IF NOT EXISTS ClanSystem(UUID VARCHAR(64) UNIQUE, PLAYERID int, CLANID int, NAME VARCHAR(64), CLANNAME VARCHAR(64), CLANTAG VARCHAR(64), RANK VARCHAR(64))");

        nickSQL = new MySQL("127.0.0.1", "NickSystem", "()p9UPeO[DP6AyWm", "NickSystem", "3306", false);
        nickSQL.connect();
        nickSQL.createTable("CREATE TABLE IF NOT EXISTS NickSystem (UUID VARCHAR(80) UNIQUE, PlayerName VARCHAR(32), AutoNick VARCHAR(10), NickType VARCHAR(20));");
        nickSQL.createTable("CREATE TABLE IF NOT EXISTS NickedPlayers (UUID VARCHAR(80) UNIQUE, Realname VARCHAR(32), Nickname VARCHAR(32));");
        nickSQL.createTable("CREATE TABLE IF NOT EXISTS NickNames (Names VARCHAR(32) UNIQUE, NickNames VARCHAR(1000));");

        Bukkit.getScheduler().scheduleAsyncDelayedTask(this, () -> {
            cloudSQL.setAutoReconnect(true);
            cloudSQL.reconnect();
            nickSQL.setAutoReconnect(true);
            nickSQL.reconnect();
        }, 20*10);
    }
}
