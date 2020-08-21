package net.baumarkt.servermanager.api.permission.listeners;

/*
 * Created on 16.08.2020 at 11:04
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.api.permission.PermissionHandler;
import net.baumarkt.servermanager.api.permission.objects.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void handle(final PlayerJoinEvent event){
        final Player player = event.getPlayer();

        ServerManager.getInstance().getExecutorService().execute(() -> {
            if(ServerManager.getInstance().getFunctionHandler().getFunctionByName("Permission Control").isAvailable()) {
                final PermissionHandler permissionHandler = ServerManager.getInstance().getPermissionHandler();

                permissionHandler.getPlayerPermissionHandler().createPlayer(player.getUniqueId());

                for (Group group : permissionHandler.getPlayerAPI(player.getUniqueId()).getPlayer().getGroups())
                    for (String permission : group.getPermissions()) {
                        final PermissionAttachment permissionAttachment = Bukkit.getPlayer(player.getUniqueId()).addAttachment(ServerManager.getInstance());

                        permissionAttachment.setPermission(permission, true);
                    }
            }
        });

    }

}
