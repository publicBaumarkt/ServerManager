package net.baumarkt.servermanager.api.permission;

/*
 * Created on 16.08.2020 at 13:30
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import com.google.common.collect.Lists;
import net.baumarkt.servermanager.api.events.permission.group.GroupUpdateEvent;
import net.baumarkt.servermanager.api.events.permission.group.GroupUpdatePermissionEvent;
import net.baumarkt.servermanager.api.permission.objects.Group;
import net.baumarkt.servermanager.api.permission.objects.PlayerGroup;
import net.baumarkt.servermanager.utils.GameProfileBuilder;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerGroupHandler {

    private final PermissionHandler permissionHandler;

    private final List<String> playersUUID;
    private final List<PlayerGroup> players;

    private final List<Group> groups;
    private final List<String> groupNames;

    public PlayerGroupHandler(final PermissionHandler permissionHandler){
        this.permissionHandler = permissionHandler;
        this.players = Lists.newArrayList();
        this.playersUUID = Lists.newArrayList();
        this.groups = Lists.newArrayList();
        this.groupNames = Lists.newArrayList();

        if(permissionHandler.getConfiguration().getStringList("players") == null)
            permissionHandler.getConfiguration().set("players", playersUUID);
        else
            playersUUID.addAll(permissionHandler.getConfiguration().getStringList("players"));

        if(permissionHandler.getConfiguration().getStringList("groups") == null)
            permissionHandler.getConfiguration().set("groups", groupNames);
        else
            groupNames.addAll(permissionHandler.getConfiguration().getStringList("groups"));

        permissionHandler.saveConfig();

        groupNames.forEach(groupName -> {
            final Group group = new Group(groupName);

            if(!groups.contains(group))
                groups.add(group);

            group.getPermissions().addAll(permissionHandler.getConfiguration().getStringList("group." + groupName + ".permission"));
        });

        playersUUID.forEach(player ->{
            try {
                final PlayerGroup playerGroup = new PlayerGroup(GameProfileBuilder.fetch(UUID.fromString(player)).getName(), UUID.fromString(player));

                if(!players.contains(playerGroup))
                    players.add(playerGroup);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void createGroup(final String name, final boolean defaultGroup){
        final Group group = new Group(name).setDefaultGroup(defaultGroup);
        groups.add(group);
        groupNames.add(name);

        permissionHandler.getConfiguration().set("groups", groupNames);
        permissionHandler.getConfiguration().set("group." + name + ".default", defaultGroup);
        permissionHandler.getConfiguration().set("group." + name + ".permission", Arrays.asList("test.permission"));

        permissionHandler.saveConfig();

        Bukkit.getPluginManager().callEvent(new GroupUpdateEvent(group, GroupUpdateEvent.Type.CREATE));
    }

    public void groupByName(final String name, final Consumer<Group> consumer){
        for(Group group : groups){
            if(group.getName().equals(name)) {
                consumer.accept(group);
                return;
            }
        }
        consumer.accept(null);
    }

    public void update(final PlayerGroup playerGroup){

        permissionHandler.getConfiguration().set("groups", groupNames);
        permissionHandler.getConfiguration().set("payerData." + playerGroup.getUuid()+ ".name", playerGroup.getPlayerName());
        permissionHandler.getConfiguration().set("payerData." + playerGroup.getUuid()+ ".permission", playerGroup.getPermissions());
        permissionHandler.getConfiguration().set("payerData." + playerGroup.getUuid()+ ".groups", playerGroup.getPlayerGroups());

        permissionHandler.saveConfig();
    }

    public void update(){

        permissionHandler.getConfiguration().set("groups", groupNames);

        for(String group : groupNames)
            groupByName(group, consumer -> {
                permissionHandler.getConfiguration().set("group." + group + ".default", consumer.isDefaultGroup());
                permissionHandler.getConfiguration().set("group." + group + ".permission", consumer.getPermissions());
            });

        permissionHandler.saveConfig();
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<String> getGroupNames() {
        return groupNames;
    }
}
