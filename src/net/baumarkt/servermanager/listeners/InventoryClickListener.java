package net.baumarkt.servermanager.listeners;

/*
 * Created on 11.08.2020 at 10:10
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.utils.Properties;
import net.baumarkt.servermanager.utils.funciton.Function;
import net.baumarkt.servermanager.utils.funciton.functions.plugin.installer.PluginInstallerCategory;
import net.baumarkt.servermanager.utils.player.ServerPlayer;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import javax.tools.JavaFileManager;
import java.io.File;
import java.io.IOException;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void handle(final InventoryClickEvent event){
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta() == null) return;
        if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
        if(event.getInventory() == null) return;
        if(event.getInventory().getTitle() == null) return;
        
        final Player player = (Player) event.getWhoClicked();
        final ServerPlayer serverPlayer = new ServerPlayer(player);

        if(serverPlayer.hasPermission("click")){
            if(event.getInventory().getTitle().equals(ServerManager.getInstance().getConfigHandler().getContent("inventories.mainMenu"))){
                event.setCancelled(true);

                if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§8» §b")) {
                    final Function function = ServerManager.getInstance().getFunctionHandler().getFunctionByName(event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§8» §b", ""));

                    if(function.isAvailable()  && serverPlayer.hasPermission(function.getPermission()))
                        function.onInventoryClick(player);
                }
            }else if(event.getInventory().getTitle().equals("§8» §bWorld Control")){
                event.setCancelled(true);

                if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§e")){
                    serverPlayer.getInventoryHolder().openWorldOptionsInventory(event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§e", ""));
                }else{
                    if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §9Create world")){
                        player.closeInventory();
                        player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Write the §aworld name §7in the chat. To cancel write §c\"exit\"");
                        ServerManager.getInstance().getPlayerChattingMap().put(player, "createWorld");
                    }else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §cBack")){
                        serverPlayer.getInventoryHolder().openMainMenuInventory();
                    }
                }
            }else if(event.getInventory().getTitle().equals("§8» §bServer Control")){
                event.setCancelled(true);

                switch (event.getCurrentItem().getItemMeta().getDisplayName()){
                    case "§8» §bBroadcast Message":
                        player.closeInventory();
                        player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Write the §amessage §7in the chat. To cancel write §c\"exit\"");

                        ServerManager.getInstance().getPlayerChattingMap().put(player, "broadcastMessage");
                        break;
                    case "§8» §cStop the server":
                        Bukkit.shutdown();
                        break;
                    case "§8» §cBack":
                        serverPlayer.getInventoryHolder().openMainMenuInventory();
                        break;
                    case "§aFood level change":
                        ServerManager.getInstance().changeServerSetting("foodLevelChange");
                        serverPlayer.getInventoryHolder().openServerControlFunctionInventory();
                        break;
                    case "§aWeather change":
                        ServerManager.getInstance().changeServerSetting("weatherChange");
                        serverPlayer.getInventoryHolder().openServerControlFunctionInventory();
                        break;
                    case "§aBlock physics":
                        ServerManager.getInstance().changeServerSetting("blockPhysics");
                        serverPlayer.getInventoryHolder().openServerControlFunctionInventory();
                        break;
                    case "§aArm swing animation":
                        ServerManager.getInstance().changeServerSetting("armSwingAnimation");
                        serverPlayer.getInventoryHolder().openServerControlFunctionInventory();
                        break;
                    case "§aCraft items":
                        ServerManager.getInstance().changeServerSetting("craftItems");
                        serverPlayer.getInventoryHolder().openServerControlFunctionInventory();
                        break;
                    case "§aPVP":
                        ServerManager.getInstance().propertiesValue(Properties.ServerProperty.PVP.getPropertyName(), consumer -> {
                            System.out.println(consumer);
                            Properties.setServerProperty(Properties.ServerProperty.PVP, !consumer.equals("true"));
                        });

                        Properties.savePropertiesFile();
                        ServerManager.getInstance().getSettingsConfig().saveConfig();

                        serverPlayer.getInventoryHolder().openServerControlFunctionInventory();
                        break;
                    case "§aWhitelist":
                        ServerManager.getInstance().propertiesValue(Properties.ServerProperty.WHITE_LIST.getPropertyName(), consumer -> {
                            Properties.setServerProperty(Properties.ServerProperty.WHITE_LIST, !consumer.equals("true"));
                        });
                        
                        Properties.savePropertiesFile();
                        ServerManager.getInstance().getSettingsConfig().saveConfig();

                        serverPlayer.getInventoryHolder().openServerControlFunctionInventory();
                        break;
                    case "§aMax players":
                        ServerManager.getInstance().propertiesValue(Properties.ServerProperty.MAX_PLAYERS.getPropertyName(), consumer -> {
                            int current =  Integer.parseInt(consumer);

                            if(event.getClick().isRightClick())
                                Properties.setServerProperty(Properties.ServerProperty.MAX_PLAYERS, current += 1);
                            else if(event.getClick().isLeftClick())
                                Properties.setServerProperty(Properties.ServerProperty.MAX_PLAYERS, current -= 1);
                        });

                        Properties.savePropertiesFile();
                        ServerManager.getInstance().getSettingsConfig().saveConfig();

                        serverPlayer.getInventoryHolder().openServerControlFunctionInventory();
                        break;
                    case "§aDifficulty":
                        ServerManager.getInstance().propertiesValue(Properties.ServerProperty.DIFFICULTY.getPropertyName(), consumer -> {
                            int current =  Integer.parseInt(consumer);

                            if(event.getClick().isRightClick())
                                if(current < 3)
                                    Properties.setServerProperty(Properties.ServerProperty.DIFFICULTY, current += 1);
                                else
                                    Properties.setServerProperty(Properties.ServerProperty.DIFFICULTY, current -= 1);
                            else if(event.getClick().isLeftClick())
                                if(current > 0)
                                    Properties.setServerProperty(Properties.ServerProperty.DIFFICULTY, current -= 1);
                                else
                                    Properties.setServerProperty(Properties.ServerProperty.DIFFICULTY, current += 1);
                        });

                        Properties.savePropertiesFile();
                        ServerManager.getInstance().getSettingsConfig().saveConfig();

                        serverPlayer.getInventoryHolder().openServerControlFunctionInventory();
                        break;
                    case "§aPlayer achievements":
                        ServerManager.getInstance().propertiesValue(Properties.ServerProperty.ANNOUNCE_PLAYER_ACHIEVEMENTS.getPropertyName(), consumer -> {
                            Properties.setServerProperty(Properties.ServerProperty.ANNOUNCE_PLAYER_ACHIEVEMENTS, !consumer.equals("true"));
                        });

                        Properties.savePropertiesFile();
                        ServerManager.getInstance().getSettingsConfig().saveConfig();

                        serverPlayer.getInventoryHolder().openServerControlFunctionInventory();
                        break;
                    case "§aMotd":
                        player.closeInventory();
                        player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Write the §amotd §7in the chat. To cancel write §c\"exit\"");
                        ServerManager.getInstance().getPlayerChattingMap().put(player, "motd");

                        break;
                }

            }else if(event.getInventory().getTitle().equals("§8» §bChat Control")) {
                event.setCancelled(true);

                if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7")){
                    final String colorName = event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§7", "");
                    final DyeColor dyeColor = DyeColor.valueOf(colorName);
                    final ChatColor chatColor = ServerManager.getInstance().translateDyeColorToChatColor(dyeColor);

                    ServerManager.getInstance().getSettingsConfig().getConfiguration().set("chatColor", chatColor.getChar());
                    ServerManager.getInstance().getSettingsConfig().saveConfig();
                    player.sendMessage(ServerManager.getInstance().getPrefix() + "§aChat color has been changed!");
                }else{
                    if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §cBack")){
                        serverPlayer.getInventoryHolder().openMainMenuInventory();
                    }else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §cReset Chat Color")){
                        ServerManager.getInstance().getSettingsConfig().getConfiguration().set("chatColor", "none");
                        ServerManager.getInstance().getSettingsConfig().saveConfig();

                        player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Chat color has been §areset!");
                    }
                }
            }else if(event.getInventory().getTitle().equals("§8» §bPlayer Control")) {
                event.setCancelled(true);

                if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7")){
                    final Player target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§7", ""));

                    if(target != null)
                        serverPlayer.getInventoryHolder().openPlayerControlOptionsInventory(target);
                }else{
                    if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §cBack")){
                        serverPlayer.getInventoryHolder().openMainMenuInventory();
                    }
                }
            }else if(event.getInventory().getTitle().equals("§8» §bPlugin Control")) {
                event.setCancelled(true);

                if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7")){
                    final Plugin plugin = Bukkit.getPluginManager().getPlugin(event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§7", ""));

                    serverPlayer.getInventoryHolder().openPluginControlOptionsInventory(plugin);
                }else{
                    if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §cBack")){
                        serverPlayer.getInventoryHolder().openMainMenuInventory();
                    }
                }
            }else if(event.getInventory().getTitle().equals("§8» §bPlugin Installer")) {
                event.setCancelled(true);

                if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §cBack")){
                    serverPlayer.getInventoryHolder().openMainMenuInventory();
                }else{
                    if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7UTILITY")){
                        serverPlayer.getInventoryHolder().openPluginInstallerSelectedCategoryInventory(PluginInstallerCategory.UTILITY);
                    }else if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7WORLD")){
                        serverPlayer.getInventoryHolder().openPluginInstallerSelectedCategoryInventory(PluginInstallerCategory.WORLD);
                    }else if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7FUN")){
                        serverPlayer.getInventoryHolder().openPluginInstallerSelectedCategoryInventory(PluginInstallerCategory.FUN);
                    }else if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7GAME")){
                        serverPlayer.getInventoryHolder().openPluginInstallerSelectedCategoryInventory(PluginInstallerCategory.GAME);
                    }
                }
            }else{
                if(event.getInventory().getTitle().startsWith("§8» §bWorld §8┃ §7")){
                    event.setCancelled(true);

                    final World world = Bukkit.getWorld(event.getInventory().getTitle().replaceAll("§8» §bWorld §8┃ §7", ""));

                    if(event.getCurrentItem().getType().equals(Material.BARRIER)){
                        Bukkit.unloadWorld(world, false);
                        try {
                            FileUtils.deleteDirectory(new File(world.getName()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        player.sendMessage(ServerManager.getInstance().getPrefix() + "§7The world has been §asuccessfully§7 erased!");
                        player.closeInventory();
                    }else if(event.getCurrentItem().getType().equals(Material.ENDER_PEARL)){
                        player.teleport(world.getSpawnLocation());
                    }else if(event.getCurrentItem().getType().equals(Material.COMMAND)){
                        if(world.getGameRuleValue("commandBlockOutput").equals("true")) {
                            world.setGameRuleValue("commandBlockOutput", "false");
                            serverPlayer.getInventoryHolder().openWorldOptionsInventory(world.getName());
                        }else{
                            world.setGameRuleValue("commandBlockOutput", "true");
                            serverPlayer.getInventoryHolder().openWorldOptionsInventory(world.getName());
                        }
                    }else if(event.getCurrentItem().getType().equals(Material.ENDER_CHEST)){
                        if(world.getGameRuleValue("keepInventory").equals("true")) {
                            world.setGameRuleValue("keepInventory", "false");
                            serverPlayer.getInventoryHolder().openWorldOptionsInventory(world.getName());
                        }else{
                            world.setGameRuleValue("keepInventory", "true");
                            serverPlayer.getInventoryHolder().openWorldOptionsInventory(world.getName());
                        }
                    }
                }
                else if(event.getInventory().getTitle().startsWith("§8» §bPlayer §8┃ §7")){
                    event.setCancelled(true);

                    final String targetName = event.getInventory().getTitle().replaceAll("§8» §bPlayer §8┃ §7", "");

                    if(Bukkit.getPlayer(targetName) == null) return;

                    final Player target = Bukkit.getPlayer(targetName);

                    switch (event.getCurrentItem().getItemMeta().getDisplayName()){
                        case "§8» §cBack":
                            serverPlayer.getInventoryHolder().openPlayerControlFunctionInventory();
                            break;
                        case "§7Open player's inventory":
                            player.openInventory(target.getInventory());
                            break;
                        case "§7Open player's enderchest":
                            player.openInventory(target.getEnderChest());
                            break;
                        case "§7Players change hearts":
                            double currentHealth = target.getHealth();

                            if(event.isRightClick()){
                                currentHealth -= 0.5;
                                target.setMaxHealth(currentHealth);
                                target.setHealth(currentHealth);
                            }else if(event.isLeftClick()){
                                currentHealth += 0.5;
                                target.setMaxHealth(currentHealth);
                                target.setHealth(currentHealth);
                            }

                            serverPlayer.getInventoryHolder().openPlayerControlOptionsInventory(player);
                            break;
                        case "§7Teleport to player":
                            player.teleport(target.getLocation());
                            break;
                        case "§7Change player gamemode":
                            if(target.getGameMode().equals(GameMode.SURVIVAL))
                                target.setGameMode(GameMode.CREATIVE);
                            else if(target.getGameMode().equals(GameMode.CREATIVE))
                                target.setGameMode(GameMode.ADVENTURE);
                            else if(target.getGameMode().equals(GameMode.ADVENTURE))
                                target.setGameMode(GameMode.SPECTATOR);
                            else if(target.getGameMode().equals(GameMode.SPECTATOR))
                                target.setGameMode(GameMode.SURVIVAL);

                            serverPlayer.getInventoryHolder().openPlayerControlOptionsInventory(player);
                            break;

                    }
                }
                else if(event.getInventory().getTitle().startsWith("§8» §bPlugin §8┃ §7")){
                    event.setCancelled(true);

                    final Plugin plugin = Bukkit.getPluginManager().getPlugin(event.getInventory().getTitle().replaceAll("§8» §bPlugin §8┃ §7", ""));

                    if(event.getCurrentItem().getType().equals(Material.BARRIER)){
                        Bukkit.getPluginManager().disablePlugin(plugin);
                        serverPlayer.getInventoryHolder().openPluginControlFunctionInventory();
                    }else if(event.getCurrentItem().getType().equals(Material.EMERALD)){
                        Bukkit.getPluginManager().enablePlugin(plugin);
                        serverPlayer.getInventoryHolder().openPluginControlFunctionInventory();
                    }else if(event.getCurrentItem().getType().equals(Material.SKULL_ITEM))
                        if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §cBack")){
                            serverPlayer.getInventoryHolder().openPluginControlFunctionInventory();
                        }
                }
                else if(event.getInventory().getTitle().startsWith("§8» §bInstaller §8┃ §7")){
                    event.setCancelled(true);

                    if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §cBack"))
                        serverPlayer.getInventoryHolder().openPluginInstallerFunctionInventory();
                    else{
                        if(event.getCurrentItem().getType().equals(Material.PAPER)){
                            final String id = event.getCurrentItem().getItemMeta().getDisplayName().split(" ")[0].replaceAll("§3§o", "");
                            final File file = new File("plugins/" + id.replaceAll(":", "") + "Plugin.jar");

                            player.sendMessage(ServerManager.getInstance().getPrefix() + "§eTry to install plugin ...");

                            try {
                                ServerManager.getInstance().getPluginInstaller().downloadSpigotMcPlugin(Integer.parseInt(id.replaceAll(":", "")), file);
                                player.sendMessage(ServerManager.getInstance().getPrefix() + "§aInstallation successful!");
                                final Plugin plugin = Bukkit.getPluginManager().loadPlugin(file);

                                Bukkit.getPluginManager().enablePlugin(plugin);
                            } catch (IOException exception) {
                                player.sendMessage(ServerManager.getInstance().getPrefix() + "§cInstallation unsuccessful!");
                                exception.printStackTrace();
                            } catch (InvalidDescriptionException | InvalidPluginException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }
                else if(event.getInventory().getTitle().startsWith("§8» §bPermission")){
                    event.setCancelled(true);

                    if(event.getInventory().getTitle().equals("§8» §bPermission Control")){
                        if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7")){
                            ServerManager.getInstance().getPermissionHandler().getPlayerGroupHandler().groupByName(event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§7", ""), consumer -> {
                                serverPlayer.getInventoryHolder().openPermissionControlSelectedGroupInventory(consumer);
                            });
                            return;
                        }

                        switch (event.getCurrentItem().getItemMeta().getDisplayName()){
                            case "§8» §cBack":
                                serverPlayer.getInventoryHolder().openMainMenuInventory();
                                break;
                            case "§8» §aAdd the permission to a player":
                                serverPlayer.getInventoryHolder().openPermissionControlAddPermissionToAPlayerInventory();
                                break;
                            case "§8» §aCreate a group":
                                player.closeInventory();
                                player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Write the name of the group in the chat");
                                player.sendMessage(ServerManager.getInstance().getPrefix() + "§7To cancel, write §c\"exit\" §7 in the chat");

                                ServerManager.getInstance().getPlayerChattingMap().put(player, "permission:createGroup");
                                break;
                        }
                    }else if(event.getInventory().getTitle().equals("§8» §bPermission §8┃ §7Add")){
                        if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7")){
                            final String playerName = event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§7", "");
                            player.closeInventory();

                            player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Write the name of the permission in the chat");
                            player.sendMessage(ServerManager.getInstance().getPrefix() + "§7To cancel, write §c\"exit\" §7 in the chat");
                            ServerManager.getInstance().getPlayerChattingMap().put(player,"permission:add:" + playerName);
                        }else{
                            if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §cBack")){
                                serverPlayer.getInventoryHolder().openPermissionControlFunctionInventory();
                            }else if(event.getCurrentItem().getItemMeta().getDisplayName().equals("§8» §aDefine players via chat")){

                                player.closeInventory();

                                player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Write the name of the player in the chat");
                                player.sendMessage(ServerManager.getInstance().getPrefix() + "§7To cancel, write §c\"exit\" §7 in the chat");
                                ServerManager.getInstance().getPlayerChattingMap().put(player, "addPermissionWithDefinePlayers");
                            }
                        }
                    }
                }
                else if(event.getInventory().getTitle().startsWith("§8» §bGroup §8┃ §7")){
                    event.setCancelled(true);

                    final String groupName = event.getInventory().getTitle().replaceAll("§8» §bGroup §8┃ §7", "");

                    switch (event.getCurrentItem().getItemMeta().getDisplayName()){
                        case "§7Default group":
                            ServerManager.getInstance().getPermissionHandler().getPlayerGroupHandler().groupByName(groupName, consumer -> {
                                if(consumer.isDefaultGroup())
                                    consumer.setDefaultGroup(false);
                                else
                                    consumer.setDefaultGroup(true);

                                serverPlayer.getInventoryHolder().openPermissionControlSelectedGroupInventory(consumer);
                            });

                            break;
                        case "§7Do players in the group":
                            player.closeInventory();
                            player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Write the name of the player in the chat");
                            player.sendMessage(ServerManager.getInstance().getPrefix() + "§7To cancel, write §c\"exit\" §7 in the chat");

                            ServerManager.getInstance().getPlayerChattingMap().put(player,"group:addPlayer:" + groupName);
                            break;
                        case "§7Add permission":
                            player.closeInventory();
                            player.sendMessage(ServerManager.getInstance().getPrefix() + "§7Write the name of the permission in the chat");
                            player.sendMessage(ServerManager.getInstance().getPrefix() + "§7To cancel, write §c\"exit\" §7 in the chat");

                            ServerManager.getInstance().getPlayerChattingMap().put(player, "group:addPermission:" + groupName);
                            break;
                        case "§8» §cBack":
                            serverPlayer.getInventoryHolder().openPermissionControlFunctionInventory();
                            break;
                    }
                }
            }
        }
    }

}
