package net.baumarkt.servermanager.utils.player;

/*
 * Created on 11.08.2020 at 08:19
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.api.permission.objects.Group;
import net.baumarkt.servermanager.utils.ItemBuilder;
import net.baumarkt.servermanager.utils.funciton.Function;
import net.baumarkt.servermanager.utils.funciton.functions.plugin.installer.PluginInstallerCategory;
import net.baumarkt.servermanager.utils.funciton.functions.plugin.installer.objects.PluginInstalling;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class ServerPlayer {

    private final Player player;

    public ServerPlayer(final Player player){
        this.player = player;
    }

    public ServerPlayerInventory getInventoryHolder(){
        return new ServerPlayerInventory(this);
    }

    public boolean hasPermission(final String permission){
        if(ServerManager.getInstance().getFunctionHandler().getFunctionByName("Permission Control").isAvailable())
            if(ServerManager.getInstance().getPermissionHandler().getConfiguration().getStringList("playersWithAllServerManagerSettings").contains(player.getName()))
                return true;

        return player.hasPermission(ServerManager.getInstance().getConfigHandler().getContent("permissions.all"))
                || player.hasPermission(ServerManager.getInstance().getConfigHandler().getContent("permissions." + permission));
    }

    public Player getPlayer() {
        return player;
    }

    public class ServerPlayerInventory {

        private final ServerPlayer serverPlayer;

        public ServerPlayerInventory(final ServerPlayer serverPlayer){
            this.serverPlayer = serverPlayer;
        }

        public void openMainMenuInventory(){
            final Inventory inventory = Bukkit.createInventory(null, 9*6, ServerManager.getInstance().getConfigHandler().getContent("inventories.mainMenu"));

            for(int i = 0; i < 9; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            for(int i = 42; i < 52; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            inventory.setItem(52, new ItemBuilder(Material.SKULL_ITEM)
                    .setDisplayName(ServerManager.getInstance().getConfigHandler().getContent("inventories.mainMenu.back"))
                    .setData(3)
                    .spigot()
                    .setSkullTexture("816ea34a6a6ec5c051e6932f1c471b7012b298d38d179f1b487c413f51959cd4")
                    .build());

            inventory.setItem(53, new ItemBuilder(Material.SKULL_ITEM)
                    .setDisplayName(ServerManager.getInstance().getConfigHandler().getContent("inventories.mainMenu.nextPage"))
                    .setData(3)
                    .spigot()
                    .setSkullTexture("9c9ec71c1068ec6e03d2c9287f9da9193639f3a635e2fbd5d87c2fabe6499")
                    .build());

            for(Function function : ServerManager.getInstance().getFunctionHandler().getFunctions()){
                if(function.isAvailable())
                    inventory.addItem(new ItemBuilder(function.getItemStack()).setDisplayName("§8» §b" + function.getName()).addLore(function.getDescription()).build());
            }

            player.openInventory(inventory);
        }

        public void openWorldOptionsInventory(final String worldName){
            String finalName = worldName;

            if(worldName.length() > 31)
                finalName = worldName.substring(0, 31);

            final Inventory inventory = Bukkit.createInventory(null, 9, "§8» §bWorld §8┃ §7" + finalName);
            final World world = Bukkit.getWorld(worldName);

            inventory.addItem(new ItemBuilder(Material.PAPER)
                    .setDisplayName("§8» §aInformations")
                    .addLore(" ",
                            "§7Selected world§8: §7" + worldName,
                            "§7Command blocks§8: §7" + world.getGameRuleValue("commandBlockOutput"),
                            "§7Keep inventory§8: §7" + world.getGameRuleValue("keepInventory"))
                    .build());

            inventory.addItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());
            inventory.addItem(new ItemBuilder(Material.BARRIER).setDisplayName("§cDelete world").build());
            inventory.addItem(new ItemBuilder(Material.ENDER_PEARL).setDisplayName("§5Teleport").build());
            inventory.addItem(new ItemBuilder(Material.COMMAND).setDisplayName("§cCommand blocks").addLore(world.getGameRuleValue("commandBlockOutput")).build());
            inventory.addItem(new ItemBuilder(Material.ENDER_CHEST).setDisplayName("§cKeep inventory").addLore(world.getGameRuleValue("keepInventory")).build());

            player.openInventory(inventory);
        }

        public void openPlayerControlOptionsInventory(final Player target){
            final Inventory inventory = Bukkit.createInventory(null, 9, "§8» §bPlayer §8┃ §7" + target.getName());

            inventory.addItem(new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setOwner(target.getName())
                    .setDisplayName("§8» §b" + target.getName())
                    .addLore(" ", "§7Ping§8: §a" + ((CraftPlayer)target).getHandle().ping)
                    .build());

            inventory.addItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());
            inventory.addItem(new ItemBuilder(Material.CHEST).setDisplayName("§7Open player's inventory").build());
            inventory.addItem(new ItemBuilder(Material.ENDER_CHEST).setDisplayName("§7Open player's enderchest").build());
            inventory.addItem(new ItemBuilder(Material.APPLE).setDisplayName("§7Players change hearts").addLore("§7Current§8: §7" + target.getHealth(), "§7Rightclick§8: §7+1", "§7Leftclick§8: §7-1").build());
            inventory.addItem(new ItemBuilder(Material.ENDER_PEARL).setDisplayName("§7Teleport to player").build());
            inventory.addItem(new ItemBuilder(Material.ANVIL).setDisplayName("§7Change player gamemode").addLore("", "§7Current§8: §7" + target.getGameMode().name()).build());

            inventory.setItem(8, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §cBack")
                    .spigot()
                    .setSkullTexture("816ea34a6a6ec5c051e6932f1c471b7012b298d38d179f1b487c413f51959cd4")
                    .build());

            player.openInventory(inventory);
        }

        public void openPluginControlOptionsInventory(final Plugin plugin){
            final Inventory inventory = Bukkit.createInventory(null, 9, "§8» §bPlugin §8┃ §7" + plugin.getDescription().getName());

            inventory.addItem(new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setOwner(player.getName())
                    .setDisplayName("§8» §b" + plugin.getName())
                    .addLore(" ", "§7Author§8: §a" + plugin.getDescription().getAuthors().get(0), "§7Activated§8: §7" + plugin.isEnabled())
                    .build());

            inventory.addItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            if(plugin.isEnabled())
                inventory.addItem(new ItemBuilder(Material.BARRIER).setDisplayName("§7Deactivate plugin").build());
            else
                inventory.addItem(new ItemBuilder(Material.EMERALD).setDisplayName("§7Activate plugin").build());

            inventory.setItem(8, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §cBack")
                    .spigot()
                    .setSkullTexture("816ea34a6a6ec5c051e6932f1c471b7012b298d38d179f1b487c413f51959cd4")
                    .build());

            player.openInventory(inventory);
        }

        public void openServerControlFunctionInventory(){
            final Inventory inventory = Bukkit.createInventory(null, 9*6, "§8» §bServer Control");

            for(int i = 0; i < 9; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            for(int i = 41; i < 53; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            inventory.setItem(51, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §bBroadcast Message")
                    .spigot()
                    .setSkullTexture("8ae7bf4522b03dfcc866513363eaa9046fddfd4aa6f1f0889f03c1e6216e0ea0")
                    .build());

            inventory.setItem(52, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §cStop the server")
                    .spigot()
                    .setSkullTexture("65ed481e04524f8ebcaf840a7d6f167f9941670c57ac88a93c14cec239d88389")
                    .build());

            inventory.setItem(53, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §cBack")
                    .spigot()
                    .setSkullTexture("816ea34a6a6ec5c051e6932f1c471b7012b298d38d179f1b487c413f51959cd4")
                    .build());

            inventory.setItem(4, new ItemBuilder(Material.PAPER).setDisplayName("§8» §bServer Control").build());


            ServerManager.getInstance().propertiesValue("pvp", value -> {
                inventory.addItem(new ItemBuilder(Material.IRON_SWORD)
                        .setDisplayName("§aPVP")
                        .addLore("§7Current§8: §7" + value).build());
            });

            ServerManager.getInstance().propertiesValue("white-list", value -> {
                inventory.addItem(new ItemBuilder(Material.SIGN)
                        .setDisplayName("§aWhitelist")
                        .addLore("§7Current§8: §7" + value).build());
            });

            ServerManager.getInstance().propertiesValue("max-players", value -> {
                inventory.addItem(new ItemBuilder(Material.NAME_TAG)
                        .setDisplayName("§aMax players")
                        .addLore("§7Current§8: §7" + value, " ", "§7Rightclick§8: §7+1", "§7Leftclick§8: §7-1").build());
            });

            ServerManager.getInstance().propertiesValue("difficulty", value -> {
                inventory.addItem(new ItemBuilder(Material.MOB_SPAWNER)
                        .setDisplayName("§aDifficulty")
                        .addLore("§7Current§8: §7" + value, " ", "§7Rightclick§8: §7+1", "§7Leftclick§8: §7-1")
                        .build());
            });

            ServerManager.getInstance().propertiesValue("motd", value -> {
                inventory.addItem(new ItemBuilder(Material.ANVIL)
                        .setDisplayName("§aMotd")
                        .addLore("§7Current§8: §7" + value).build());
            });

            ServerManager.getInstance().propertiesValue("announce-player-achievements", value -> {
                inventory.addItem(new ItemBuilder(Material.GOLD_INGOT)
                        .setDisplayName("§aPlayer achievements")
                        .addLore("§7Current§8: §7" + value).build());
            });

            inventory.addItem(new ItemBuilder(Material.COOKED_CHICKEN)
                    .setDisplayName("§aFood level change")
                    .addLore("§7Current§8: §7" + ServerManager.getInstance().getSettingsConfig().getConfiguration().getBoolean("foodLevelChange")).build());

            inventory.addItem(new ItemBuilder(Material.WATER_BUCKET)
                    .setDisplayName("§aWeather change")
                    .addLore("§7Current§8: §7" + ServerManager.getInstance().getSettingsConfig().getConfiguration().getBoolean("weatherChange")).build());

            inventory.addItem(new ItemBuilder(Material.GRASS)
                    .setDisplayName("§aBlock physics")
                    .addLore("§7Current§8: §7" + ServerManager.getInstance().getSettingsConfig().getConfiguration().getBoolean("blockPhysics")).build());

            inventory.addItem(new ItemBuilder(Material.ITEM_FRAME)
                    .setDisplayName("§aArm swing animation")
                    .addLore("§7Current§8: §7" + ServerManager.getInstance().getSettingsConfig().getConfiguration().getBoolean("armSwingAnimation") + " §8(§4Function currently not available.§8)").build());

            inventory.addItem(new ItemBuilder(Material.WORKBENCH)
                    .setDisplayName("§aCraft items")
                    .addLore("§7Current§8: §7" + ServerManager.getInstance().getSettingsConfig().getConfiguration().getBoolean("craftItems")).build());

            player.openInventory(inventory);
        }

        public void openPlayerControlFunctionInventory(){
            final Inventory inventory = Bukkit.createInventory(null, 9*6, "§8» §bPlayer Control");

            for(int i = 0; i < 9; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            for(int i = 43; i < 53; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            inventory.setItem(53, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §cBack")
                    .spigot()
                    .setSkullTexture("816ea34a6a6ec5c051e6932f1c471b7012b298d38d179f1b487c413f51959cd4")
                    .build());

            inventory.setItem(4, new ItemBuilder(Material.PAPER).setDisplayName("§8» §bPlayer Control").build());

            for(Player players : Bukkit.getOnlinePlayers())
                inventory.addItem(new ItemBuilder(Material.SKULL_ITEM)
                        .setData(3)
                        .setOwner(players.getName())
                        .setDisplayName("§7" + players.getName())
                        .addLore(" ", "§7Click for more information")
                        .build());

            player.openInventory(inventory);
        }

        public void openChatControlFunctionInventory(){
            final Inventory inventory = Bukkit.createInventory(null, 9*6, "§8» §bChat Control");

            for(int i = 0; i < 9; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            for(int i = 42; i < 52; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            inventory.setItem(53, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §cBack")
                    .spigot()
                    .setSkullTexture("816ea34a6a6ec5c051e6932f1c471b7012b298d38d179f1b487c413f51959cd4")
                    .build());

            inventory.setItem(52, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §cReset Chat Color")
                    .spigot()
                    .setSkullTexture("65ed481e04524f8ebcaf840a7d6f167f9941670c57ac88a93c14cec239d88389")
                    .build());

            inventory.setItem(4, new ItemBuilder(Material.PAPER).setDisplayName("§8» §bChat Control").build());

            for(DyeColor chatColor : DyeColor.values())
                inventory.addItem(new ItemBuilder(Material.INK_SACK).setData(chatColor.getDyeData()).setDisplayName("§7" + chatColor.name()).build());


            player.openInventory(inventory);
        }

        public void openPluginControlFunctionInventory(){
            final Inventory inventory = Bukkit.createInventory(null, 9*6, "§8» §bPlugin Control");

            for(int i = 0; i < 9; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            for(int i = 43; i < 53; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            inventory.setItem(53, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §cBack")
                    .spigot()
                    .setSkullTexture("816ea34a6a6ec5c051e6932f1c471b7012b298d38d179f1b487c413f51959cd4")
                    .build());

            inventory.setItem(4, new ItemBuilder(Material.PAPER).setDisplayName("§8» §bPlugin Control").build());

            for(Plugin plugin : Bukkit.getPluginManager().getPlugins())
                inventory.addItem(new ItemBuilder(Material.SKULL_ITEM)
                        .setData(3)
                        .spigot()
                        .setSkullTexture("f37cae5c51eb1558ea828f58e0dff8e6b7b0b1a183d737eecf714661761")
                        .setDisplayName("§7" + plugin.getDescription().getName())
                        .addLore(" ", "§7Author§8: §7" + plugin.getDescription().getAuthors().get(0), "", "§7Click for options")
                        .build());

            player.openInventory(inventory);
        }

        public void openPluginInstallerFunctionInventory(){
            final Inventory inventory = Bukkit.createInventory(null, 9*6, "§8» §bPlugin Installer");

            for(int i = 0; i < 9; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            for(int i = 43; i < 53; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            inventory.setItem(53, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §cBack")
                    .spigot()
                    .setSkullTexture("816ea34a6a6ec5c051e6932f1c471b7012b298d38d179f1b487c413f51959cd4")
                    .build());

            for(PluginInstallerCategory category : PluginInstallerCategory.values())
                inventory.addItem(category.getItemStack());

            inventory.setItem(4, new ItemBuilder(Material.PAPER).setDisplayName("§8» §bPlugin Installer").build());

            player.openInventory(inventory);
        }

        public void openPluginInstallerSelectedCategoryInventory(final PluginInstallerCategory category){
            final Inventory inventory = Bukkit.createInventory(null, 9*6, "§8» §bInstaller §8┃ §7" + category.name());

            for(int i = 0; i < 9; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            for(int i = 43; i < 53; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            for(PluginInstalling pluginInstalling : ServerManager.getInstance().getPluginInstaller().getPluginInstallingList()){
                if(pluginInstalling.getPluginInstallerCategory().equals(category)){
                    inventory.addItem(new ItemBuilder(Material.PAPER)
                            .setDisplayName("§3§o" + pluginInstalling.getId() + ": §7" + pluginInstalling.getName())
                            .addLore("",
                                    "§7Description§8: §7" + pluginInstalling.getDescription(),
                                    "§7Total downloads§8: §7" + pluginInstalling.getTotalDownloads(),
                                    "§7Rating§8: §6" + pluginInstalling.getRating() + "§8/§a5 §6✦",
                                    "§7Author§8: §7" + pluginInstalling.getAuhor())
                            .build());
                }
            }

            inventory.setItem(53, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §cBack")
                    .spigot()
                    .setSkullTexture("816ea34a6a6ec5c051e6932f1c471b7012b298d38d179f1b487c413f51959cd4")
                    .build());

            player.openInventory(inventory);
        }

        public void openPermissionControlSelectedGroupInventory(Group group){
            final Inventory inventory = Bukkit.createInventory(null, 9, "§8» §bGroup §8┃ §7" + group.getName());

            inventory.addItem(new ItemBuilder(Material.PAPER)
                    .setDisplayName("§8» §b" + group.getName())
                    .addLore(" ", "§7Default group§8: §a" + group.isDefaultGroup(), "")
                    .addLore(group.getPermissions())
                    .build());

            inventory.addItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());
            inventory.addItem(new ItemBuilder(Material.NAME_TAG).setDisplayName("§7Default group").addLore("" + group.isDefaultGroup()).build());
            inventory.addItem(new ItemBuilder(Material.DIAMOND).setDisplayName("§7Do players in the group").build());
            inventory.addItem(new ItemBuilder(Material.EMERALD).setDisplayName("§7Add permission").build());

            inventory.setItem(8, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §cBack")
                    .spigot()
                    .setSkullTexture("816ea34a6a6ec5c051e6932f1c471b7012b298d38d179f1b487c413f51959cd4")
                    .build());

            player.openInventory(inventory);
        }

        public void openPermissionControlAddPermissionToAPlayerInventory(){
            final Inventory inventory = Bukkit.createInventory(null, 9*6, "§8» §bPermission §8┃ §7Add");

            for(int i = 0; i < 9; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            for(int i = 43; i < 53; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            inventory.setItem(53, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §cBack")
                    .spigot()
                    .setSkullTexture("816ea34a6a6ec5c051e6932f1c471b7012b298d38d179f1b487c413f51959cd4")
                    .build());

            inventory.setItem(4, new ItemBuilder(Material.PAPER).setDisplayName("§8» §bPermission Control").addLore("§7Add a permission to a player").build());

            for (Player players : Bukkit.getOnlinePlayers()) {
                inventory.addItem(new ItemBuilder(Material.SKULL_ITEM).setData(3).setOwner(players.getName()).setDisplayName("§7" + players.getName()).build());
            }

            inventory.setItem(49, new ItemBuilder(Material.EMERALD)
                    .setDisplayName("§8» §aDefine players via chat")
                    .build());

            
            player.openInventory(inventory);
        }

        public void openPermissionControlFunctionInventory(){
            final Inventory inventory = Bukkit.createInventory(null, 9*6, "§8» §bPermission Control");

            for(int i = 0; i < 9; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            for(int i = 43; i < 53; i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

            inventory.setItem(53, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§8» §cBack")
                    .spigot()
                    .setSkullTexture("816ea34a6a6ec5c051e6932f1c471b7012b298d38d179f1b487c413f51959cd4")
                    .build());

            inventory.setItem(48, new ItemBuilder(Material.DIAMOND)
                    .setDisplayName("§8» §aShow players")
                    .build());
            inventory.setItem(49, new ItemBuilder(Material.EMERALD)
                    .setDisplayName("§8» §aCreate a group")
                    .build());
            inventory.setItem(50, new ItemBuilder(Material.COAL)
                    .setDisplayName("§8» §aAdd the permission to a player")
                    .build());

            inventory.setItem(4, new ItemBuilder(Material.PAPER).setDisplayName("§8» §bPermission Control").build());

            for(Group group : ServerManager.getInstance().getPermissionHandler().getPlayerGroupHandler().getGroups())
                inventory.addItem(new ItemBuilder(Material.LEATHER_CHESTPLATE)
                        .setDisplayName("§7" + group.getName())
                        .addLore(" ", "§7default§8: §e" + group.isDefaultGroup(), "")
                        .addLore(group.getPermissions())
                        .build());

            player.openInventory(inventory);
        }

    }
}
