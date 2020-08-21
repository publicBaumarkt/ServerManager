package net.baumarkt.servermanager.listeners;

/*
 * Created on 14.08.2020 at 20:28
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockPhysicsListener implements Listener {

    @EventHandler
    public void handle(final BlockPhysicsEvent event){
        if(!ServerManager.getInstance().getSettingsConfig().getConfiguration().getBoolean("blockPhysics")){
            event.setCancelled(true);
        }
    }

}
