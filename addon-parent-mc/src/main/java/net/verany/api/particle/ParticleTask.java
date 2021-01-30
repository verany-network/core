package net.verany.api.particle;

import net.verany.api.Verany;
import net.verany.api.task.AbstractTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticleTask extends AbstractTask {

    private final ParticleTaskData data;

    public ParticleTask(ParticleTaskData data, int millis) {
        super(millis);
        this.data = data;
    }

    @Override
    public void run() {
        for (Location location : data.getLocations()) {
            ParticleManager particleManager = new ParticleManager(data.getParticle(), location, true, data.getOffsetX(), data.getOffsetY(), data.getOffsetZ(), data.getSpeed(), data.getAmount());
            for (Player player : data.getPlayers())
                particleManager.sendPlayer(player);
        }
    }
}
