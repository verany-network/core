package net.verany.api.hotbar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.itembuilder.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public abstract class HotbarItem {
    private final ItemStack itemStack;

    /**
     * @param itemBuilder new {@link ItemBuilder ItemBuilder.class}
     */
    public HotbarItem(ItemBuilder itemBuilder) {
        this.itemStack = itemBuilder.build();
    }


    /**
     * Calls method if {@link InventoryClickEvent InventoryClickEvent.class} are executed on {@link #itemStack}
     *
     * @param event interact event
     */
    public void onInteract(PlayerInteractEvent event) {
    }

    /**
     * Calls method if {@link PlayerDropItemEvent PlayerDropItemEvent.class} are executed on {@link #itemStack}
     *
     * @param event drop event
     */
    public void onDrop(PlayerDropItemEvent event) {
    }

    /**
     * Calls method if {@link InventoryClickEvent InventoryClickEvent.class} are executed on {@link #itemStack}
     *
     * @param event click event
     */
    public void onClick(InventoryClickEvent event) {
    }

    /**
     * Calls method if {@link BlockPlaceEvent BlockPlaceEvent.class} are executed on {@link #itemStack}
     *
     * @param event place event
     */
    public void onPlace(BlockPlaceEvent event) {
    }

    public void onInteract(PlayerInteractAtEntityEvent event) {
    }
}
