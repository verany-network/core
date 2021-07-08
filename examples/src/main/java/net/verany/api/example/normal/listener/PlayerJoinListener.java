package net.verany.api.example.normal.listener;

import net.verany.api.Verany;
import net.verany.api.actionbar.DefaultActionbar;
import net.verany.api.actionbar.NumberActionbar;
import net.verany.api.example.normal.ExamplePlugin;
import net.verany.api.example.normal.player.ExamplePlayer;
import net.verany.api.example.normal.player.IExamplePlayer;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.concurrent.TimeUnit;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        IPlayerInfo playerInfo = Verany.getPlayer(player);

        // Initialize Player
        IExamplePlayer examplePlayer = new ExamplePlayer(ExamplePlugin.INSTANCE);

        // Load player
        examplePlayer.load(player.getUniqueId());

        // Register IExamplePlayer in IPlayerInfo
        Verany.setPlayer(IExamplePlayer.class, examplePlayer);

        examplePlayer.setItems();
        examplePlayer.setScoreboard();

        
        // Set the default actionbar
        playerInfo.setDefaultActionbar("This is your default actionbar");

        // Add a actionbar for 5 Seconds
        playerInfo.addActionbar(new DefaultActionbar("Example Actionbar", TimeUnit.SECONDS.toMillis(5)));

        // Add another actionbar for 3 seconds (Will displayed after actionbar before is expired)
        playerInfo.addActionbar(new NumberActionbar("You got {0} Coins", TimeUnit.SECONDS.toMillis(3), 10));
    }

}
