package net.verany.api.task;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class RunnableSeries {

    private final List<BukkitRunnable> runnables;

    public RunnableSeries(BukkitRunnable... runnables) {
        this.runnables = Arrays.asList(runnables);
    }

    public List<BukkitRunnable> getRunnables() {
        return runnables;
    }

    public void cancelAll() {
        for (BukkitRunnable runnable : runnables) {
            runnable.cancel();
        }
    }
}
