package net.verany.api.npc;

import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.verany.api.npc.animation.NPCAnimation;
import net.verany.api.npc.datawatcher.DataWatcherGenerator;
import net.verany.api.skin.AbstractSkinData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface INPC {

    void spawn();

    void despawn();

    void setDataWatcher(DataWatcherGenerator watcher);

    void setGameProfile(AbstractSkinData skinData);

    void setGameProfile(String signature, String texture);

    void removeFromTablist();

    void teleport(Location location);

    void setAnimation(NPCAnimation animation);

    void setEquipment(EnumItemSlot itemSlot, Material material);

    void setHeadRotation(float rotation);

    void move(Location location);

    UUID getUniqueId();

    String getName();

    String getDisplayName();

    int getId();

    Player getPlayer();

    Location getLocation();

    void lookAtPlayer(LivingEntity entity);

}
