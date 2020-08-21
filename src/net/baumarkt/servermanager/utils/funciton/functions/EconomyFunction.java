package net.baumarkt.servermanager.utils.funciton.functions;

/*
 * Created on 21.08.2020 at 13:35
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.utils.funciton.Function;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EconomyFunction implements Function {


    @Override
    public void onInventoryClick(Player player) {

    }

    @Override
    public String getName() {
        return "Economy";
    }

    @Override
    public String getPermission() {
        return ServerManager.getInstance().getConfigHandler().getContent("permission.function.economy");
    }

    @Override
    public Boolean isAvailable() {
        return ServerManager.getInstance().getConfigHandler().getJsonObject("functions").get("economy").getAsBoolean();
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.GOLD_INGOT);
    }

    @Override
    public String[] getDescription() {
        return new String[]{"§7Manage the economy system."};
    }
}
