package net.verany.api.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerInteractManager;
import net.minecraft.server.level.WorldServer;

import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.ItemStack;
import net.verany.api.Verany;
import net.verany.api.npc.animation.NPCAnimation;
import net.verany.api.npc.datawatcher.DataWatcherGenerator;
import net.verany.api.npc.reflection.ReflectionUtils;
import net.verany.api.skin.AbstractSkinData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NPCObject extends ReflectionUtils implements INPC {

    public static final Map<Integer, NPCObject> NPCS = new HashMap();
    public static Field idField;
    private final Random random;
    private final Player players;
    private long interactTime;
    private CraftServer craftServer;
    private WorldServer worldServer;
    private String name;
    private String value;
    private String signature;
    private Location location;
    private GameProfile gameProfile;
    private EntityPlayer entityPlayer;
    private DataWatcher watcher;
    private boolean removeTablist;
    private boolean sleeping;
    private boolean spawned;
    private long xLoc;
    private long yLoc;
    private long zLoc;
    private int yawRot;
    private int pitchRot;
    private int headYaw;
    private int count;
    private int v;
    private boolean onGround;

    static {
        try {
            idField = PacketPlayInUseEntity.class.getDeclaredField("a");
            idField.setAccessible(true);
        } catch (SecurityException | NoSuchFieldException var1) {
            Logger.getLogger(NPCObject.class.getName()).log(Level.SEVERE, (String) null, var1);
        }

    }

    public NPCObject(CraftServer server, String name, Location location, boolean removeTablist, Player players) {
        this.random = new Random();
        this.interactTime = System.currentTimeMillis();
        this.removeTablist = false;
        this.sleeping = false;
        this.spawned = false;
        this.count = 0;
        this.v = 0;
        this.craftServer = server;
        this.name = name;
        this.location = location;
        this.xLoc = this.toFixedPoint(location.getX());
        this.yLoc = this.toFixedPoint(location.getY());
        this.zLoc = this.toFixedPoint(location.getZ());
        this.yawRot = this.toAngle(location.getYaw());
        this.pitchRot = this.toAngle(location.getPitch());
        this.removeTablist = removeTablist;
        this.players = players;
        this.gameProfile = new GameProfile(new UUID(this.random.nextLong(), 0L), ChatColor.translateAlternateColorCodes('&', this.name));
        this.worldServer = ((CraftWorld) location.getWorld()).getHandle();
        this.entityPlayer = new EntityPlayer(server.getServer(), this.worldServer, this.gameProfile);
        this.watcher = (new DataWatcherGenerator(entityPlayer.getDataWatcher())).setHeath(20.0F).setSkinParts(DataWatcherGenerator.SkinState.ALL).setMainHand(DataWatcherGenerator.MainHandState.LEFT).build();
        this.entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.onGround = this.entityPlayer.isOnGround();
        this.headYaw = this.toAngle(this.entityPlayer.getHeadRotation());
    }

    @Override
    public void spawn() {
        try {
            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, this.entityPlayer);
            this.sendPacket(this.players, packet);
            PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(this.entityPlayer);
            this.sendPacket(this.players, spawnPacket);
            this.setHeadRotation(this.entityPlayer.getHeadRotation());
            if (!NPCS.containsKey(this.entityPlayer.getId())) {
                System.out.println("NPC SPAWN ID:" + this.entityPlayer.getId() + " UUID: " + this.entityPlayer.getUniqueID().toString());
                NPCS.put(this.entityPlayer.getId(), this);
            }
            PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityPlayer.getId(), entityPlayer.getDataWatcher(), true);
            sendPacket(players, entityMetadata);

            this.spawned = true;
            if (this.removeTablist) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var4) {
                    Logger.getLogger(NPCObject.class.getName()).log(Level.SEVERE, null, var4);
                }

                this.removeFromTablist();
            }

            Verany.NPCS.add(this);
        } catch (Exception var5) {
            Logger.getLogger(NPCObject.class.getName()).log(Level.SEVERE, null, var5);
        }

    }

    @Override
    public void despawn() {
        try {
            this.removeFromTablist();
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.entityPlayer.getId());
            this.sendPacket(this.players, packet);
            if (NPCS.containsKey(this.entityPlayer.getId())) {
                NPCS.remove(this.entityPlayer.getId());
            }

            this.spawned = false;
        } catch (IllegalArgumentException | SecurityException var2) {
            Logger.getLogger(NPCObject.class.getName()).log(Level.SEVERE, (String) null, var2);
        }
        Verany.NPCS.remove(this);
    }

    @Override
    public void setDataWatcher(DataWatcherGenerator dataWatcher) {
        this.watcher = dataWatcher.build();
    }

    @Override
    public void setGameProfile(AbstractSkinData skinData) {
        this.setGameProfile(skinData.getValue(), skinData.getSignature());
    }

    @Override
    public void setGameProfile(String value, String signature) {
        this.gameProfile.getProperties().clear();
        this.gameProfile.getProperties().put("textures", new Property("textures", value, signature));
        if (this.spawned) {
            this.despawn();
            this.spawn();
        }
    }

    @Override
    public void removeFromTablist() {
        try {
            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.b, this.entityPlayer);
            this.sendPacket(this.players, packet);
        } catch (SecurityException | IllegalArgumentException var2) {
            Logger.getLogger(NPCObject.class.getName()).log(Level.SEVERE, (String) null, var2);
        }
    }

    @Override
    public void teleport(Location location) {
        try {
            this.location = location;
            this.xLoc = this.toFixedPoint(location.getX());
            this.yLoc = this.toFixedPoint(location.getY());
            this.zLoc = this.toFixedPoint(location.getZ());
            this.yawRot = this.toAngle(location.getYaw());
            this.pitchRot = this.toAngle(location.getPitch());
            this.entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(this.entityPlayer);
            this.sendPacket(this.players, packet);
        } catch (SecurityException | IllegalArgumentException var3) {
            Logger.getLogger(NPCObject.class.getName()).log(Level.SEVERE, (String) null, var3);
        }

    }

    @Override
    public void setAnimation(NPCAnimation animation) {
        try {
            PacketPlayOutAnimation packet = new PacketPlayOutAnimation(this.entityPlayer, animation.getId());
            this.sendPacket(this.players, packet);
        } catch (IllegalArgumentException | SecurityException var3) {
            Logger.getLogger(NPCObject.class.getName()).log(Level.SEVERE, (String) null, var3);
        }

    }

    @Override
    public void setEquipment(EnumItemSlot slot, Material material) {
        try {
            ItemStack itemStack = material != Material.AIR && material != null ? CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(material)) : CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.AIR));
            List<Pair<EnumItemSlot, ItemStack>> pairs = new ArrayList<>();
            pairs.add(new Pair<>(slot, itemStack));
            PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(this.entityPlayer.getId(), pairs);
            this.sendPacket(this.players, packet);
        } catch (IllegalArgumentException | SecurityException var5) {
            Logger.getLogger(NPCObject.class.getName()).log(Level.SEVERE, null, var5);
        }

    }

    @Override
    public void setHeadRotation(float yaw) {
        try {
            PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation(this.entityPlayer, (byte) ((int) (yaw * 256.0F / 360.0F)));
            this.sendPacket(this.players, packet);
        } catch (IllegalArgumentException | SecurityException var3) {
            Logger.getLogger(NPCObject.class.getName()).log(Level.SEVERE, (String) null, var3);
        }

    }

    public void sleep(boolean sleep) {
        try {
            this.sleeping = sleep;
            if (sleep) {
                System.out.println("SLEEP");
                BlockPosition position = new BlockPosition(location.clone().getX(), location.clone().getY(), location.clone().getZ());
                entityPlayer.sleep(position);
                this.teleport(this.location);
                return;
            }

            System.out.println("SLEEP NOT");
            this.setAnimation(NPCAnimation.LEAVE_BED);
            this.teleport(this.location);
        } catch (IllegalArgumentException | SecurityException var5) {
            Logger.getLogger(NPCObject.class.getName()).log(Level.SEVERE, (String) null, var5);
        }

    }

    @Override
    public void move(Location moveTo) {
        ++this.v;
        long x = this.toFixedPoint(moveTo.getX());
        long y = this.toFixedPoint(moveTo.getY());
        long z = this.toFixedPoint(moveTo.getZ());
        int yaw = this.toAngle(moveTo.getYaw());
        int pitch = this.toAngle(moveTo.getPitch());
        short newX = (short) (x - this.xLoc);
        short newY = (short) (y - this.yLoc);
        short newZ = (short) (z - this.zLoc);
        Object packet = null;
        boolean positionChange = newX * newX + newY * newY + newZ * newZ >= 128L || this.count % 60 == 0;
        boolean rotationChange = Math.abs(yaw - this.yawRot) >= 1 || Math.abs(pitch - this.pitchRot) >= 1;
        if (positionChange) {
            this.xLoc = x;
            this.yLoc = y;
            this.zLoc = z;
        }

        if (rotationChange) {
            this.yawRot = yaw;
            this.pitchRot = pitch;
        }

        if (this.count > 0) {
            if (newX >= -32768L && newX < 32768L && newY >= -32768L && newY < 32768L && newZ >= -32768L && newZ < 32768L && this.v <= 400 && this.onGround == this.entityPlayer.isOnGround()) {
                if (positionChange && rotationChange) {
                    packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(this.entityPlayer.getId(), newX, newY, newZ, (byte) yaw, (byte) pitch, this.entityPlayer.isOnGround());
                } else if (positionChange) {
                    packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(this.entityPlayer.getId(), newX, newY, newZ, this.entityPlayer.isOnGround());
                } else if (rotationChange) {
                    packet = new PacketPlayOutEntity.PacketPlayOutEntityLook(this.entityPlayer.getId(), (byte) yaw, (byte) pitch, this.entityPlayer.isOnGround());
                }
            } else {
                this.onGround = this.entityPlayer.isOnGround();
                this.v = 0;
                packet = new PacketPlayOutEntityTeleport(this.entityPlayer);
            }
        }

        this.sendPacket(this.players, packet);
        ++this.count;
        this.sendPacket(this.players, new PacketPlayOutEntityHeadRotation(this.entityPlayer, (byte) yaw));
        this.location = moveTo;
    }

    public void lookAtPlayer(LivingEntity entity) {
        double diffX = entity.getLocation().getX() - location.getX();
        double diffY = (entity.getLocation().getY() + entity.getEyeHeight() * 0.9D) - (this.location.getY() + 1.6F);
        double diffZ = entity.getLocation().getZ() - location.getZ();
        double hypoXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180D / Math.PI) - 90F;
        float pitch = (float) -(Math.atan2(diffY, hypoXZ) * 180D / Math.PI);
        look(yaw, pitch);
    }

    private void look(float yaw, float pitch) {
        setHeadRotation(yaw);
        sendPacket(players, new PacketPlayOutEntity.PacketPlayOutEntityLook(this.getId(), (byte) toAngle(yaw), (byte) toAngle(pitch), false));

        location.setYaw(yaw);
        location.setPitch(pitch);
    }

    @Override
    public UUID getUniqueId() {
        return this.entityPlayer.getUniqueID();
    }

    @Override
    public String getName() {
        return this.entityPlayer.getName();
    }

    @Override
    public String getDisplayName() {
        return this.entityPlayer.getCustomName().toString();
    }

    public void setInteractTime(long interactTime) {
        this.interactTime = interactTime;
    }

    public long getInteractTime() {
        return this.interactTime;
    }

    public int getId() {
        return entityPlayer.getId();
    }

    @Override
    public Player getPlayer() {
        return players;
    }

    @Override
    public Location getLocation() {
        return location;
    }

}
