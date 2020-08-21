package net.baumarkt.servermanager.listeners;

/*
 * Created on 11.08.2020 at 12:14
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.api.permission.objects.Group;
import net.baumarkt.servermanager.utils.GameProfileBuilder;
import net.baumarkt.servermanager.utils.Properties;
import net.baumarkt.servermanager.utils.player.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void handle(final PlayerChatEvent event){
        final Player player = event.getPlayer();
        final ServerPlayer serverPlayer = new ServerPlayer(player);

        if(!ServerManager.getInstance().getSettingsConfig().getConfiguration().get("chatColor").equals("none"))
            event.setMessage("§" + ServerManager.getInstance().getSettingsConfig().getConfiguration().get("chatColor") + event.getMessage());

        if(ServerManager.getInstance().getPlayerChattingMap().containsKey(player)){
            event.setCancelled(true);

            switch (ServerManager.getInstance().getPlayerChattingMap().get(player)){
                case "createWorld":
                    if(event.getMessage().equalsIgnoreCase("exit")){
                        player.sendMessage(ServerManager.getInstance().getPrefix() + "§cExit!");
                        ServerManager.getInstance().getPlayerChattingMap().remove(player);

                        return;
                    }

                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§7World is being created ...");
                    Bukkit.createWorld(new WorldCreator(event.getMessage()));

                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§7World was §acreated§7!");
                    serverPlayer.getInventoryHolder().openMainMenuInventory();
                    ServerManager.getInstance().getPlayerChattingMap().remove(player);

                    return;
                case "broadcastMessage":
                    if(event.getMessage().equalsIgnoreCase("exit")){
                        player.sendMessage(ServerManager.getInstance().getPrefix() + "§cExit!");
                        ServerManager.getInstance().getPlayerChattingMap().remove(player);

                        return;
                    }

                    Bukkit.broadcastMessage(event.getMessage().replaceAll("&", "§"));
                    serverPlayer.getInventoryHolder().openServerControlFunctionInventory();
                    ServerManager.getInstance().getPlayerChattingMap().remove(player);
                    return;
                case "motd":
                    if(event.getMessage().equalsIgnoreCase("exit")){
                        player.sendMessage(ServerManager.getInstance().getPrefix() + "§cExit!");
                        ServerManager.getInstance().getPlayerChattingMap().remove(player);

                        return;
                    }

                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Motd§8: §7" + event.getMessage());
                    Properties.setServerProperty(Properties.ServerProperty.MOTD, event.getMessage());
                    ServerManager.getInstance().getPlayerChattingMap().remove(player);
                    return;
                case "permission:createGroup":
                    if(event.getMessage().equalsIgnoreCase("exit")){
                        player.sendMessage(ServerManager.getInstance().getPrefix() + "§cExit!");
                        ServerManager.getInstance().getPlayerChattingMap().remove(player);
                        return;
                    }

                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Group created §asuccessfully§8:");
                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Name§8: §b" + event.getMessage());
                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Default§8: §efalse");

                    ServerManager.getInstance().getPermissionHandler().getPlayerGroupHandler().createGroup(event.getMessage(), false);
                    ServerManager.getInstance().getPlayerChattingMap().remove(player);

                    return;
                case "addPermissionWithDefinePlayers":
                    if(event.getMessage().equalsIgnoreCase("exit")){
                        player.sendMessage(ServerManager.getInstance().getPrefix() + "§cExit!");
                        ServerManager.getInstance().getPlayerChattingMap().remove(player);
                        return;
                    }

                    if(Bukkit.getPlayer(event.getMessage()) == null){
                        player.sendMessage(ServerManager.getInstance().getPrefix() + "§7The specified player is not §conline§7!");
                        ServerManager.getInstance().getPlayerChattingMap().remove(player);

                        return;
                    }

                    player.closeInventory();

                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Write the name of the permission in the chat");
                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§7To cancel, write §c\"exit\" §7 in the chat");
                    ServerManager.getInstance().getPlayerChattingMap().put(player,"permission:add:" + event.getMessage());

                    ServerManager.getInstance().getPlayerChattingMap().remove(player);

                    break;
            }

            if(ServerManager.getInstance().getPlayerChattingMap().get(player).startsWith("permission:add:")){
                if(event.getMessage().equalsIgnoreCase("exit")){
                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§cExit!");
                    ServerManager.getInstance().getPlayerChattingMap().remove(player);

                    return;
                }

                final String split = ServerManager.getInstance().getPlayerChattingMap().get(player).split(":")[2];

                if(Bukkit.getPlayer(split) == null) {
                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§7The specified player is not §conline§7!");
                    ServerManager.getInstance().getPlayerChattingMap().remove(player);

                    return;
                }

                ServerManager
                        .getInstance()
                        .getPermissionHandler()
                        .getPlayerAPI(Bukkit.getPlayer(split).getUniqueId())
                        .addPermission(event.getMessage());

                player.sendMessage(ServerManager.getInstance().getPrefix() + "§7You have given player §a" + split + " §7permission §b" + event.getMessage() + "§7!");
                ServerManager.getInstance().getPlayerChattingMap().remove(player);
            }else if(ServerManager.getInstance().getPlayerChattingMap().get(player).startsWith("group:addPermission:")){
                if(event.getMessage().equalsIgnoreCase("exit")){
                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§cExit!");
                    ServerManager.getInstance().getPlayerChattingMap().remove(player);

                    return;
                }

                final String split = ServerManager.getInstance().getPlayerChattingMap().get(player).split(":")[2];

                ServerManager.getInstance().getPermissionHandler().getPlayerGroupHandler().groupByName(split, consumer -> {
                    consumer.addPermission(event.getMessage());
                });

                player.sendMessage(ServerManager.getInstance().getPrefix() + "§7You have §asuccessfully§7 added the permission to the group!");

                ServerManager.getInstance().getPlayerChattingMap().remove(player);

            }
            else if(ServerManager.getInstance().getPlayerChattingMap().get(player).startsWith("group:addPlayer:")){
                if(event.getMessage().equalsIgnoreCase("exit")){
                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§cExit!");
                    ServerManager.getInstance().getPlayerChattingMap().remove(player);

                    return;
                }

                final String groupName = ServerManager.getInstance().getPlayerChattingMap().get(player).split(":")[2];
                final Player target = Bukkit.getPlayer(event.getMessage());

                if(target != null){
                    ServerManager.getInstance().getPermissionHandler().getPlayerAPI(target.getUniqueId()).addGroup(groupName);
                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§7The group was §aassigned§7 to the player!");
                }else
                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§7The specified player is not §conline§7!");

                ServerManager.getInstance().getPlayerChattingMap().remove(player);
            }
        }
    }

}
