package net.verany.api.inventory;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public interface IInventoryBuilder {

    IInventoryBuilder fillCycle(ItemStack itemStack);

    IInventoryBuilder fillInventory(ItemStack itemStack);

    IInventoryBuilder fillInventory(ItemStack itemStack, Integer... slots);

    IInventoryBuilder fillPageItems(PageData pageData, PageSwitchHandler handler);

    //IInventoryBuilder fillPageItems(PageData pageData, Consumer<PageSwitchHandler.Type> clickConsumer);

    Inventory getInventory();

    void onClick(InventoryClickEvent event);

    void onClose(InventoryCloseEvent event);

    Consumer<InventoryClickEvent> getOnClick();

    Consumer<InventoryCloseEvent> getOnClose();

    Consumer<InventoryClickEvent> getOnNullClick();

    Inventory createAndOpen(Player player);

    @Deprecated
    Inventory buildAndOpen(Player player);

    @Getter
    class PageData {
        private final int currentPage;
        private final Integer[] slots;
        private final int nextPageItem;
        private final int previousPageItem;
        private final List<ItemStack> items;
        private int currentPageItem = -1;

        public PageData(int currentPage, Integer[] slots, int nextPageItem, int previousPageItem, List<ItemStack> items) {
            this.currentPage = currentPage;
            this.slots = slots;
            this.nextPageItem = nextPageItem;
            this.previousPageItem = previousPageItem;
            this.items = items;
        }

        public PageData(int currentPage, Integer[] slots, int nextPageItem, int previousPageItem, List<ItemStack> items, int currentPageItem) {
            this.currentPage = currentPage;
            this.slots = slots;
            this.nextPageItem = nextPageItem;
            this.previousPageItem = previousPageItem;
            this.items = items;
            this.currentPageItem = currentPageItem;
        }
    }

    interface PageSwitchHandler {
        void onSwitch(Type type);

        enum Type {
            NEXT,
            PREVIOUS,
            FIRST,
            LAST
        }
    }

}
