package net.verany.executor.listener;

import net.verany.api.Verany;
import net.verany.api.event.AbstractListener;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.PlayerInfo;
import net.verany.volcano.VeranyServer;
import net.verany.volcano.player.IVolcanoPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends AbstractListener {
    public PlayerQuitListener(VeranyProject project) {
        super(project);
        Verany.registerListener(project, PlayerQuitEvent.class, event -> {
            Player player = event.getPlayer();

            event.setQuitMessage(null);

            IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get();

            if (!VeranyServer.ROUNDS.isEmpty()) {
                playerInfo.getPlayer(IVolcanoPlayer.class).quitRound();
                Verany.removePlayer(player.getUniqueId().toString(), IVolcanoPlayer.class);
            }

            playerInfo.setPlayer(null);
            ((PlayerInfo) playerInfo).sendUpdate();
            playerInfo.update();

        }, EventPriority.LOWEST);
    }
}
