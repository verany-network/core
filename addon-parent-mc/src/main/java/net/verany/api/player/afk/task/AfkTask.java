package net.verany.api.player.afk.task;

import net.verany.api.Verany;
import net.verany.api.config.IngameConfig;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.afk.IAFKObject;
import net.verany.api.task.AbstractTask;
import org.bukkit.Bukkit;

public class AfkTask extends AbstractTask {

    private final VeranyProject project;

    public AfkTask(long waitTime, VeranyProject project) {
        super(waitTime);
        this.project = project;
    }

    @Override
    public void run() {
        if (!IngameConfig.AFK.getValue()) return;
        Verany.getOnlinePlayers().forEach(iPlayerInfo -> {
            IAFKObject object = iPlayerInfo.getAfkObject();
            if (object.isAfk()) {
                if (!object.shouldAfk()) {
                    object.setAfk(false);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(project, object::removeHologram);
                    iPlayerInfo.sendKey(Verany.getPrefix("AFK", iPlayerInfo.getPrefixPattern()), "core.afk.false");
                    return;
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(project, object::updateHologram);
            } else {
                if (object.shouldAfk()) {
                    object.setAfk(true);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(project, object::spawnHologram);
                    iPlayerInfo.sendKey(Verany.getPrefix("AFK", iPlayerInfo.getPrefixPattern()), "core.afk.true");
                }
            }
        });
    }
}
