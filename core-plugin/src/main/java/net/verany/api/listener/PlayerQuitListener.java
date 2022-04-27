package net.verany.api.listener;

import de.dytanic.cloudnet.driver.event.EventListener;
import net.verany.api.Verany;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.PlayerInfo;
import net.verany.volcano.VeranyServer;
import net.verany.volcano.player.IVolcanoPlayer;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.quitMessage(null);

        IPlayerInfo playerInfo = Verany.getPlayer(player.getUniqueId());

        playerInfo.setBossBar((BossBar) null);
        playerInfo.setDefaultBossBar(null);

        if (!VeranyServer.ROUNDS.isEmpty()) {
            playerInfo.getPlayer(IVolcanoPlayer.class).quitRound();
            Verany.removePlayer(player.getUniqueId(), IVolcanoPlayer.class);
        }

        playerInfo.update();
    }

}
