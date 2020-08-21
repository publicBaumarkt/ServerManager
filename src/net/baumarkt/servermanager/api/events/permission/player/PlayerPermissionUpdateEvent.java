package net.baumarkt.servermanager.api.events.permission.player;

/*
 * Created on 16.08.2020 at 10:11
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.api.permission.objects.Group;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPermissionUpdateEvent extends Event {

    private static HandlerList handlerList = new HandlerList();

    private Player player;
    private String permission;
    private Type type;

    public PlayerPermissionUpdateEvent(final Player player, final String permission, final Type type){
        this.player = player;
        this.permission = permission;
        this.type = type;
    }

    public String getPermission() {
        return permission;
    }

    public Player getPlayer() {
        return player;
    }

    public Type getType() {
        return type;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public enum Type {
        ADD, REMOVE;
    }
}
