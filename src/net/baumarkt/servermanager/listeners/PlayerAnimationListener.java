package net.baumarkt.servermanager.listeners;

/*
 * Created on 14.08.2020 at 20:30
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;

public class PlayerAnimationListener implements Listener {

    @EventHandler
    public void handle(final PlayerAnimationEvent event){
        if(ServerManager.getInstance().getSettingsConfig().getConfiguration().get("armSwingAnimation").equals("false")){
            if(event.getAnimationType().equals(PlayerAnimationType.ARM_SWING))
                event.setCancelled(true);
        }
    }


}
