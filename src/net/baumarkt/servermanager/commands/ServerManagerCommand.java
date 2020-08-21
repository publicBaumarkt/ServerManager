package net.baumarkt.servermanager.commands;

/*
 * Created on 11.08.2020 at 09:48
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.utils.player.ServerPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerManagerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            final ServerPlayer serverPlayer = new ServerPlayer(player);

            if(serverPlayer.hasPermission("command")){

                switch (args.length){
                    case 0:
                        serverPlayer.getInventoryHolder().openMainMenuInventory();
                        break;
                }

            }else
                player.sendMessage(ServerManager.getInstance().getPrefix() +  "§cYou are not allowed to do this");
        }
        return false;
    }

}
