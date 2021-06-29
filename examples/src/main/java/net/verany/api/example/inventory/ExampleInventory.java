package net.verany.api.example.inventory;

import net.verany.api.example.ExamplePlugin;
import net.verany.api.inventory.InventoryBuilder;
import net.verany.api.itembuilder.ItemBuilder;
import net.verany.api.message.KeyBuilder;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.skull.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.SimpleDateFormat;

public class ExampleInventory {

    private final Inventory inventory;

    private BukkitTask task;

    public ExampleInventory(IPlayerInfo playerInfo) {
        Player player = playerInfo.getPlayer();
        this.inventory = InventoryBuilder.builder().
                title(playerInfo.getKey("inventory.title", new Placeholder("%name%", player.getName()))).
                size(9 * 3).
                onClick(event ->
                        {
                            KeyBuilder timeKey = KeyBuilder.builder().
                                    key("current.time").
                                    placeholders(new Placeholder[]
                                            {
                                                    new Placeholder("%time%", getTime())
                                            }
                                    ).
                                    prefix(playerInfo.getPrefix("CurrentTime")).
                                    build();

                            playerInfo.sendKey(timeKey);
                        }
                ).
                onClose(event ->
                        task.cancel()
                ).
                build().
                fillInventory(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build()).
                createAndOpen(player);

        inventory.setItem(4, new SkullBuilder(playerInfo.getSkinData()).setDisplayName(playerInfo.getNameWithColor()).build());

        startTask();
    }

    public void setItems() {
        inventory.setItem(13,
                new ItemBuilder(Material.CLOCK).
                        setDisplayName("Uhrzeit").
                        addLoreArray(
                                "§7Es ist §b" + getTime() + " §7Uhr"
                        ).
                        build()
        );
    }

    private void startTask() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                setItems();
            }
        }.runTaskTimer(ExamplePlugin.INSTANCE, 0, 20);
    }

    private String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis());
    }
}
