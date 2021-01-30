package net.verany.api.npc.reflection;

import net.minecraft.server.v1_16_R3.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReflectionUtils {

    public Field modifiers = this.getField(Field.class, "modifiers");

    public Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            if (Modifier.isFinal(field.getModifiers())) {
                this.modifiers.set(field, field.getModifiers() & -17);
            }

            return field;
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var4) {
            Logger.getLogger(ReflectionUtils.class.getName()).log(Level.SEVERE, (String) null, var4);
            return null;
        }
    }

    public void setField(Object object, String name, Object to) throws Exception {
        Field field = object.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(object, to);
        field.setAccessible(false);
    }

    public String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException var3) {
            Logger.getLogger(ReflectionUtils.class.getName()).log(Level.SEVERE, (String) null, var3);
            return null;
        }
    }

    public Class<?> getCraftBukkitClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + name);
        } catch (ClassNotFoundException var3) {
            Logger.getLogger(ReflectionUtils.class.getName()).log(Level.SEVERE, (String) null, var3);
            return null;
        }
    }

    public long toFixedPoint(double value) {
        return MathHelper.d(value * 4096.0D);
    }

    public int toAngle(float value) {
        return MathHelper.d(value * 256.0F / 360.0F);
    }

    public void sendPacket(Player player, Object packet) {
        try {
            Object playerHandle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = playerHandle.getClass().getField("playerConnection").get(playerHandle);
            playerConnection.getClass().getMethod("sendPacket", this.getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | NoSuchMethodException var5) {
            Logger.getLogger(ReflectionUtils.class.getName()).log(Level.SEVERE, (String) null, var5);
        }

    }

    public void sendPacket(Object packet) {
        Bukkit.getOnlinePlayers().forEach((player) -> this.sendPacket(player, packet));
    }
}
