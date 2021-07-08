package net.verany.executor.listener;

import de.dytanic.cloudnet.driver.event.EventListener;
import net.verany.api.Verany;
import net.verany.api.event.AbstractListener;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.PlayerInfo;
import net.verany.volcano.VeranyServer;
import net.verany.volcano.player.IVolcanoPlayer;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventListener
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.quitMessage(null);

        IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get();

        playerInfo.setBossBar((BossBar) null);
        playerInfo.setDefaultBossBar(null);

        if (!VeranyServer.ROUNDS.isEmpty()) {
            playerInfo.getPlayer(IVolcanoPlayer.class).quitRound();
            Verany.removePlayer(player.getUniqueId().toString(), IVolcanoPlayer.class);
        }

        ((PlayerInfo) playerInfo).sendUpdate();
        playerInfo.update();
    }

    /*public PlayerQuitListener(VeranyProject project) {
        super(project);
        Verany.registerListener(project, PlayerQuitEvent.class, event -> {
            Player player = event.getPlayer();

            event.setQuitMessage(null);

            IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get();

            playerInfo.setBossBar((BossBar) null);
            playerInfo.setDefaultBossBar(null);

            if (!VeranyServer.ROUNDS.isEmpty()) {
                playerInfo.getPlayer(IVolcanoPlayer.class).quitRound();
                Verany.removePlayer(player.getUniqueId().toString(), IVolcanoPlayer.class);
            }

            ((PlayerInfo) playerInfo).sendUpdate();
            playerInfo.update();

        }, EventPriority.LOWEST);
    }*/
}
