package net.verany.api.bossbar;

import net.verany.api.Verany;
import net.verany.api.actionbar.AbstractActionbar;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.task.AbstractTask;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarTask extends AbstractTask {

    private final VeranyProject project;

    public BossBarTask(long waitTime, VeranyProject project) {
        super(waitTime);
        this.project = project;
    }

    @Override
    public void run() {
        for (IPlayerInfo gamePlayer : Verany.getOnlinePlayers()) {
            Player player = gamePlayer.getPlayer();
            if (player == null) continue;
            AbstractBossBar defaultBossBar = gamePlayer.getDefaultBossBar();
            if (defaultBossBar != null) {
                BossBar bossBar = gamePlayer.getBossBar();
                if (bossBar == null) {
                    bossBar = Bukkit.createBossBar(new NamespacedKey(project, player.getUniqueId().toString()), defaultBossBar.getText(), defaultBossBar.getBarColor(), defaultBossBar.getBarStyle());
                    bossBar.addPlayer(player);
                } else {
                    bossBar.setProgress(defaultBossBar.getProgress());
                    bossBar.setTitle(defaultBossBar.getText());
                    bossBar.setColor(defaultBossBar.getBarColor());
                    bossBar.setStyle(defaultBossBar.getBarStyle());
                }
                gamePlayer.setBossBar(bossBar);
            } else {
                if (gamePlayer.getBossBar() != null)
                    gamePlayer.setBossBar((BossBar) null);
            }
        }
    }
}
