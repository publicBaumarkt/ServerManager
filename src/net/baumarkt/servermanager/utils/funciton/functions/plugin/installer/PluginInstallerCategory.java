package net.baumarkt.servermanager.utils.funciton.functions.plugin.installer;

/*
 * Created on 15.08.2020 at 14:01
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum PluginInstallerCategory {

    UTILITY(new ItemBuilder(Material.BANNER).setDisplayName("§7UTILITY Plugins").build()),
    WORLD(new ItemBuilder(Material.SKULL_ITEM).setDisplayName("§7WORLD Plugins").setData(3).spigot().setSkullTexture("438cf3f8e54afc3b3f91d20a49f324dca1486007fe545399055524c17941f4dc").build()),
    FUN(new ItemBuilder(Material.CAKE).setDisplayName("§7FUN Plugins").build()),
    GAME(new ItemBuilder(Material.IRON_SWORD).setDisplayName("§7GAME Plugins").build());

    private final ItemStack itemStack;

    PluginInstallerCategory(final ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
