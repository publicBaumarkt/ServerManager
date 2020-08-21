package net.baumarkt.servermanager.utils.funciton.functions;

/*
 * Created on 11.08.2020 at 11:07
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.utils.ItemBuilder;
import net.baumarkt.servermanager.utils.funciton.Function;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

public class WorldControlFunction implements Function {

    @Override
    public void onInventoryClick(Player player) {
        final Inventory inventory = Bukkit.createInventory(null, 9*6, "§8» §bWorld Control");
        int count = 9;

        for (World world: Bukkit.getWorlds()) {
            inventory.setItem(count, new ItemBuilder(Material.SKULL_ITEM)
                    .setData(3)
                    .setDisplayName("§e" + world.getName())
                    .addLore("§7Click for more options")
                    .spigot()
                    .setSkullTexture("438cf3f8e54afc3b3f91d20a49f324dca1486007fe545399055524c17941f4dc")
                    .build());
            count++;
        }

        for(int i = 0; i < 9; i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

        for(int i = 42; i < 52; i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setNoName().build());

        inventory.setItem(52, new ItemBuilder(Material.SKULL_ITEM)
                .setData(3)
                .setDisplayName("§8» §9Create world")
                .spigot()
                .setSkullTexture("3edd20be93520949e6ce789dc4f43efaeb28c717ee6bfcbbe02780142f716")
                .build());

        inventory.setItem(53, new ItemBuilder(Material.SKULL_ITEM)
                .setData(3)
                .setDisplayName("§8» §cBack")
                .spigot()
                .setSkullTexture("816ea34a6a6ec5c051e6932f1c471b7012b298d38d179f1b487c413f51959cd4")
                .build());


        inventory.setItem(4, new ItemBuilder(Material.PAPER).setDisplayName("§8» §bWorld Control").build());

        player.openInventory(inventory);
    }

    @Override
    public String getName() {
        return "World Control";
    }

    @Override
    public String getPermission() {
        return ServerManager.getInstance().getConfigHandler().getContent("permission.function.worldControl");
    }

    @Override
    public Boolean isAvailable() {
        return ServerManager.getInstance().getConfigHandler().getJsonObject("functions").get("worldControl").getAsBoolean();
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemBuilder(Material.SKULL_ITEM).setData(3).spigot().setSkullTexture("438cf3f8e54afc3b3f91d20a49f324dca1486007fe545399055524c17941f4dc").build();
    }

    @Override
    public String[] getDescription() {
        return new String[] {"§7Create, change or delete worlds."};
    }
}
