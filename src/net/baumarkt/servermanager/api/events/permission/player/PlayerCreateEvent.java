package net.baumarkt.servermanager.api.events.permission.player;

/*
 * Created on 16.08.2020 at 10:57
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCreateEvent extends Event {

    private static HandlerList handlerList = new HandlerList();

    private Player player;

    public PlayerCreateEvent(final Player player){
        this.player = player;
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
}
