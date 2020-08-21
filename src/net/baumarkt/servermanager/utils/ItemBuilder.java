package net.baumarkt.servermanager.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class ItemBuilder {

    private final Spigot spigot;

    private final ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(ItemStack itemStack) {
        this.spigot = new Spigot(this);

        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this.spigot = new Spigot(this);

        this.itemStack = ((material.equals(Material.SKULL_ITEM)) ? new ItemStack(material, 1, ((short) 3)) : new ItemStack(material));
        this.itemMeta = this.itemStack.getItemMeta();
    }

    @Override
    public boolean equals(Object obj) {
        ItemBuilder item = ((obj instanceof ItemBuilder) ? ((ItemBuilder) obj) : new ItemBuilder((obj instanceof ItemStack) ? ((ItemStack) obj) : new ItemStack(Material.AIR)));
        return item.getType().equals(this.getType()) && item.getData() == this.getData() && item.getDisplayName().equals(this.getDisplayName());
    }

    public Material getType() {
        return this.itemStack.getType();
    }

    public ItemBuilder setType(Material material) {
        this.itemStack.setType(material);

        return this;
    }

    public int getAmount() {
        return this.itemStack.getAmount();
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);

        return this;
    }

    public int getData() {
        return this.itemStack.getDurability();
    }

    public ItemBuilder setData(int data) {
        this.itemStack.setDurability((short) data);

        return this;
    }

    public boolean hasDisplayName() {
        return this.itemMeta.hasDisplayName();
    }

    public String getDisplayName() {
        return this.itemMeta.getDisplayName();
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.itemMeta.setDisplayName(displayName);
        this.itemStack.setItemMeta(this.itemMeta);

        return this;
    }

    public ItemBuilder clearLore() {
        this.itemMeta.setLore(new LinkedList<String>());
        this.itemStack.setItemMeta(this.itemMeta);

        return this;
    }

    public ItemBuilder addLore(String... lore) {
        List<String> list = ((this.itemMeta.hasLore()) ? this.itemMeta.getLore() : new LinkedList<String>());
        list.addAll(Arrays.asList(lore));

        this.itemMeta.setLore(list);
        this.itemStack.setItemMeta(this.itemMeta);

        return this;
    }

    public ItemBuilder addLore(List<String> lore) {
        List<String> list = ((this.itemMeta.hasLore()) ? this.itemMeta.getLore() : new LinkedList<String>());
        list.addAll(lore);

        this.itemMeta.setLore(list);
        this.itemStack.setItemMeta(this.itemMeta);

        return this;
    }

    public ItemBuilder removeLore(String... lore) {
        List<String> list = ((this.itemMeta.hasLore()) ? this.itemMeta.getLore() : new LinkedList<String>());
        list.removeAll(Arrays.asList(lore));

        this.itemMeta.setLore(list);
        this.itemStack.setItemMeta(this.itemMeta);

        return this;
    }

    public boolean hasLore() {
        return this.itemMeta.hasLore();
    }

    public List<String> getLore() {
        return ((this.itemMeta.getLore() == null) ? Collections.emptyList() : this.itemMeta.getLore());
    }

    public ItemBuilder setLore(String... lore) {
        this.itemMeta.setLore(Arrays.asList(lore));
        this.itemStack.setItemMeta(this.itemMeta);

        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        this.itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction);
        this.itemStack.setItemMeta(this.itemMeta);

        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        this.itemMeta.removeEnchant(enchantment);
        this.itemStack.setItemMeta(this.itemMeta);

        return this;
    }

    public boolean hasEnchants() {
        return this.itemMeta.hasEnchants();
    }

    public boolean hasEnchant(Enchantment enchantment) {
        return this.itemMeta.hasEnchant(enchantment);
    }

    public int getEnchantLevel(Enchantment enchantment) {
        return this.itemMeta.getEnchantLevel(enchantment);
    }

    public Map<Enchantment, Integer> getEnchants() {
        return this.itemMeta.getEnchants();
    }

    public void addItemFlags(ItemFlag... itemFlags) {
        this.itemMeta.addItemFlags(itemFlags);
        this.itemStack.setItemMeta(this.itemMeta);
    }

    public ItemBuilder removeItemFlags(ItemFlag... itemFlags) {
        this.itemMeta.removeItemFlags(itemFlags);
        this.itemStack.setItemMeta(this.itemMeta);

        return this;
    }

    public boolean hasItemFlag(ItemFlag itemFlag) {
        return this.itemMeta.hasItemFlag(itemFlag);
    }

    public Set<ItemFlag> getItemFlags() {
        return this.itemMeta.getItemFlags();
    }

    public boolean isUnbreakable() {
        return this.itemMeta.spigot().isUnbreakable();
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.itemMeta.spigot().setUnbreakable(unbreakable);
        this.itemStack.setItemMeta(this.itemMeta);

        return this;
    }

    private boolean isSkull() {
        return this.itemStack.getType().equals(Material.SKULL_ITEM);
    }

    public String getOwner() {
        return ((this.isSkull()) ? ((SkullMeta) this.itemMeta).getOwner() : null);
    }

    public ItemBuilder setOwner(String owner) {
        if(this.isSkull()) {
            SkullMeta metadata = ((SkullMeta) this.itemMeta);
            metadata.setOwner(owner);
            this.itemStack.setItemMeta(metadata);
        }

        return this;
    }

    public ItemBuilder setNoName(){
        setDisplayName(" ");
        return this;
    }

    public ItemStack build() {
        return this.itemStack.clone();
    }

    public Spigot spigot() {
        return this.spigot;
    }

    public static class Spigot {

        private final ItemBuilder builder;

        public Spigot(ItemBuilder builder) {
            this.builder = builder;
        }

        public ItemBuilder setSkullTexture(UUID uuid) {
            if(this.builder.isSkull()) {
                SkullMeta metadata = ((SkullMeta) this.builder.itemMeta);

                try {
                    Field field = metadata.getClass().getDeclaredField("profile");
                    field.setAccessible(true);

                    field.set(metadata, GameProfileBuilder.fetch(uuid));
                    field.setAccessible(false);
                } catch (IllegalAccessException | NoSuchFieldException | IOException e) {
                    e.printStackTrace();
                }
                this.builder.itemStack.setItemMeta(metadata);
                this.builder.itemStack.setItemMeta(this.builder.itemMeta);
            }
            return this.builder;
        }

        private  final String JSON_SKIN = "{\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"https://textures.minecraft.net/texture/%s\"}}}";

        public ItemBuilder setSkullTexture(String texture) {
            if(this.builder.isSkull()) {
                SkullMeta metadata = ((SkullMeta) this.builder.itemMeta);

                GameProfile profile = new GameProfile(UUID.randomUUID(), "");
                profile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString(String.format(JSON_SKIN, texture))));

                try {
                    Field field = metadata.getClass().getDeclaredField("profile");
                    field.setAccessible(true);
                    field.set(metadata, profile);
                    field.setAccessible(false);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                this.builder.itemStack.setItemMeta(metadata);
                this.builder.itemStack.setItemMeta(this.builder.itemMeta);
            }
            return this.builder;
        }

        public ItemBuilder removeNBT(String key) {
            net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this.builder.itemStack);
            NBTTagCompound compound = ((itemStack.hasTag()) ? itemStack.getTag() : new NBTTagCompound());
            compound.remove(key);
            itemStack.setTag(compound);

            this.builder.itemMeta = CraftItemStack.getItemMeta(itemStack);
            this.builder.itemStack.setItemMeta(this.builder.itemMeta);
            return this.builder;
        }

        public boolean hasNBT(String key) {
            net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this.builder.itemStack);
            NBTTagCompound compound = ((itemStack.hasTag()) ? itemStack.getTag() : new NBTTagCompound());
            return compound.hasKey(key);
        }

        public String getNBTString(String key) {
            net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this.builder.itemStack);
            NBTTagCompound compound = ((itemStack.hasTag()) ? itemStack.getTag() : new NBTTagCompound());
            return compound.getString(key);
        }

        public boolean getNBTBoolean(String key) {
            net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this.builder.itemStack);
            NBTTagCompound compound = ((itemStack.hasTag()) ? itemStack.getTag() : new NBTTagCompound());
            return compound.getBoolean(key);
        }

        public int getNBTInt(String key) {
            net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this.builder.itemStack);
            NBTTagCompound compound = ((itemStack.hasTag()) ? itemStack.getTag() : new NBTTagCompound());
            return compound.getInt(key);
        }

        public double getNBTDouble(String key) {
            net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this.builder.itemStack);
            NBTTagCompound compound = ((itemStack.hasTag()) ? itemStack.getTag() : new NBTTagCompound());
            return compound.getDouble(key);
        }

        public double getNBTLong(String key) {
            net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this.builder.itemStack);
            NBTTagCompound compound = ((itemStack.hasTag()) ? itemStack.getTag() : new NBTTagCompound());
            return compound.getLong(key);
        }

        public float getNBTFloat(String key) {
            net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this.builder.itemStack);
            NBTTagCompound compound = ((itemStack.hasTag()) ? itemStack.getTag() : new NBTTagCompound());
            return compound.getFloat(key);
        }

        public ItemBuilder setNBT(String key, NBTBase base) {
            net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(this.builder.itemStack);
            NBTTagCompound compound = ((itemStack.hasTag()) ? itemStack.getTag() : new NBTTagCompound());
            compound.set(key, base);
            itemStack.setTag(compound);

            this.builder.itemMeta = CraftItemStack.getItemMeta(itemStack);
            this.builder.itemStack.setItemMeta(this.builder.itemMeta);
            return this.builder;
        }
    }
}
