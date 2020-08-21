package net.baumarkt.servermanager;

/*
 * Created on 11.08.2020 at 08:11
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.baumarkt.servermanager.api.permission.PermissionHandler;
import net.baumarkt.servermanager.api.permission.listeners.PlayerJoinListener;
import net.baumarkt.servermanager.commands.ServerManagerCommand;
import net.baumarkt.servermanager.listeners.*;
import net.baumarkt.servermanager.utils.config.SettingsConfig;
import net.baumarkt.servermanager.utils.funciton.FunctionHandler;
import net.baumarkt.servermanager.utils.funciton.functions.*;
import net.baumarkt.servermanager.utils.config.BufferedFileWriter;
import net.baumarkt.servermanager.utils.config.ConfigHandler;
import net.baumarkt.servermanager.utils.funciton.functions.plugin.installer.PluginInstaller;
import net.baumarkt.servermanager.utils.funciton.functions.plugin.installer.PluginInstallerCategory;
import net.baumarkt.servermanager.utils.funciton.functions.plugin.installer.objects.PluginInstalling;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ServerManager extends JavaPlugin {

    private static ServerManager instance;

    private ExecutorService executorService;

    private FunctionHandler functionHandler;
    private Gson gson;
    private ConfigHandler configHandler;
    private SettingsConfig settingsConfig;
    private PluginInstaller pluginInstaller;
    private PermissionHandler permissionHandler;

    private Map<Player, String> playerChattingMap;

    private String prefix;

    @Override
    public void onEnable() {
        init();
    }

    @Override
    public void onDisable() {
        if(functionHandler.getFunctionByName("Permission Control").isAvailable())
            Bukkit.getOnlinePlayers().forEach(players -> players.kickPlayer("§cRestart"));
    }

    private void init(){
        instance = this;

        executorService = Executors.newCachedThreadPool();

        functionHandler = new FunctionHandler();
        gson = new Gson();
        configHandler = new ConfigHandler();
        settingsConfig = new SettingsConfig();
        pluginInstaller = new PluginInstaller();
        permissionHandler = new PermissionHandler();

        playerChattingMap = Maps.newHashMap();

        prefix = configHandler.getContent("prefix");

        registerCommands();
        registerListeners();
        registerFunctions();
        registerPluginInstallerPlugins();

    }

    private void registerCommands(){
        getCommand("servermanager").setExecutor(new ServerManagerCommand());
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerAnimationListener(), this);
        getServer().getPluginManager().registerEvents(new WeatherChangeListener(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPhysicsListener(), this);
        getServer().getPluginManager().registerEvents(new CraftItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    private void registerFunctions(){
        functionHandler.registerFunction(new WorldControlFunction());
        functionHandler.registerFunction(new ServerControlFunction());
        functionHandler.registerFunction(new ChatControlFunction());
        functionHandler.registerFunction(new PlayerControlFunction());
        functionHandler.registerFunction(new PluginControlFunction());
        functionHandler.registerFunction(new PluginInstallerFunction());
        functionHandler.registerFunction(new PermissionControlFunction());
    }

    private void registerPluginInstallerPlugins(){
        pluginInstaller.getPluginInstallingList().add(new PluginInstalling(PluginInstallerCategory.UTILITY, 32536, "Rsl1122"));

        pluginInstaller.getPluginInstallingList().add(new PluginInstalling(PluginInstallerCategory.WORLD, 67701, "Andross"));

        pluginInstaller.getPluginInstallingList().add(new PluginInstalling(PluginInstallerCategory.FUN, 79312, "Labrix"));
        pluginInstaller.getPluginInstallingList().add(new PluginInstalling(PluginInstallerCategory.FUN, 59157, "jbs1222"));

        pluginInstaller.getPluginInstallingList().add(new PluginInstalling(PluginInstallerCategory.GAME, 6799, "Yannici"));
        pluginInstaller.getPluginInstallingList().add(new PluginInstalling(PluginInstallerCategory.GAME, 47572, "Merzy"));
    }

    public ChatColor translateDyeColorToChatColor(DyeColor chatColor) {
        switch (chatColor) {
            case BLACK:
                return ChatColor.BLACK;
            case BLUE:
                return ChatColor.DARK_BLUE;
            case BROWN:
                return ChatColor.DARK_GREEN;
            case CYAN:
                return ChatColor.AQUA;
            case LIGHT_BLUE:
            case SILVER:
                return ChatColor.BLUE;
            case GRAY:
                return ChatColor.GRAY;
            case YELLOW:
                return ChatColor.YELLOW;
            case GREEN:
                return ChatColor.GREEN;
            case WHITE:
            case MAGENTA:
                return ChatColor.WHITE;
            case LIME:
            case ORANGE:
                return ChatColor.GOLD;
            case PURPLE:
            case PINK:
                return ChatColor.LIGHT_PURPLE;
            case RED:
                return ChatColor.RED;
        }

        return null;
    }

    public void changeServerSetting(final String path){
        settingsConfig.getConfiguration().set(path, !settingsConfig.getConfiguration().getString(path).equals("true"));

        settingsConfig.saveConfig();
    }

    public void propertiesValue(final String path, final Consumer<String> consumer){
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(new File("server.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        consumer.accept(prop.getProperty(path));
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public FunctionHandler getFunctionHandler() {
        return functionHandler;
    }

    public SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }

    public Gson getGson() {
        return gson;
    }

    public String getPrefix() {
        return prefix;
    }

    public Map<Player, String> getPlayerChattingMap() {
        return playerChattingMap;
    }

    public PluginInstaller getPluginInstaller() {
        return pluginInstaller;
    }

    public PermissionHandler getPermissionHandler() {
        return permissionHandler;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public static ServerManager getInstance() {
        return instance;
    }
}
