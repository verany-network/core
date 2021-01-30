package net.verany.executor.listener;

import net.verany.api.Verany;
import net.verany.api.event.AbstractListener;
import net.verany.api.hotbar.HotbarItem;
import net.verany.api.module.VeranyProject;
import net.verany.api.npc.INPC;
import net.verany.api.npc.reader.PacketReader;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.afk.IAFKObject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

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
    }
}
