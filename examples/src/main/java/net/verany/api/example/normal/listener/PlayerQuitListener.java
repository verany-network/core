package net.verany.api.example.normal.listener;

import net.verany.api.Verany;
import net.verany.api.example.normal.player.IExamplePlayer;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        IPlayerInfo playerInfo = Verany.getPlayer(player);

        IExamplePlayer examplePlayer = playerInfo.getPlayer(IExamplePlayer.class);
        examplePlayer.update();
        Verany.removePlayer(player.getUniqueId().toString(), IExamplePlayer.class);
    }

}
