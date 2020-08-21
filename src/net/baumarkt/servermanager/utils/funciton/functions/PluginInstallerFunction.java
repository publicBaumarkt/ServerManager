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

public class PluginInstallerFunction implements Function {

    @Override
    public void onInventoryClick(Player player) {
        new ServerPlayer(player).getInventoryHolder().openPluginInstallerFunctionInventory();
    }

    @Override
    public String getName() {
        return "Plugin Installer";
    }

    @Override
    public String getPermission() {
        return ServerManager.getInstance().getConfigHandler().getContent("permission.function.pluginInstaller");
    }

    @Override
    public Boolean isAvailable() {
        return ServerManager.getInstance().getConfigHandler().getJsonObject("functions").get("pluginInstaller").getAsBoolean();
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemBuilder(Material.SKULL_ITEM).setData(3).spigot().setSkullTexture("25ba9789087b012307dd47de38c6622d75d6bb21eb97016c49afe9d700419909").build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{"§7Install plguins"};
    }
}
