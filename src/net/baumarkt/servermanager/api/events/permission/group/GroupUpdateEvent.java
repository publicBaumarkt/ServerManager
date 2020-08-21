package net.baumarkt.servermanager.api.events.permission.group;

/*
 * Created on 18.08.2020 at 18:16
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.api.permission.objects.Group;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GroupUpdateEvent extends Event {

    private static HandlerList handlerList = new HandlerList();

    private Group group;
    private Type type;

    public GroupUpdateEvent(final Group group, final Type type){
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
        CREATE, CHANGE_DEFAULT_TO_TRUE, CHANGE_DEFAULT_TO_FALSE;
    }

}
