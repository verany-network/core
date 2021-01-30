package net.verany.executor.listener;

import net.verany.api.Verany;
import net.verany.api.event.AbstractListener;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends AbstractListener {
    public PlayerQuitListener(VeranyProject project) {
        super(project);
        Verany.registerListener(project, PlayerQuitEvent.class, event -> {
            Player player = event.getPlayer();

            IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get();
            playerInfo.setPlayer(null);
            playerInfo.update();
        });
    }
}
