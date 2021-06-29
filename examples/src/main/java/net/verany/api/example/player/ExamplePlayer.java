package net.verany.api.example.player;

import net.verany.api.Verany;
import net.verany.api.example.inventory.ExampleInventory;
import net.verany.api.example.player.setting.ExampleSetting;
import net.verany.api.hotbar.HotbarItem;
import net.verany.api.itembuilder.ItemBuilder;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.scoreboard.IScoreboardBuilder;
import net.verany.api.scoreboard.ScoreboardBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class ExamplePlayer extends DatabaseLoader implements IExamplePlayer {

    private UUID uniqueId;
    private IPlayerInfo playerInfo;
    private Player player;

    public ExamplePlayer(VeranyProject project) {
        super(project, "collection", "database");
    }

    @Override
    public void load(UUID key) {
        uniqueId = key;
        playerInfo = Verany.getPlayer(key);
        player = playerInfo.getPlayer();

        // Load Object from Database
        load(new LoadInfo<>("player", ExamplePlayerObject.class, new ExamplePlayerObject(key.toString(), "Max Mustermann", 21)));
    }

    @Override
    public void update() {
        // Update Object to Database
        save("player");
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public void setItems() {

        boolean settingVal = playerInfo.getSettingValue(ExampleSetting.EXAMPLE_SETTING);

        if (settingVal) {
            int amount = playerInfo.getSettingValue(ExampleSetting.EXAMPLE_SETTING_1);

            playerInfo.setItem(0, new HotbarItem(new ItemBuilder(Material.IRON_SWORD).setAmount(amount).setDisplayName("Iron Sword").setUnbreakable(true), player) {
                @Override
                public void onClick(InventoryClickEvent event) {
                    event.setCancelled(true);
                }

                @Override
                public void onDrop(PlayerDropItemEvent event) {
                    event.setCancelled(true);
                }

                @Override
                public void onInteract(PlayerInteractEvent event) {
                    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                        new ExampleInventory(playerInfo).setItems();
                    }
                }
            });
        }

        playerInfo.setSettingValue(ExampleSetting.EXAMPLE_SETTING, true);
        playerInfo.setSettingValue(ExampleSetting.EXAMPLE_SETTING_1, 5);
    }

    @Override
    public void setScoreboard() {
        IScoreboardBuilder scoreboardBuilder = new ScoreboardBuilder(player);
        scoreboardBuilder.setTitle("Scoreboard Title");
        scoreboardBuilder.setSlot(2, " ");
        scoreboardBuilder.setSlot(1, "Scoreboard Text");
        scoreboardBuilder.setSlot(0, " ");
    }

    @Override
    public void setName(String name) {
        if (getDataOptional(ExamplePlayerObject.class).isEmpty()) return;
        getDataOptional(ExamplePlayerObject.class).get().setName(name);
    }

    @Override
    public void setAge(int age) {
        if (getDataOptional(ExamplePlayerObject.class).isEmpty()) return;
        getDataOptional(ExamplePlayerObject.class).get().setAge(age);
    }

    @Override
    public String getName() {
        if (getDataOptional(ExamplePlayerObject.class).isEmpty()) return null;
        return getDataOptional(ExamplePlayerObject.class).get().getName();
    }

    @Override
    public int getAge() {
        if (getDataOptional(ExamplePlayerObject.class).isEmpty()) return -1;
        return getDataOptional(ExamplePlayerObject.class).get().getAge();
    }

}
