package net.verany.api.example.roundsystem.listener;

import net.verany.api.Verany;
import net.verany.api.example.roundsystem.game.AbstractExampleManager;
import net.verany.api.player.IPlayerInfo;
import net.verany.volcano.event.VolcanoPlayerJoinEvent;
import net.verany.volcano.round.AbstractVolcanoRound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void handleJoin(VolcanoPlayerJoinEvent event) {
        Player player = event.getPlayer();
        IPlayerInfo playerInfo = Verany.getPlayer(player);

        AbstractVolcanoRound round = event.getRound();
        AbstractExampleManager gameManager = round.getGameManager(AbstractExampleManager.class);


    }

}
