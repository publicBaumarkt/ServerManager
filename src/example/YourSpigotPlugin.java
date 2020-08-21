package example;

/*
 * Created on 21.08.2020 at 18:59
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.api.events.permission.group.GroupUpdateEvent;
import net.baumarkt.servermanager.api.events.permission.group.GroupUpdatePermissionEvent;
import net.baumarkt.servermanager.api.events.permission.player.PlayerCreateEvent;
import net.baumarkt.servermanager.api.events.permission.player.PlayerGroupUpdateEvent;
import net.baumarkt.servermanager.api.events.permission.player.PlayerPermissionUpdateEvent;
import net.baumarkt.servermanager.api.permission.PermissionHandler;
import net.baumarkt.servermanager.api.permission.objects.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class YourSpigotPlugin extends JavaPlugin implements Listener {

    private PermissionHandler permissionHandler = ServerManager.getInstance().getPermissionHandler();

    @Override
    public void onEnable() {

        final PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(this, this);

        this.permissionHandler = ServerManager.getInstance().getPermissionHandler();
    }

    public void createGroup(final boolean defaultGroup){
        permissionHandler.getPlayerGroupHandler().createGroup("player", true);
    }

    public void addGroup(){
        permissionHandler.getPlayerAPI("Baumarkt").addGroup("player");
    }

    public void removeGroup(){
        permissionHandler.getPlayerAPI("Baumarkt").removeGroup("player");
    }

    public void addPermission(){
        permissionHandler.getPlayerAPI("Baumarkt").addPermission("permission");
    }

    public void getGroups(){
        permissionHandler.getPlayerAPI("Baumarkt").getPlayer().getGroups();
    }

    public void getPermissions(){
        permissionHandler.getPlayerAPI("Baumarkt").getPlayer().getPermissions();
    }

    public void getGroupByName(){
        permissionHandler.getPlayerGroupHandler().groupByName("player", group -> {

        });
    }

    @EventHandler
    public void handle(final GroupUpdateEvent event){
        final Group group = event.getGroup();
        final GroupUpdateEvent.Type type = event.getType();


    }

    @EventHandler
    public void handle(final GroupUpdatePermissionEvent event){
        final Group group = event.getGroup();
        final GroupUpdatePermissionEvent.Type type = event.getType();


    }

    @EventHandler
    public void handle(final PlayerCreateEvent event){
        final Player player = event.getPlayer();

    }

    @EventHandler
    public void handle(final PlayerGroupUpdateEvent event){
        final Player player = event.getPlayer();
        final PlayerGroupUpdateEvent.Type type = event.getType();
        final Group group = event.getUpdateGroup();

    }

    @EventHandler
    public void handle(final PlayerPermissionUpdateEvent event){
        final Player player = event.getPlayer();
        final String permission = event.getPermission();

        event.getType();
    }

}
