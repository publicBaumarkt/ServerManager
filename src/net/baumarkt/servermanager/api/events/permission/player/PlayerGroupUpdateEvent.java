package net.baumarkt.servermanager.api.events.permission.player;

/*
 * Created on 16.08.2020 at 10:09
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.api.permission.objects.Group;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerGroupUpdateEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private Player player;
    private Group updateGroup;
    private Type type;

    public PlayerGroupUpdateEvent(final Player player, final Group group, final Type type){
        this.player = player;
        this.updateGroup = group;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Group getUpdateGroup() {
        return updateGroup;
    }

    public Player getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public enum Type{
        ADD, REMOVE;
    }
}
