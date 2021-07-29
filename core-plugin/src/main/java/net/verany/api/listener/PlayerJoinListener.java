package net.verany.api.listener;

import net.verany.api.Verany;
import net.verany.api.event.events.PlayerLoadCompleteEvent;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.PlayerInfo;
import net.verany.api.player.permission.Permissible;
import net.verany.api.plugin.IVeranyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.joor.Reflect;

import java.util.Optional;

public record PlayerJoinListener(IVeranyPlugin project) implements Listener {

    @EventHandler
    public void handleJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        IPlayerInfo playerInfo;
        Optional<IPlayerInfo> playerInfoOptional = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId(), IPlayerInfo.class);
        if (playerInfoOptional.isEmpty()) {
            playerInfo = new PlayerInfo(project, player.getName());
            playerInfo.load(player.getUniqueId());
            Verany.PROFILE_OBJECT.setPlayer(IPlayerInfo.class, playerInfo);
        } else {
            playerInfo = playerInfoOptional.get();
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

        player.sendMessage("moin " + playerInfo.getNameWithColor());

        /*Bukkit.getScheduler().scheduleSyncDelayedTask(project, () -> {
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
        });*/
    }

    @EventHandler
    public void handleLoadComplete(PlayerLoadCompleteEvent event) {
        Player player = event.getPlayer();

        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        //Bukkit.getOnlinePlayers().forEach(onlinePlayer -> new TabListObject().setTabList(onlinePlayer));
    }


}
