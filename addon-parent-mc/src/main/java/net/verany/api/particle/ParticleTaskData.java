package net.verany.api.particle;

import net.minecraft.server.v1_16_R3.ParticleType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class ParticleTaskData {

    private final ParticleType particle;
    private final int time;
    private final int amount;
    private final float offsetX;
    private final float offsetY;
    private final float offsetZ;
    private final float speed;
    private final List<Player> players;
    private final List<Location> locations;

    public ParticleTaskData(ParticleType particle, int time, int amount, float offsetX, float offsetY, float offsetZ, float speed, List<Player> players, List<Location> locations) {
        this.particle = particle;
        this.time = time;
        this.amount = amount;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.players = players;
        this.locations = locations;
    }

    public ParticleType getParticle() {
        return particle;
    }

    public int getTime() {
        return time;
    }

    public int getAmount() {
        return amount;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public float getOffsetZ() {
        return offsetZ;
    }

    public float getSpeed() {
        return speed;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Location> getLocations() {
        return locations;
    }
}
