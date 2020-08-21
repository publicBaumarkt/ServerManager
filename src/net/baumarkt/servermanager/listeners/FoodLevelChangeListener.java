package net.baumarkt.servermanager.listeners;

/*
 * Created on 14.08.2020 at 20:27
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {


    @EventHandler
    public void handle(final FoodLevelChangeEvent event){
        if(!ServerManager.getInstance().getSettingsConfig().getConfiguration().getBoolean("foodLevelChange")){
            event.setCancelled(true);
        }
    }

}
