package net.baumarkt.servermanager.utils.funciton.functions;

/*
 * Created on 15.08.2020 at 13:39
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.utils.ItemBuilder;
import net.baumarkt.servermanager.utils.funciton.Function;
import net.baumarkt.servermanager.utils.player.ServerPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PluginControlFunction implements Function {


    @Override
    public void onInventoryClick(Player player) {
        new ServerPlayer(player).getInventoryHolder().openPluginControlFunctionInventory();
    }

    @Override
    public String getName() {
        return "Plugin Control";
    }

    @Override
    public String getPermission() {
        return ServerManager.getInstance().getConfigHandler().getContent("permission.function.pluginControl");
    }

    @Override
    public Boolean isAvailable() {
        return ServerManager.getInstance().getConfigHandler().getJsonObject("functions").get("pluginControl").getAsBoolean();
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemBuilder(Material.SKULL_ITEM).setData(3).spigot().setSkullTexture("f37cae5c51eb1558ea828f58e0dff8e6b7b0b1a183d737eecf714661761").build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{"§7Activate or deactivate plugins"};
    }
}
