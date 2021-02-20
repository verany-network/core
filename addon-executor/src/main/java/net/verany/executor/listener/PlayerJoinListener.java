package net.verany.executor.listener;

import net.md_5.bungee.api.chat.ClickEvent;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.verany.api.Verany;
import net.verany.api.event.AbstractListener;
import net.verany.api.event.events.PlayerLoadCompleteEvent;
import net.verany.api.message.AbstractComponentBuilder;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.api.npc.reader.PacketReader;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.PlayerInfo;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.api.player.permission.Permissible;
import net.verany.api.player.permission.duration.GroupTime;
import net.verany.api.player.permission.group.AbstractPermissionGroup;
import net.verany.api.player.permission.group.PermissionGroup;
import net.verany.api.tablist.TabListObject;
import net.verany.executor.CoreExecutor;
import net.verany.executor.PlayerList;
import net.verany.volcano.VeranyServer;
import net.verany.volcano.player.IVolcanoPlayer;
import net.verany.volcano.player.VolcanoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftHumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

public class PlayerJoinListener extends AbstractListener {

    public PlayerJoinListener(VeranyProject project) {
        super(project);

        Verany.registerListener(project, PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();

            IPlayerInfo playerInfo;
            Optional<IPlayerInfo> playerInfoOptional = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId());
            if (playerInfoOptional.isEmpty()) {
                playerInfo = new PlayerInfo(project, player.getName());
                Verany.PROFILE_OBJECT.getRegisteredPlayers().add(playerInfo);
            } else
                playerInfo = playerInfoOptional.get();
            playerInfo.load(player.getUniqueId());
        });

        Verany.registerListener(project, PlayerJoinEvent.class, event -> {
            Player player = event.getPlayer();

            event.setJoinMessage(null);

            IPlayerInfo playerInfo = Verany.getPlayer(player);
            playerInfo.setPlayer(player);

            Bukkit.getScheduler().scheduleSyncDelayedTask(CoreExecutor.INSTANCE, () -> {
                Bukkit.getPluginManager().callEvent(new PlayerLoadCompleteEvent(player));

                if (!VeranyServer.ROUNDS.isEmpty()) {
                    IVolcanoPlayer volcanoPlayer = new VolcanoPlayer();
                    volcanoPlayer.load(player.getUniqueId());
                    Verany.setPlayer(IVolcanoPlayer.class, volcanoPlayer);
                    volcanoPlayer.joinRound(VeranyServer.ROUNDS.get(0).getId());
                }
            });
        });

        Verany.registerListener(project, PlayerLoadCompleteEvent.class, event -> {
            Player player = event.getPlayer();

            try {
                Field field = CraftHumanEntity.class.getDeclaredField("perm");
                field.setAccessible(true);
                Field modifiersField = field.getClass().getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                field.set(player, new Permissible(player));
            } catch (Exception e) {
                e.printStackTrace();
            }

            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> new TabListObject().setTabList(onlinePlayer));
        }, EventPriority.LOWEST);
    }
}
