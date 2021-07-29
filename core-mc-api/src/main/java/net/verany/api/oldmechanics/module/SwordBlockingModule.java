package net.verany.api.oldmechanics.module;

import net.verany.api.oldmechanics.AbstractModule;
import net.verany.api.task.RunnableSeries;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SwordBlockingModule extends AbstractModule {

    private static final ItemStack SHIELD = new ItemStack(Material.SHIELD);
    private final Map<UUID, ItemStack> storedOffhandItems = new HashMap<>();
    private final Map<UUID, RunnableSeries> correspondingTasks = new HashMap<>();
    private int restoreDelay;
    private boolean blacklist;
    private final EnumSet<Material> swords = EnumSet.of(Material.DIAMOND_SWORD, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD);
    private final EnumSet<Material> noBlockingItems;

    public SwordBlockingModule() {
        restoreDelay = 10;
        blacklist = true;
        noBlockingItems = EnumSet.noneOf(Material.class);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getItem() == null) return;

        Action action = e.getAction();

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        if (action == Action.RIGHT_CLICK_BLOCK && isInteractive(e.getClickedBlock().getType()))
            return;

        Player p = e.getPlayer();

        UUID id = p.getUniqueId();

        if (p.isBlocking()) {
            tryCancelTask(id);
        } else {
            ItemStack item = e.getItem();

            if (!swords.contains(item.getType()) || hasShield(p)) return;

            PlayerInventory inv = p.getInventory();

            boolean isANoBlockingItem = noBlockingItems.contains(inv.getItemInOffHand().getType());

            if (blacklist == isANoBlockingItem) return;

            storedOffhandItems.put(id, inv.getItemInOffHand());

            inv.setItemInOffHand(SHIELD);
        }

        scheduleRestore(p);
    }

    private boolean isInteractive(Material material) {
        String interactive = "crafting_table,enchanting_table,anvil,brewing_stand,trapped_chest,chest,dispenser,dropper,furnace,jukebox,ender_chest,stone_button,stone_button,beacon,tripwire_hook,hopper,item_frame,acacia_door,birch_door,dark_oak_door,jungle_door,spruce_door,lever,spruce_fence_gate,birch_fence_gate,jungle_fence_gate,dark_oak_fence_gate,acacia_fence_gate,white_shulker_box,orange_shulker_box,magenta_shulker_box,light_blue_shulker_box,yellow_shulker_box,lime_shulker_box,pink_shulker_box,gray_shulker_box,cyan_shulker_box,purple_shulker_box,blue_shulker_box,brown_shulker_box,green_shulker_box,red_shulker_box,black_shulker_box";
        for (String s : interactive.split(","))
            if (s.toUpperCase().equals(material.name()))
                return true;
        return false;
    }

    @EventHandler
    public void onHotBarChange(PlayerItemHeldEvent e) {
        tryCancelTask(e.getPlayer().getUniqueId());
        restore(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChange(PlayerChangedWorldEvent e) {
        restore(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogout(PlayerQuitEvent e) {
        restore(e.getPlayer());
        tryCancelTask(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!isBlocking(e.getEntity().getUniqueId())) return;

        Player p = e.getEntity();
        UUID id = p.getUniqueId();

        e.getDrops().replaceAll(item -> {
            ItemStack itemStack = item;
            if (itemStack.getType() == Material.SHIELD)
                itemStack = storedOffhandItems.remove(id);
            return itemStack;
        });

        tryCancelTask(id);
        restore(p);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        if (isBlocking(p.getUniqueId()))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e) {

        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();

            if (isBlocking(p.getUniqueId())) {
                ItemStack cursor = e.getCursor();
                ItemStack current = e.getCurrentItem();
                if (cursor != null && cursor.getType() == Material.SHIELD ||
                    current != null && current.getType() == Material.SHIELD) {
                    e.setCancelled(true);
                    restore(p);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDrop(PlayerDropItemEvent e) {
        Item is = e.getItemDrop();

        Player p = e.getPlayer();

        if (isBlocking(p.getUniqueId()) && is.getItemStack().getType() == Material.SHIELD) {
            e.setCancelled(true);
            restore(p);
        }
    }

    private void scheduleRestore(final Player player) {
        BukkitRunnable removeItem = new BukkitRunnable() {
            @Override
            public void run() {
                restore(player);
            }
        };
        removeItem.runTaskLater(getProject(), restoreDelay);

        BukkitRunnable checkBlocking = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isBlocking()) {
                    restore(player);
                    tryCancelTask(player.getUniqueId());
                }
            }
        };
        checkBlocking.runTaskTimer(getProject(), 10L, 2L);

        correspondingTasks.put(player.getUniqueId(), new RunnableSeries(removeItem, checkBlocking));
    }

    private void restore(Player p) {
        UUID id = p.getUniqueId();

        if (!isBlocking(id)) return;

        if (p.isBlocking())
            postponeRestoring(p);
        else {
            p.getInventory().setItemInOffHand(storedOffhandItems.get(id));
            storedOffhandItems.remove(id);
        }
    }

    private void tryCancelTask(UUID id) {
        RunnableSeries series = correspondingTasks.remove(id);
        if (series != null) series.cancelAll();
    }

    private void postponeRestoring(Player p) {
        UUID id = p.getUniqueId();
        Optional.ofNullable(correspondingTasks.get(id))
            .ifPresent(RunnableSeries::cancelAll);

        correspondingTasks.remove(id);
        scheduleRestore(p);
    }

    private boolean isBlocking(UUID uuid) {
        return storedOffhandItems.containsKey(uuid);
    }

    private boolean hasShield(Player p) {
        return p.getInventory().getItemInOffHand().getType() == Material.SHIELD;
    }
}
