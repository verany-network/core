package net.verany.api.setup.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.verany.api.Verany;
import net.verany.api.chat.request.ChatRequest;
import net.verany.api.chat.request.ChatRequestCallback;
import net.verany.api.chat.request.StringChatRequest;
import net.verany.api.enumhelper.EnumHelper;
import net.verany.api.enumhelper.IdentifierType;
import net.verany.api.inventory.InventoryBuilder;
import net.verany.api.itembuilder.ItemBuilder;
import net.verany.api.locationmanager.VeranyLocation;
import net.verany.api.map.data.MapEntry;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.setup.AbstractSetupObject;
import net.verany.api.setup.category.AbstractSetupCategory;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class SetupInventory {

    private final Integer[] mainSlots = {10, 12, 14, 16};

    private final VeranyPlugin plugin;
    private final AbstractSetupObject setupObject;
    private final Player player;

    public SetupInventory(VeranyPlugin plugin, AbstractSetupObject setupObject, Player player) {
        this.plugin = plugin;
        this.setupObject = setupObject;
        this.player = player;
    }

    public void setMainItems() {
        Inventory inventory = InventoryBuilder.builder().size(9 * 3).title("Setup").onClick(event -> {
            event.setCancelled(true);

            SetupCategory category = EnumHelper.INSTANCE.valueOf(event.getCurrentItem().getType(), SetupCategory.values());
            if (category != null) {
                switch (category) {
                    case TELEPORT_ON_BLOCK -> {
                        Location location = player.getLocation().getBlock().getLocation();
                        location.add(0.5, 0, 0.5);
                        location.setPitch(0);
                        float yaw = getClosestToTarget(player.getLocation().getYaw(), 0, 90, -180, -90);
                        location.setYaw(yaw);
                        player.teleport(location);
                    }
                    case CATEGORIES -> setCategoryItems();
                    case WORLDS -> setWorldItems();
                    case SAVE_LOCATIONS -> {
                        setupObject.saveLocations();
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    }
                }
            }
        }).build().fillCycle(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build()).createAndOpen(player);

        for (int i = 0; i < mainSlots.length; i++) {
            SetupCategory category = SetupCategory.values()[i];
            inventory.setItem(mainSlots[i], new ItemBuilder(category.getId()).setDisplayName(category.name()).build());
        }
    }

    private float getClosestToTarget(float myNumber, float... numbers) {
        float distance = Math.abs(numbers[0] - myNumber);
        int idx = 0;
        for (int c = 1; c < numbers.length; c++) {
            float cdistance = Math.abs(numbers[c] - myNumber);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
        return numbers[idx];
    }

    public void setWorldItems() {
        Inventory inventory = InventoryBuilder.builder().size(9 * 6).title("Worlds").onClick(event -> {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(Material.CLAY_BALL)) {
                setMainItems();
                return;
            }

            if (event.getSlot() == 49) {
                ChatRequest<String> request = new StringChatRequest("Gebe den Namen der Welt in den nächsten 30 Sekunden in den Chat ein!", 30 * 1000, 1);
                Verany.getPlayer(player.getUniqueId()).requestChatMessage(plugin, request, new ChatRequestCallback() {
                    @Override
                    public void accept(FinishType onFinish) {
                        if (onFinish.equals(FinishType.SUCCESS)) {
                            String name = request.getValue();
                            player.sendMessage("Importiere " + name + "...");
                            Bukkit.createWorld(new WorldCreator(name));
                            player.sendMessage(name + " wurde importiert.");
                            setWorldItems();
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        } else {
                            setWorldItems();
                            player.sendMessage("§cIrgendetwas ist schief gelaufen!");
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
                        }
                    }
                });
                return;
            }

            player.closeInventory();
            String mapName = PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName());
            player.sendMessage("Loading map " + mapName + "...");
            World world = Bukkit.getWorld(mapName);
            if (world == null) world = Bukkit.createWorld(new WorldCreator(mapName));
            player.teleport(world.getSpawnLocation());
            player.setGameMode(GameMode.CREATIVE);
            player.setFlying(true);

        }).build().fillCycle(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build()).createAndOpen(player);

        List<MapEntry> maps = new ArrayList<>();

        if (setupObject.getMapObject() != null) {
            maps = setupObject.getMapObject().getMaps()/*.stream().filter(mapEntry -> !mapEntry.isEnabled()).collect(Collectors.toList())*/;
            for (MapEntry map : setupObject.getMapObject().getMaps()) {
                if (map.isEnabled()) {
                    inventory.addItem(new ItemBuilder(map.getMaterial()).setDisplayName(map.getName()).addLoreArray(" ", "§7Map Type: " + map.getMapType(), "§7Builder:").addLoreAll(map.getBuilder()).build());
                    maps.remove(map);
                }
            }
        }

        for (World world : Bukkit.getWorlds()) {
            inventory.addItem(new ItemBuilder(Material.COARSE_DIRT).setDisplayName(world.getName()).setGlow(player.getWorld().getName().equals(world.getName())).addLoreArray(" ", "§7Bukkit loaded World").build());
        }

        for (MapEntry map : maps) {
            inventory.addItem(new ItemBuilder(map.getMaterial()).setDisplayName(map.getName()).addLoreArray("§cDisabled", " ", "§7Map Type: " + map.getMapType(), "§7Builder:").addLoreAll(map.getBuilder()).build());
        }

        inventory.setItem(45, new ItemBuilder(Material.CLAY_BALL).setDisplayName("Back").build());

        inventory.setItem(49, new ItemBuilder(Material.HOPPER).setDisplayName("Import map").build());
    }

    public void setCategoryItems() {
        Inventory inventory = InventoryBuilder.builder().size(9 * 3).title("Categories").onClick(event -> {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(Material.CLAY_BALL)) {
                setMainItems();
                return;
            }

            String name = PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName());
            AbstractSetupCategory category = setupObject.getCategory(name);
            if (category != null) {
                setLocationsItems(name);
            }
        }).build().fillCycle(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build()).createAndOpen(player);

        /*setupObject.getCategories().forEach((name, category) -> {
            inventory.addItem(new ItemBuilder(category.getMaterial()).setDisplayName(name).build());
        });*/

        for (AbstractSetupCategory category : setupObject.getCategories()) {
            inventory.addItem(new ItemBuilder(category.getMaterial()).setDisplayName(category.getName()).build());
        }

        inventory.setItem(18, new ItemBuilder(Material.CLAY_BALL).setDisplayName("Back").build());
    }

    public void setLocationsItems(String categoryName) {
        AbstractSetupCategory category = setupObject.getCategory(categoryName);

        Inventory inventory = InventoryBuilder.builder().size(9 * 6).title("Locations | " + categoryName).onClick(event -> {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(Material.CLAY_BALL)) {
                setCategoryItems();
                return;
            }

            /*if (!player.hasMetadata("current_world")) {
                if (event.getCurrentItem().getType().equals(Material.GREEN_DYE)) {
                    ChatRequest<String> request = new StringChatRequest("Gebe innerhalb der nächsten 30 Sekunden den Namen der Welt ein, die du einrichten möchtest!", 30 * 1000, 1);
                    Verany.getPlayer(player.getUniqueId()).requestChatMessage(plugin, request, new ChatRequestCallback() {
                        @Override
                        public void accept(FinishType onFinish) {
                            if (onFinish.equals(FinishType.SUCCESS)) {
                                plugin.setMetadata(player, "current_world", request.getValue());
                                player.sendMessage("Du richtest nun in " + request.getValue() + " ein.");
                            }
                        }
                    });
                    return;
                }
                String world = PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName());
                if (setupObject.getMapObject() == null) return;
                MapEntry mapEntry = setupObject.getMapObject().getMap(world);
                if (mapEntry == null) return;
                plugin.setMetadata(player, "current_world", mapEntry.getName());
                player.sendMessage("Du richtest nun in " + mapEntry.getName() + " ein.");
                return;
            }*/

            AbstractSetupCategory.LocationData locationData = category.getLocation(PlainTextComponentSerializer.plainText().serialize(event.getCurrentItem().getItemMeta().displayName()));
            if (locationData == null) {
                player.sendMessage("Das hat nicht funktioniert");
                return;
            }

            locationData.setLocation(VeranyLocation.fromBukkit(player.getLocation()));
            player.sendMessage("Location " + locationData.getName() + " has been set!");

            setLocationsItems(categoryName);
        }).build().fillCycle(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build()).createAndOpen(player);

        /*if (!player.hasMetadata("current_world")) {
            if (setupObject.getMapObject() != null) {
                for (MapEntry map : setupObject.getMapObject().getMaps(category.getWorlds())) {
                    inventory.addItem(new ItemBuilder(map.getMaterial()).setDisplayName(map.getName()).build());
                }
                inventory.addItem(new ItemBuilder(Material.GREEN_DYE).setDisplayName("§aNeue Map auswählen").build());
            }
        } else {*/
            for (AbstractSetupCategory.LocationData location : category.getLocations()) {
                inventory.addItem(new ItemBuilder(location.getMaterial()).setDisplayName(location.getName()).setGlow(!location.getLocation().isEmpty()).build());
            }
        //}

        inventory.setItem(45, new ItemBuilder(Material.CLAY_BALL).setDisplayName("Back").build());
    }

    @AllArgsConstructor
    @Getter
    public enum SetupCategory implements IdentifierType<Material> {
        TELEPORT_ON_BLOCK(Material.ENDER_PEARL),
        WORLDS(Material.GRASS_BLOCK),
        CATEGORIES(Material.BOOK),
        SAVE_LOCATIONS(Material.LIME_DYE);

        private final Material id;
    }

}
