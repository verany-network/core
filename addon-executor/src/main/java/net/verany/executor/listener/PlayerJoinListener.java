package net.verany.executor.listener;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.verany.api.Verany;
import net.verany.api.event.AbstractListener;
import net.verany.api.event.events.PlayerLoadCompleteEvent;
import net.verany.api.inventory.InventoryBuilder;
import net.verany.api.itembuilder.ItemBuilder;
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
import net.verany.volcano.round.AbstractVolcanoRound;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftHumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;
import org.json.JSONObject;

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
                playerInfo.load(player.getUniqueId());
                Verany.PROFILE_OBJECT.getRegisteredPlayers().add(playerInfo);
            } else {
                playerInfo = playerInfoOptional.get();
                if (playerInfo.isShouldLoad()) {
                    playerInfo.load(player.getUniqueId());
                    playerInfo.setShouldLoad(false);
                }
            }
        });

        Verany.registerListener(project, PlayerJoinEvent.class, event -> {
            Player player = event.getPlayer();

            event.setJoinMessage(null);

            IPlayerInfo playerInfo = Verany.getPlayer(player);
            playerInfo.setPlayer(player);

            Bukkit.getScheduler().scheduleSyncDelayedTask(CoreExecutor.INSTANCE, () -> {
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

    private void loadRoundInventory(IPlayerInfo player) {
        Inventory inventory = InventoryBuilder.builder().size(9 * 6).title("Choose your round").onClick(clickEvent -> {
            clickEvent.setCancelled(true);

            String id = clickEvent.getCurrentItem().getItemMeta().getDisplayName();
            player.getPlayer(IVolcanoPlayer.class).joinRound(id, CoreExecutor.INSTANCE);

            ICloudPlayer cloudPlayer = player.getCloudPlayer();
            cloudPlayer.getProperties().append("round-id", id);
            CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class).updateOnlinePlayer(cloudPlayer);

            player.getPlayer().closeInventory();
        }).onClose(onClose -> {
            Bukkit.getScheduler().runTaskLater(CoreExecutor.INSTANCE, () -> {
                if (!player.getCloudPlayer().getProperties().contains("round-id"))
                    loadRoundInventory(player);
            }, 2);
        }).build().createAndOpen(player.getPlayer());

        for (AbstractVolcanoRound round : VeranyServer.ROUNDS) {
            inventory.addItem(new ItemBuilder(Material.ARMOR_STAND).setDisplayName(round.getId()).build());
        }
    }
}
