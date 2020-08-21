package net.baumarkt.servermanager.utils.funciton.functions;

/*
 * Created on 11.08.2020 at 11:07
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.utils.ItemBuilder;
import net.baumarkt.servermanager.utils.funciton.Function;
import net.baumarkt.servermanager.utils.player.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ServerControlFunction implements Function {

    @Override
    public void onInventoryClick(Player player) {
        new ServerPlayer(player).getInventoryHolder().openServerControlFunctionInventory();
    }

    @Override
    public String getName() {
        return "Server Control";
    }

    @Override
    public String getPermission() {
        return ServerManager.getInstance().getConfigHandler().getContent("permission.function.serverControl");
    }

    @Override
    public Boolean isAvailable() {
        return ServerManager.getInstance().getConfigHandler().getJsonObject("functions").get("serverControl").getAsBoolean();
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemBuilder(Material.SKULL_ITEM).setData(3).spigot().setSkullTexture("109cde1afc95a474d222554097ed6d391e7cc7ae1f202fdbfd2d6dbc98309370").build();
    }

    @Override
    public String[] getDescription() {
        return new String[] {"§7Change settings of the server and more."};
    }
}
