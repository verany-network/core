package net.verany.executor.listener;

import com.google.gson.Gson;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.verany.api.Verany;
import net.verany.api.event.AbstractListener;
import net.verany.api.hotbar.HotbarItem;
import net.verany.api.message.AbstractComponentBuilder;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.api.npc.INPC;
import net.verany.api.npc.reader.PacketReader;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.PlayerInfo;
import net.verany.api.player.afk.IAFKObject;
import net.verany.api.player.friend.FriendObject;
import net.verany.api.player.friend.data.FriendData;
import net.verany.api.redis.events.VeranyMessageInEvent;
import net.verany.executor.CoreExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.UUID;

public class ProtectionListener extends AbstractListener {

    public ProtectionListener(VeranyProject project) {
        super(project);

        Verany.registerListener(project, BlockPlaceEvent.class, event -> {
            Player player = event.getPlayer();

            for (HotbarItem abstractHotbarItem : Verany.getHotbarItem(player))
                if (abstractHotbarItem.getItemStack().getType().equals(player.getItemInHand().getType()) && abstractHotbarItem.getItemStack().getItemMeta().getDisplayName().equals(player.getItemInHand().getItemMeta().getDisplayName()))
                    abstractHotbarItem.onPlace(event);

        });

        Verany.registerListener(project, PlayerJoinEvent.class, event -> {
            Player player = event.getPlayer();

            PacketReader packetReader = new PacketReader(player);
            packetReader.inject();
            player.setMetadata("reader", new FixedMetadataValue(project, packetReader));
        }, EventPriority.HIGHEST);

        Verany.registerListener(project, PlayerQuitEvent.class, event -> {
            Player player = event.getPlayer();

            for (INPC npc : Verany.NPCS)
                if (npc.getPlayer().equals(player))
                    npc.despawn();

            PacketReader reader = (PacketReader) player.getMetadata("reader").get(0).value();
            reader.uninject();

            Verany.getHotbarItem(player).forEach(Verany.HOTBAR_ITEMS::remove);
        });

        Verany.registerListener(project, InventoryClickEvent.class, event -> {
            Player player = (Player) event.getWhoClicked();

            if (event.getCurrentItem() == null) {
                return;
            }
            if (event.getCurrentItem().getItemMeta() == null) {
                return;
            }

            if (Verany.INVENTORY_MAP.containsKey(player))
                if (Arrays.equals(Verany.INVENTORY_MAP.get(player).getInventory().getStorageContents(), event.getInventory().getStorageContents()))
                    Verany.INVENTORY_MAP.get(player).onClick(event);

            try {
                for (HotbarItem hotbarItem : Verany.getHotbarItem(player))
                    if (hotbarItem.getItemStack().getType().equals(event.getCurrentItem().getType()) && hotbarItem.getItemStack().getItemMeta() != null && hotbarItem.getItemStack().getItemMeta().getDisplayName().equals(event.getCurrentItem().getItemMeta().getDisplayName()))
                        hotbarItem.onClick(event);
            } catch (Exception ignore) {
            }
        });

        Verany.registerListener(project, PlayerInteractEvent.class, event -> {
            Player player = event.getPlayer();

            if (event.getItem() == null) return;

            for (HotbarItem hotbarItem : Verany.getHotbarItem(player))
                if (hotbarItem.getItemStack().getType().equals(event.getItem().getType()) && hotbarItem.getItemStack().getItemMeta().getDisplayName().equals(event.getItem().getItemMeta().getDisplayName()))
                    hotbarItem.onInteract(event);

        });

        Verany.registerListener(project, PlayerDropItemEvent.class, event -> {
            Player player = event.getPlayer();

            for (HotbarItem hotbarItem : Verany.getHotbarItem(player))
                if (hotbarItem.getItemStack().getType().equals(event.getItemDrop().getItemStack().getType()) && hotbarItem.getItemStack().getItemMeta().getDisplayName().equals(event.getItemDrop().getItemStack().getItemMeta().getDisplayName()))
                    hotbarItem.onDrop(event);
        });

        Verany.registerListener(project, PlayerMoveEvent.class, event -> {
            Player player = event.getPlayer();
            IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get();
            if (playerInfo.getAfkObject().isCheckEnabled(IAFKObject.CheckType.MOVE))
                playerInfo.getAfkObject().resetAfkTime();
        });

        Verany.registerListener(project, VeranyMessageInEvent.class, event -> {
            String[] data = event.getMessage().split("~");

            if (data[0].equals("teamspeak")) {
                if (data[1].equals("support")) {
                    if (data[2].equals("accept")) return;
                    UUID supporter = UUID.fromString(data[2]);
                    String target = data[3];
                    Player player = Bukkit.getPlayer(supporter);
                    if (player != null) {
                        if (!player.hasPermission("verany.command.support"))
                            return;
                        if (player.hasMetadata("support.time")) {
                            long time = player.getMetadata("support.time").get(0).asLong();
                            if (time >= System.currentTimeMillis()) return;
                        }
                        CoreExecutor.INSTANCE.setMetadata(player, "support.time", System.currentTimeMillis() + 100);
                        IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(supporter).get();
                        playerInfo.sendMessage(new AbstractComponentBuilder(playerInfo.getPrefix("TeamSpeak") + playerInfo.getKey("core.teamspeak.support", new Placeholder("%target%", target))) {
                            @Override
                            public void onCreate() {
                                setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/support accept"));
                                setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(playerInfo.getKey("core.teamspeak.support.hover")).create()));
                            }
                        });
                    }
                } else if (data[2].equals("meeting")) {
                    if (data[1].equals("start")) {
                        for (IPlayerInfo playerInfo : Verany.getOnlinePlayers())
                            if (playerInfo.getPermissionObject().hasPermission("verany.meeting"))
                                playerInfo.sendKey(playerInfo.getPrefix("TeamSpeak"), "core.teamspeak.meeting.start", new Placeholder("%url%", "ts3server://verany.net"));
                    }
                }
            } else if (data[0].equals("update")) {
                if (data[1].equals("player")) {
                    UUID uuid = UUID.fromString(data[2]);
                    if (Bukkit.getPlayer(uuid) == null) {
                        PlayerInfo.PlayerData playerData = new Gson().fromJson(data[3], PlayerInfo.PlayerData.class);
                        if (Verany.PROFILE_OBJECT.getPlayer(uuid).isEmpty()) {
                            IPlayerInfo playerInfo = new PlayerInfo(CoreExecutor.INSTANCE, playerData.getName());
                            playerInfo.load(uuid);
                            Verany.PROFILE_OBJECT.getRegisteredPlayers().add(playerInfo);
                            return;
                        }
                        ((PlayerInfo) Verany.PROFILE_OBJECT.getPlayer(uuid).get()).update(PlayerInfo.PlayerData.class, playerData);
                    }
                } else if (data[1].equals("friends")) {
                    UUID uuid = UUID.fromString(data[2]);
                    FriendObject friendObject = (FriendObject) Verany.PROFILE_OBJECT.getPlayer(uuid).get().getFriendObject();
                    friendObject.update(FriendObject.PlayerFriend.class, Verany.REDIS_MANAGER.getObject("friends_" + uuid.toString(), FriendObject.PlayerFriend.class));
                }
            }
        });
    }
}
