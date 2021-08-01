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
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public record PlayerQuitListener (VeranyPlugin project) implements Listener {

    @EventListener
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.quitMessage(null);

        IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId(), IPlayerInfo.class).get();

        playerInfo.setBossBar((BossBar) null);
        playerInfo.setDefaultBossBar(null);

        if (!VeranyServer.ROUNDS.isEmpty()) {
            playerInfo.getPlayer(IVolcanoPlayer.class).quitRound();
            Verany.removePlayer(player.getUniqueId(), IVolcanoPlayer.class);
        }

        Verany.getHotbarItem(player).forEach(Verany.HOTBAR_ITEMS::remove);
        playerInfo.update();
    }

}
