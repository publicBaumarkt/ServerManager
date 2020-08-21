package net.baumarkt.servermanager.utils.funciton.functions;

/*
 * Created on 15.08.2020 at 12:07
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

public class PlayerControlFunction implements Function {

    @Override
    public void onInventoryClick(Player player) {
        new ServerPlayer(player).getInventoryHolder().openPlayerControlFunctionInventory();
    }

    @Override
    public String getName() {
        return "Player Control";
    }

    @Override
    public String getPermission() {
        return ServerManager.getInstance().getConfigHandler().getContent("permission.function.playerControl");
    }

    @Override
    public Boolean isAvailable() {
        return ServerManager.getInstance().getConfigHandler().getJsonObject("functions").get("playerControl").getAsBoolean();
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemBuilder(Material.SKULL_ITEM).setData(3).setOwner("Baumarkt").build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{"§7See information about players or edit players"};
    }
}
