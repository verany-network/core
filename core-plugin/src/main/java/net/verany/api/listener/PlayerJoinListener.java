package net.verany.api.listener;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.EventPriority;
import net.verany.api.Core;
import net.verany.api.Verany;
import net.verany.api.event.events.PlayerLoadCompleteEvent;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.PlayerInfo;
import net.verany.api.player.permission.Permissible;
import net.verany.api.plugin.IVeranyPlugin;
import net.verany.api.tablist.TabListObject;
import net.verany.volcano.VeranyServer;
import net.verany.volcano.player.IVolcanoPlayer;
import net.verany.volcano.player.VolcanoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.joor.Reflect;

import java.util.Optional;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void handleJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        IPlayerInfo playerInfo;
        IPlayerInfo playerInfoOptional = Verany.getPlayer(player.getUniqueId(), IPlayerInfo.class);
        if (playerInfoOptional == null) {
            playerInfo = new PlayerInfo(Core.INSTANCE, player.getName());
            playerInfo.load(player.getUniqueId());
            Verany.setPlayer(IPlayerInfo.class, playerInfo);
        } else {
            playerInfo = playerInfoOptional;
            playerInfo.load(player.getUniqueId());
        }
        Reflect.on(player).set("perm", new Permissible(player));
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
        playerInfo.setSkinData();

        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.INSTANCE, () -> {
            if (!VeranyServer.ROUNDS.isEmpty()) {
                if (playerInfo.getCloudPlayer().getProperties().contains("round-id")) {
                    IVolcanoPlayer volcanoPlayer = new VolcanoPlayer();
                    volcanoPlayer.load(player.getUniqueId());
                    Verany.setPlayer(IVolcanoPlayer.class, volcanoPlayer);

                    volcanoPlayer.joinRound(playerInfo.getCloudPlayer().getProperties().getString("round-id"), Core.INSTANCE);

                    for (IVolcanoPlayer otherPlayer : volcanoPlayer.getRound().getOtherPlayers()) {
                        Player otherBukkitPlayer = Bukkit.getPlayer(otherPlayer.getUniqueId());
                        otherBukkitPlayer.hidePlayer(Core.INSTANCE, player);
                        player.hidePlayer(Core.INSTANCE, otherBukkitPlayer);
                    }
                }

            }

            Bukkit.getPluginManager().callEvent(new PlayerLoadCompleteEvent(player));
        });
    }

    @EventHandler
    public void handleLoadComplete(PlayerLoadCompleteEvent event) {
        Player player = event.getPlayer();

        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> new TabListObject().setTabList(onlinePlayer));
    }

}
