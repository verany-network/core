package net.verany.api.inventory;

import lombok.Builder;
import lombok.Getter;
import net.verany.api.Verany;
import net.verany.api.itembuilder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Builder
@Getter
public class InventoryBuilder implements IInventoryBuilder {
    private final int size;
    private final InventoryType inventoryType;
    private final String title;
    @Deprecated
    private final Consumer<InventoryClickEvent> event;
    private final Consumer<InventoryCloseEvent> onClose;
    private final Consumer<InventoryClickEvent> onClick;
    private final Consumer<InventoryClickEvent> onNullClick;
    private final Map<Integer, ItemStack> itemStackMap = new HashMap<>();
    private final Map<PageData, PageSwitchHandler> pageSwitchHandlers = new ConcurrentHashMap<>();
    private Inventory inventory;

    /*private final ItemStack rightSkull = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19").build();
    private final ItemStack leftSkull = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==").build();
*/
    @Override
    public IInventoryBuilder fillCycle(ItemStack itemStack) {
        for (int i = 0; i < 9; ++i)
            setItem(i, itemStack);
        for (int i = getSize() - 9; i < getSize(); ++i)
            setItem(i, itemStack);
        int j = 0;
        for (int i = 0; i < getSize() / 9; ++i) {
            if (i != 0 && i != getSize() - 9)
                setItem(j, itemStack);
            j += 9;
        }
        int k = 8;
        for (int i = 0; i < getSize() / 9; ++i) {
            if (i != 0 && i != getSize() - 9)
                setItem(k, itemStack);
            k += 9;
        }
        return this;
    }

    private void setItem(int index, ItemStack itemStack) {
        itemStackMap.put(index, itemStack);
        if (inventory != null) {
            inventory.setItem(index, itemStack);
            itemStackMap.remove(index);
        }
    }

    @Override
    public IInventoryBuilder fillInventory(ItemStack itemStack) {
        for (int i = 0; i < getSize(); i++)
            if (!itemStackMap.containsKey(i))
                setItem(i, itemStack);
        return this;
    }

    @Override
    public IInventoryBuilder fillInventory(ItemStack itemStack, Integer... slots) {
        for (int slot : slots)
            setItem(slot, itemStack);
        return this;
    }

    @Override
    public IInventoryBuilder fillPageItems(PageData pageData, PageSwitchHandler handler) {
        List<ItemStack> list = Verany.getPageList(pageData.getCurrentPage(), pageData.getSlots().length, pageData.getItems());
        for (int i = 0; i < list.size(); i++)
            setItem(pageData.getSlots()[i], list.get(i));

        setItem(pageData.getPreviousPageItem(), new ItemBuilder(Material.PLAYER_HEAD).setDisplayName("Previous Page").build());
        setItem(pageData.getNextPageItem(), new ItemBuilder(Material.PLAYER_HEAD).setDisplayName("Next Page").build());

        if (pageData.getCurrentPageItem() != -1)
            setItem(pageData.getCurrentPageItem(), new ItemBuilder(Material.PAPER).setDisplayName("Current Page: " + pageData.getCurrentPage()).build());

        pageSwitchHandlers.put(pageData, handler);

        return this;
    }

    /*@Override
    public IInventoryBuilder fillPageItems(PageData pageData, Consumer<PageSwitchHandler.Type> clickConsumer) {
        return fillPageItems(pageData, (PageSwitchHandler) clickConsumer::accept);
    }*/

    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) {
            if (onNullClick != null)
                onNullClick.accept(event);
            return;
        }
        if (this.onClick != null)
            this.onClick.accept(event);
        if (this.event != null)
            this.event.accept(event);

        pageSwitchHandlers.forEach((pageData, handler) -> {
            if (event.getSlot() == pageData.getNextPageItem()) {
                event.setCancelled(true);
                handler.onSwitch(PageSwitchHandler.Type.NEXT);
                pageSwitchHandlers.remove(pageData);
            } else if (event.getSlot() == pageData.getPreviousPageItem()) {
                event.setCancelled(true);
                handler.onSwitch(pageData.getCurrentPage() == 1 ? PageSwitchHandler.Type.FIRST : PageSwitchHandler.Type.PREVIOUS);
                pageSwitchHandlers.remove(pageData);
            }
        });
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if (this.onClose != null)
            this.onClose.accept(event);
    }

    @Override
    public Inventory createAndOpen(Player player) {
        if (inventoryType == null)
            inventory = Bukkit.createInventory(null, size, title);
        else
            inventory = Bukkit.createInventory(null, inventoryType, title);
        itemStackMap.forEach(inventory::setItem);
        itemStackMap.clear();
        Verany.INVENTORY_MAP.put(player, this);
        player.openInventory(inventory);
        return inventory;
    }

    @Override
    @Deprecated
    public Inventory buildAndOpen(Player player) {
        return createAndOpen(player);
    }
}
