package net.baumarkt.servermanager.listeners;

/*
 * Created on 14.08.2020 at 21:08
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftItemListener implements Listener {

    @EventHandler
    public void handle(final CraftItemEvent event){
        if(!ServerManager.getInstance().getSettingsConfig().getConfiguration().getBoolean("craftItems")){
            event.setCancelled(true);
        }
    }

}
