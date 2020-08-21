package net.baumarkt.servermanager.utils.funciton.functions;

/*
 * Created on 14.08.2020 at 21:13
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

public class ChatControlFunction implements Function {

    @Override
    public void onInventoryClick(Player player) {
        new ServerPlayer(player).getInventoryHolder().openChatControlFunctionInventory();
    }

    @Override
    public String getName() {
        return "ChatColor Control";
    }

    @Override
    public String getPermission() {
        return ServerManager.getInstance().getConfigHandler().getContent("permission.function.chatControl");
    }

    @Override
    public Boolean isAvailable() {
        return ServerManager.getInstance().getConfigHandler().getJsonObject("functions").get("chatControl").getAsBoolean();
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemBuilder(Material.PAPER).build();
    }

    @Override
    public String[] getDescription() {
        return new String[] {"§7Change the ChatColor."};
    }
}
