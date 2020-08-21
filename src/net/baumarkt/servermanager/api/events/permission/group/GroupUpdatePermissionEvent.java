package net.baumarkt.servermanager.api.events.permission.group;

/*
 * Created on 16.08.2020 at 10:11
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.api.permission.objects.Group;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GroupUpdatePermissionEvent extends Event{

    private static HandlerList handlerList = new HandlerList();

    private Group group;
    private Type type;

    public GroupUpdatePermissionEvent(final Group group, final Type type){
        this.group = group;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Group getGroup() {
        return group;
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
