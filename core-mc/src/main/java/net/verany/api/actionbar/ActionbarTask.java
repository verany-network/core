package net.verany.api.actionbar;

import net.verany.api.Verany;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.plugin.IVeranyPlugin;
import net.verany.api.task.AbstractTask;
import org.bukkit.entity.Player;

public class ActionbarTask extends AbstractTask {

    private final IVeranyPlugin project;

    public ActionbarTask(long waitTime, IVeranyPlugin project) {
        super(waitTime);
        this.project = project;
    }

    @Override
    public void run() {
        for (IPlayerInfo gamePlayer : Verany.getOnlinePlayers()) {
            Player player = gamePlayer.getPlayer();
            if (player == null) continue;
            long time = gamePlayer.getCurrentActionbarTime();
            if (player.hasMetadata("actionbar_set")) {
                AbstractActionbar setData = (AbstractActionbar) player.getMetadata("actionbar_set").get(0).value();
                gamePlayer.sendInfoActionbar(setData);
                if (System.currentTimeMillis() > time)
                    project.removeMetadata(gamePlayer.getPlayer(), "actionbar_set");
                continue;
            }
            if (!gamePlayer.getActionbarQueue().isEmpty()) {
                AbstractActionbar data = gamePlayer.getActionbarQueue().get(0);
                if (System.currentTimeMillis() > time) {
                    gamePlayer.getActionbarQueue().remove(0);
                    if (gamePlayer.getLastActionbarTime() != -1) {
                        gamePlayer.setCurrentActionbarTime(gamePlayer.getLastActionbarTime());
                        gamePlayer.setLastActionbarTime(-1);
                    }
                    if (!gamePlayer.getActionbarQueue().isEmpty()) {
                        AbstractActionbar newData = gamePlayer.getActionbarQueue().get(0);
                        gamePlayer.setCurrentActionbarTime(newData.getTime() + System.currentTimeMillis());
                    }
                    continue;
                }
                gamePlayer.sendInfoActionbar(data);
                continue;
            }
            if (gamePlayer.getDefaultActionbar() != null)
                player.sendActionBar(gamePlayer.getDefaultActionbar());
        }
    }
}
