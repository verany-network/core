package net.verany.executor.listener;

import net.verany.api.Verany;
import net.verany.api.event.events.PlayerLoadCompleteEvent;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.PlayerInfo;
import net.verany.api.player.permission.Permissible;
import net.verany.api.tablist.TabListObject;
import net.verany.executor.CoreExecutor;
import net.verany.volcano.VeranyServer;
import net.verany.volcano.player.IVolcanoPlayer;
import net.verany.volcano.player.VolcanoPlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.joor.Reflect;

import java.util.Optional;

public record PlayerJoinListener(VeranyProject project) implements Listener {

    @EventHandler
    public void handleLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        IPlayerInfo playerInfo;
        Optional<IPlayerInfo> playerInfoOptional = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId());
        if (playerInfoOptional.isEmpty()) {
            playerInfo = new PlayerInfo(project, player.getName());
            playerInfo.load(player.getUniqueId());
            Verany.PROFILE_OBJECT.getRegisteredPlayers().add(playerInfo);
        } else {
            playerInfo = playerInfoOptional.get();
            if (playerInfo.isShouldLoad()) {
                playerInfo.load(player.getUniqueId());
                playerInfo.setShouldLoad(false);
            }
        }
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.joinMessage(null);
        if (!player.getActivePotionEffects().isEmpty()) {
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        }

        IPlayerInfo playerInfo = Verany.getPlayer(player);
        playerInfo.setPlayer(player);

        Bukkit.getScheduler().scheduleSyncDelayedTask(project, () -> {
            if (!VeranyServer.ROUNDS.isEmpty()) {
                if (playerInfo.getCloudPlayer().getProperties().contains("round-id")) {
                    IVolcanoPlayer volcanoPlayer = new VolcanoPlayer();
                    volcanoPlayer.load(player.getUniqueId());
                    Verany.setPlayer(IVolcanoPlayer.class, volcanoPlayer);

                    volcanoPlayer.joinRound(playerInfo.getCloudPlayer().getProperties().getString("round-id"), CoreExecutor.INSTANCE);

                    for (IVolcanoPlayer otherPlayer : volcanoPlayer.getRound().getOtherPlayers()) {
                        Player otherBukkitPlayer = Bukkit.getPlayer(otherPlayer.getUniqueId());
                        otherBukkitPlayer.hidePlayer(project, player);
                        player.hidePlayer(project, otherBukkitPlayer);
                    }
                }

            }

            Bukkit.getPluginManager().callEvent(new PlayerLoadCompleteEvent(player));
        });
    }

    @EventHandler
    public void handleLoadComplete(PlayerLoadCompleteEvent event) {
        Player player = event.getPlayer();

        Reflect.on(player).set("perm", new Permissible(player));

        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> new TabListObject().setTabList(onlinePlayer));
    }

}
