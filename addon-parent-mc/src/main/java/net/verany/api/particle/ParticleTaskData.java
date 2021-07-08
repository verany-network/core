package net.verany.api.particle;

import net.minecraft.core.particles.ParticleType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public record ParticleTaskData(ParticleType particle, int time, int amount, float offsetX,
                               float offsetY, float offsetZ, float speed,
                               List<Player> players,
                               List<Location> locations) {

}
