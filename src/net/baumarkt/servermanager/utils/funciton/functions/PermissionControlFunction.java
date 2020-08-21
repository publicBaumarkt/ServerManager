package net.baumarkt.servermanager.utils.funciton.functions;

/*
 * Created on 16.08.2020 at 09:57
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.utils.funciton.Function;
import net.baumarkt.servermanager.utils.player.ServerPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PermissionControlFunction implements Function {

    @Override
    public void onInventoryClick(Player player) {
        new ServerPlayer(player).getInventoryHolder().openPermissionControlFunctionInventory();
    }

    @Override
    public String getName() {
        return "Permission Control";
    }

    @Override
    public String getPermission() {
        return ServerManager.getInstance().getConfigHandler().getContent("permission.function.permissionControl");
    }

    @Override
    public Boolean isAvailable() {
        return ServerManager.getInstance().getConfigHandler().getJsonObject("functions").get("permissionControl").getAsBoolean();
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(Material.IRON_DOOR);
    }

    @Override
    public String[] getDescription() {
        return new String[]{"§7Set up the permissions for groups."};
    }
}
