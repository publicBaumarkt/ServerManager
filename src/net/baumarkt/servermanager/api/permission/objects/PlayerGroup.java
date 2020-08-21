package net.baumarkt.servermanager.api.permission.objects;

/*
 * Created on 16.08.2020 at 10:39
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import com.google.common.collect.Lists;
import com.sun.corba.se.impl.activation.ServerMain;
import net.baumarkt.servermanager.ServerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;
import java.util.UUID;

public class PlayerGroup {

    private final String playerName;
    private final UUID uuid;

    private final List<String> permissions;
    private final List<Group> groups;
    private final List<String> playerGroups;

    public PlayerGroup(String playerName, final UUID uuid) {
        this.playerName = playerName;
        this.uuid = uuid;
        this.permissions = Lists.newArrayList();
        this.groups = Lists.newArrayList();
        playerGroups = Lists.newArrayList();
    }

    public void addPermission(final String permissionName){
        permissions.add(permissionName);

        final PermissionAttachment permissionAttachment = Bukkit.getPlayer(uuid).addAttachment(ServerManager.getInstance());

        permissionAttachment.setPermission(permissionName, true);
    }

    public void removePermission(final String permissionName){
        permissions.remove(permissionName);
        final PermissionAttachment permissionAttachment = Bukkit.getPlayer(uuid).addAttachment(ServerManager.getInstance());

        permissionAttachment.setPermission(permissionName, false);
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<String> getPlayerGroups() {
        return playerGroups;
    }

    public String getPlayerName() {
        return playerName;
    }
}
