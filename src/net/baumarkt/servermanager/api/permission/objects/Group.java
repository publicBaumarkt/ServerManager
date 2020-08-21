package net.baumarkt.servermanager.api.permission.objects;

/*
 * Created on 16.08.2020 at 10:34
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import com.google.common.collect.Lists;
import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.api.events.permission.group.GroupUpdateEvent;
import net.baumarkt.servermanager.api.events.permission.group.GroupUpdatePermissionEvent;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.util.List;

public class Group {

    private final String name;

    private final List<String> permissions;

    public Group(String name) {
        this.name = name;
        this.permissions = Lists.newArrayList();
    }

    public Group setDefaultGroup(boolean defaultGroup) {
        ServerManager.getInstance().getPermissionHandler().getPlayerGroupHandler().update();
        ServerManager.getInstance().getPermissionHandler().getConfiguration().set("group." + getName() + ".default", defaultGroup);

        if(defaultGroup)
            Bukkit.getPluginManager().callEvent(new GroupUpdateEvent(this, GroupUpdateEvent.Type.CHANGE_DEFAULT_TO_TRUE));
        else
            Bukkit.getPluginManager().callEvent(new GroupUpdateEvent(this, GroupUpdateEvent.Type.CHANGE_DEFAULT_TO_FALSE));

        return this;
    }

    public Group addPermission(final String permission){
        permissions.add(permission);
        ServerManager.getInstance().getPermissionHandler().getPlayerGroupHandler().update();

        Bukkit.getPluginManager().callEvent(new GroupUpdatePermissionEvent(this, GroupUpdatePermissionEvent.Type.ADD));

        return this;
    }

    public boolean isDefaultGroup() {
        return ServerManager.getInstance().getPermissionHandler().getConfiguration().getBoolean("group." + getName() + ".default");
    }

    public String getName() {
        return name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

}
