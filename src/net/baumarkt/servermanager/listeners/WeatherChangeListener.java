package net.baumarkt.servermanager.listeners;

/*
 * Created on 14.08.2020 at 20:28
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener implements Listener {

    @EventHandler
    public void handle(final WeatherChangeEvent event){
        if(!ServerManager.getInstance().getSettingsConfig().getConfiguration().getBoolean("weatherChange")){
            event.setCancelled(true);
        }
    }

}
