package net.baumarkt.servermanager.utils.funciton;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Function {

    void onInventoryClick(final Player player);

    String getName();
    String getPermission();
    Boolean isAvailable();
    ItemStack getItemStack();
    String[] getDescription();

}
