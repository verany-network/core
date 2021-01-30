package net.verany.api.particle;

import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_16_R3.ParticleType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ParticleManager {

    private final ParticleType particle;
    private final boolean longDistance;
    private final Location location;
    private final float offsetX;
    private final float offsetY;
    private final float offsetZ;
    private final float speed;
    private final int amount;

    public ParticleManager(ParticleType particle, Location location, boolean longdistance, float offsetx, float offsety, float offsetz, float speed, int amount) {
        this.particle = particle;
        this.location = location;
        this.longDistance = longdistance;
        this.offsetX = offsetx;
        this.offsetY = offsety;
        this.offsetZ = offsetz;
        this.speed = speed;
        this.amount = amount;
    }

    public void sendPlayer(Player player) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, this.longDistance, (float) this.location.getX(), (float) this.location.getY(), (float) this.location.getZ(), this.offsetX, this.offsetY, this.offsetZ, this.speed, this.amount);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
