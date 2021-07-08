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
        for (Location location : data.locations()) {
            ParticleManager particleManager = new ParticleManager(data.particle(), location, true, data.offsetX(), data.offsetY(), data.offsetZ(), data.speed(), data.amount());
            for (Player player : data.players())
                particleManager.sendPlayer(player);
        }
    }
}
