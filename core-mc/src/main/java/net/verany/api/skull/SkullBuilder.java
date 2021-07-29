package net.verany.api.skull;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.verany.api.skin.AbstractSkinData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SkullBuilder implements ISkullBuilder {

    private Class<?> skullMetaClass;

    private final ItemStack skull;
    private final SkullMeta skullMeta;
    private Class<?> blockPositionClass;
    private Class<?> tileEntityClass;
    private final List<String> lore = new ArrayList<>();

    private final String value;

    public SkullBuilder(AbstractSkinData skinData) {
        this.value = skinData.getValue();
        this.skull = new ItemStack(Material.PLAYER_HEAD, 1);
        this.skullMeta = (SkullMeta) skull.getItemMeta();
        try {
            skullMetaClass = Class.forName("org.bukkit.craftbukkit.v1_17_R1.inventory.CraftMetaSkull");
            tileEntityClass = Class.forName("net.minecraft.world.level.block.entity.TileEntitySkull");
            blockPositionClass = Class.forName("net.minecraft.core.BlockPosition");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public SkullBuilder(String value) {
        this.value = value;
        this.skull = new ItemStack(Material.PLAYER_HEAD, 1);
        this.skullMeta = (SkullMeta) skull.getItemMeta();
        try {
            skullMetaClass = Class.forName("org.bukkit.craftbukkit.v1_17_R1.inventory.CraftMetaSkull");
            tileEntityClass = Class.forName("net.minecraft.world.level.block.entity.TileEntitySkull");
            blockPositionClass = Class.forName("net.minecraft.core.BlockPosition");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public SkullBuilder setDisplayName(String name) {
        this.skullMeta.setDisplayName(name);
        return this;
    }

    public SkullBuilder setAmount(int amount) {
        this.skull.setAmount(amount);
        return this;
    }

    public SkullBuilder addLoreLine(String line) {
        lore.add(line);
        return this;
    }

    public SkullBuilder addLoreArray(String... lines) {
        Collections.addAll(lore, lines);
        return this;
    }

    public SkullBuilder addLoreAll(List<String> lines) {
        lore.addAll(lines);
        return this;
    }

    public ItemStack build() {
        if (!lore.isEmpty()) {
            this.skullMeta.setLore(lore);
        }
        try {
            Field profileField = skullMetaClass.getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(this.skullMeta, this.getProfile(this.value));
        } catch (Exception var9) {
            var9.printStackTrace();
        }
        this.skull.setItemMeta(this.skullMeta);
        return this.skull;
    }


    public boolean setBlock(Block block) {
        if (block.getType() != Material.PLAYER_HEAD) {
            block.setType(Material.PLAYER_HEAD);
        }

        try {
            Object nmsWorld = block.getWorld().getClass().getMethod("getHandle").invoke(block.getWorld());
            Object tileEntity = null;
            Method getTileEntity = nmsWorld.getClass().getMethod("getTileEntity", blockPositionClass);
            tileEntity = tileEntityClass.cast(getTileEntity.invoke(nmsWorld, this.getBlockPositionFor(block.getX(), block.getY(), block.getZ())));
            tileEntityClass.getMethod("setGameProfile", GameProfile.class).invoke(tileEntity, this.getProfile(value));
            return true;
        } catch (Exception var7) {
            var7.printStackTrace();
            return false;
        }
    }

    private Object getBlockPositionFor(int x, int y, int z) {
        Object blockPosition = null;

        try {
            Constructor<?> cons = blockPositionClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
            blockPosition = cons.newInstance(x, y, z);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return blockPosition;
    }


    private GameProfile getProfile(String value) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        Property property = new Property("textures", value);
        profile.getProperties().put("textures", property);
        return profile;
    }

    @Override
    public ISkullBuilder clone() {
        try {
            return (ISkullBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}
