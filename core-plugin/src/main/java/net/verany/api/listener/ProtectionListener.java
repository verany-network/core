package net.verany.api.listener;

import net.verany.api.Verany;
import net.verany.api.hotbar.HotbarItem;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.afk.IAFKObject;
import net.verany.api.region.GameRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public record ProtectionListener(VeranyPlugin plugin) implements Listener {

    @EventHandler
    public void handlePlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        for (HotbarItem hotbarItem : Verany.HOTBAR_ITEMS.getOrDefault(player.getUniqueId(), new ArrayList<>()))
            if (hotbarItem.getItemStack().getType().equals(player.getItemInHand().getType()) && hotbarItem.getItemStack().getItemMeta().getDisplayName().equals(player.getItemInHand().getItemMeta().getDisplayName()))
                hotbarItem.onPlace(event);
    }

    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null) {
            if (Verany.INVENTORY_MAP.containsKey(player))
                if (Arrays.equals(Verany.INVENTORY_MAP.get(player).getInventory().getStorageContents(), event.getInventory().getStorageContents()))
                    Verany.INVENTORY_MAP.get(player).onClick(event);
            return;
        }
        if (event.getCurrentItem().getItemMeta() == null) {
            return;
        }

        if (Verany.INVENTORY_MAP.containsKey(player))
            if (Arrays.equals(Verany.INVENTORY_MAP.get(player).getInventory().getStorageContents(), event.getInventory().getStorageContents()))
                Verany.INVENTORY_MAP.get(player).onClick(event);

        try {
            for (HotbarItem hotbarItem : Verany.HOTBAR_ITEMS.getOrDefault(player.getUniqueId(), new ArrayList<>()))
                if (hotbarItem.getItemStack().getType().equals(event.getCurrentItem().getType()) && hotbarItem.getItemStack().getItemMeta() != null && hotbarItem.getItemStack().getItemMeta().getDisplayName().equals(event.getCurrentItem().getItemMeta().getDisplayName()))
                    hotbarItem.onClick(event);
        } catch (Exception ignore) {
        }
    }

    @EventHandler
    public void handleClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (Verany.INVENTORY_MAP.containsKey(player))
            if (Arrays.equals(Verany.INVENTORY_MAP.get(player).getInventory().getStorageContents(), event.getInventory().getStorageContents()))
                Verany.INVENTORY_MAP.get(player).onClose(event);
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() == null) return;

        if (Verany.HOTBAR_ITEMS.containsKey(player.getUniqueId()))
            for (HotbarItem hotbarItem : Verany.HOTBAR_ITEMS.get(player.getUniqueId()))
                if (hotbarItem.getItemStack().getType().equals(event.getItem().getType()) && hotbarItem.getItemStack().getItemMeta().getDisplayName().equals(event.getItem().getItemMeta().getDisplayName()))
                    hotbarItem.onInteract(event);

    }

    @EventHandler
    public void handleInteractEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();

        for (HotbarItem hotbarItem : Verany.HOTBAR_ITEMS.getOrDefault(player.getUniqueId(), new ArrayList<>()))
            if (hotbarItem.getItemStack().getType().equals(player.getInventory().getItemInMainHand().getType()) && hotbarItem.getItemStack().getItemMeta().getDisplayName().equals(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()))
                hotbarItem.onInteract(event);

    }

    @EventHandler
    public void handleDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        for (HotbarItem hotbarItem : Verany.HOTBAR_ITEMS.getOrDefault(player.getUniqueId(), new ArrayList<>()))
            if (hotbarItem.getItemStack().getType().equals(event.getItemDrop().getItemStack().getType()) && hotbarItem.getItemStack().getItemMeta().getDisplayName().equals(event.getItemDrop().getItemStack().getItemMeta().getDisplayName()))
                hotbarItem.onDrop(event);
    }

    @EventHandler
    public void handleMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        IPlayerInfo playerInfo = Verany.getPlayer(player.getUniqueId(), IPlayerInfo.class);

        if (playerInfo.getAfkObject().isCheckEnabled(IAFKObject.CheckType.MOVE))
            playerInfo.getAfkObject().resetAfkTime();

        for (GameRegion gameRegion : Verany.GAME_REGIONS) {
            if (gameRegion.isInRegion(player.getLocation())) {
                if (player.hasMetadata(gameRegion.getId())) continue;
                plugin.setMetadata(player, gameRegion.getId(), gameRegion);
                gameRegion.onEnter(player);
            } else {
                if (player.hasMetadata(gameRegion.getId())) {
                    gameRegion.onLeave(player);
                    plugin.removeMetadata(player, gameRegion.getId());
                }
            }
        }
    }

}
