package net.verany.executor.commands;

import net.verany.api.Verany;
import net.verany.api.inventory.InventoryBuilder;
import net.verany.api.itembuilder.ItemBuilder;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.prefix.AbstractPrefixPattern;
import net.verany.api.prefix.PrefixPattern;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class PrefixCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        openInventory(player);

        return false;
    }

    private void openInventory(Player player) {
        IPlayerInfo playerInfo = Verany.PROFILE_OBJECT.getPlayer(player.getUniqueId()).get();

        Integer[] slots = {9, 10, 11, 12, 13, 14, 15, 16, 17};

        Inventory inventory = InventoryBuilder.builder().size(9 * 3).title(playerInfo.getKey("core.prefix.selection")).event(event -> {
            event.setCancelled(true);

            for (int i = 0; i < slots.length; i++) {
                int slot = slots[i];
                if (event.getSlot() == slot) {
                    AbstractPrefixPattern prefixPattern = PrefixPattern.VALUES.get(i);
                    playerInfo.setPrefixPattern(prefixPattern);
                    playerInfo.sendKey(Verany.getPrefix("CoreExecutor", playerInfo.getPrefixPattern()), "core.prefix.selected", new Placeholder("%name%", playerInfo.getKey("core.prefix." + prefixPattern.getKey().toLowerCase())));
                    openInventory(player);
                    break;
                }
            }
        }).build().buildAndOpen(player);

        for (int i = 0; i < PrefixPattern.VALUES.size(); i++) {
            AbstractPrefixPattern prefixPattern = PrefixPattern.VALUES.get(i);
            inventory.setItem(slots[i], new ItemBuilder(Material.valueOf(Verany.toDyeColor(prefixPattern.getColor().getFirstColor()) + "_STAINED_GLASS_PANE")).setGlow(prefixPattern.equals(playerInfo.getPrefixPattern())).setDisplayName(prefixPattern.getExample()).addLoreArray(playerInfo.getKeyArray("core.prefix.select", "~")).build());
        }

        for (Integer slot : slots) {
            inventory.setItem(slot - 9, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
            inventory.setItem(slot + 9, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
        }

    }
}
