package net.baumarkt.servermanager.api.permission;

/*
 * Created on 16.08.2020 at 10:32
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.api.events.permission.player.PlayerGroupUpdateEvent;
import net.baumarkt.servermanager.api.events.permission.player.PlayerPermissionUpdateEvent;
import net.baumarkt.servermanager.api.permission.objects.PlayerGroup;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

public class PermissionHandler {

    private YamlConfiguration configuration;
    private File file;

    private PlayerPermissionHandler playerPermissionHandler;
    private PlayerGroupHandler playerGroupHandler;

    public PermissionHandler(){
        init();
    }

    private void init(){
        new File("plugins/ServerManager/").mkdir();

        Path path = Paths.get("plugins/ServerManager/", "permissions.yml");
        file = new File("plugins/ServerManager/permissions.yml");

        if(Files.notExists(path) && !file.exists()){
            try {
                Files.copy(ServerManager.class.getClassLoader().getResourceAsStream("permissions.yml"), path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configuration = YamlConfiguration.loadConfiguration(file);
        playerPermissionHandler = new PlayerPermissionHandler(this);
        playerGroupHandler = new PlayerGroupHandler(this);
    }

    public void saveConfig(){
        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public PlayerPermissionHandler getPlayerPermissionHandler() {
        return playerPermissionHandler;
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    public PlayerGroupHandler getPlayerGroupHandler() {
        return playerGroupHandler;
    }

    public PlayerAPI getPlayerAPI(final String player){
        return new PlayerAPI(this, player);
    }

    public PlayerAPI getPlayerAPI(final UUID player){
        return new PlayerAPI(this, player);
    }

    public class PlayerAPI {

        private PlayerGroup player;
        private final PermissionHandler permissionHandler;

        public PlayerAPI(final PermissionHandler permissionHandler, final PlayerGroup player){
            this.permissionHandler = permissionHandler;
            this.player = player;
        }

        public PlayerAPI(final PermissionHandler permissionHandler, final String player){
            this.permissionHandler = permissionHandler;
            permissionHandler.playerPermissionHandler.groupPlayerByName(player, consumer -> {
                this.player = consumer;
            });
        }

        public PlayerAPI(final PermissionHandler permissionHandler, final UUID player){
            this.permissionHandler = permissionHandler;

            permissionHandler.playerPermissionHandler.groupPlayerByUUID(player, consumer -> {
                this.player = consumer;
            });
        }

        public PermissionHandler addPermission(final String permissionName){
            player.addPermission(permissionName);
            permissionHandler.getPlayerPermissionHandler().update(player);

            Bukkit.getPluginManager().callEvent(new PlayerPermissionUpdateEvent(Bukkit.getPlayer(player.getUuid()), permissionName, PlayerPermissionUpdateEvent.Type.ADD));
            return permissionHandler;
        }

        public PermissionHandler addGroup(final String groupName){
            playerGroupHandler.groupByName(groupName, consumer -> {
                player.getGroups().add(consumer);
                player.getPlayerGroups().add(consumer.getName());

                permissionHandler.getConfiguration().set("payerData." + player.getUuid()+ ".groups", player.getPlayerGroups());
                permissionHandler.saveConfig();

                final PermissionAttachment permissionAttachment = Bukkit.getPlayer(player.getUuid()).addAttachment(ServerManager.getInstance());

                for(String permission : consumer.getPermissions())
                    permissionAttachment.setPermission(permission, true);

                Bukkit.getPluginManager().callEvent(new PlayerGroupUpdateEvent(Bukkit.getPlayer(player.getUuid()), consumer, PlayerGroupUpdateEvent.Type.ADD));
            });


            return permissionHandler;
        }

        public PermissionHandler removeGroup(final String groupName){
            playerGroupHandler.groupByName(groupName, consumer -> {
                player.getGroups().remove(consumer);
                final PermissionAttachment permissionAttachment = Bukkit.getPlayer(player.getUuid()).addAttachment(ServerManager.getInstance());

                for(String permission : consumer.getPermissions())
                    permissionAttachment.setPermission(permission, false);

                Bukkit.getPluginManager().callEvent(new PlayerGroupUpdateEvent(Bukkit.getPlayer(player.getUuid()), consumer, PlayerGroupUpdateEvent.Type.REMOVE));
            });

            return permissionHandler;
        }

        public PlayerGroup getPlayer() {
            return player;
        }

        public PermissionHandler getPermissionHandler() {
            return permissionHandler;
        }
    }

}
