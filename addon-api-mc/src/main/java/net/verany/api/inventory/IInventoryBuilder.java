package net.verany.api.inventory;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public interface IInventoryBuilder {

    IInventoryBuilder fillCycle(ItemStack itemStack);

    IInventoryBuilder fillInventory(ItemStack itemStack);

    IInventoryBuilder fillInventory(ItemStack itemStack, Integer... slots);

    <T extends ItemStack> IInventoryBuilder fillPageItems(PageData<T> pageData, PageSwitchHandler handler);

    Inventory getInventory();

    void onClick(InventoryClickEvent event);

    Consumer<InventoryClickEvent> getClickConsumer();

    Consumer<InventoryClickEvent> getNullClickConsumer();

    Inventory buildAndOpen(Player player);

    @Getter
    class PageData<T extends ItemStack> {
        private final int currentPage;
        private final Integer[] slots;
        private final int nextPageItem;
        private final int previousPageItem;
        private final List<T> items;
        private int currentPageItem = -1;

        public PageData(int currentPage, Integer[] slots, int nextPageItem, int previousPageItem, List<T> items) {
            this.currentPage = currentPage;
            this.slots = slots;
            this.nextPageItem = nextPageItem;
            this.previousPageItem = previousPageItem;
            this.items = items;
        }

        public PageData(int currentPage, Integer[] slots, int nextPageItem, int previousPageItem, List<T> items, int currentPageItem) {
            this.currentPage = currentPage;
            this.slots = slots;
            this.nextPageItem = nextPageItem;
            this.previousPageItem = previousPageItem;
            this.items = items;
            this.currentPageItem = currentPageItem;
        }
    }

    abstract class PageSwitchHandler {
        public abstract void onSwitch(Type type);

        public enum Type {
            NEXT,
            PREVIOUS,
            FIRST,
            LAST
        }
    }

}
