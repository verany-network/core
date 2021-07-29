package net.verany.api;

import com.google.common.base.Strings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.verany.api.command.CommandEntry;
import net.verany.api.command.executor.VeranyCommandExecutor;
import net.verany.api.event.EventRegistry;
import net.verany.api.hotbar.HotbarItem;
import net.verany.api.inventory.IInventoryBuilder;
import net.verany.api.item.VeranyItem;
import net.verany.api.language.AbstractLanguage;
import net.verany.api.language.LanguageData;
import net.verany.api.loader.Loader;
import net.verany.api.message.MessageData;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.IVeranyPlayer;
import net.verany.api.setup.AbstractSetupObject;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Verany extends AbstractVerany {

    private static final BlockFace[] AXIS = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    private static final BlockFace[] RADIAL = {BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};

    public static final List<HotbarItem> HOTBAR_ITEMS = new CopyOnWriteArrayList<>();
    public static final EventRegistry EVENT_REGISTRY = new EventRegistry();
    public static final Map<Player, IInventoryBuilder> INVENTORY_MAP = new ConcurrentHashMap<>();
    public static final List<AbstractSetupObject> SETUP_OBJECTS = new CopyOnWriteArrayList<>();

    public static final LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();

    public static void loadModule(VeranyPlugin project, Runnable onSocketConnect) {
        VeranyModule module = project.getClass().getAnnotation(VeranyModule.class);

        VeranyModule.DatabaseConnection connection = new VeranyModule.DatabaseConnection(module);
        project.setModule(module);
        project.setConnection(connection);

        AbstractVerany.SOCKET_OPEN.add(onSocketConnect);

    }

    public static void shutdown(VeranyProject project) {
        Bukkit.getOnlinePlayers().forEach(player -> player.kick(Component.text("Shutdown")));
        LOADERS.forEach(Loader::save);
        project.getConnection().disconnect();
    }

    public static void registerCommand(@NonNull VeranyPlugin project, @NonNull CommandEntry commandEntry, @NonNull VeranyCommandExecutor executor) {
        PluginCommand pluginCommand = project.getCommand(commandEntry.getName());
        if (pluginCommand == null) return;

        pluginCommand.setExecutor((commandSender, command, s, strings) -> {
            if (commandSender instanceof Player player)
                executor.onExecute(getPlayer(player), strings);
            return false;
        });

        pluginCommand.setPermission(commandEntry.getPermission());
        pluginCommand.setAliases(commandEntry.getAliases());

        if (commandEntry.getTabCompleter() != null)
            pluginCommand.setTabCompleter(commandEntry.getTabCompleter());
    }


    public static IPlayerInfo getPlayer(UUID uuid) {
        return PROFILE_OBJECT.getPlayer(uuid, IPlayerInfo.class).orElse(null);
    }

    public static IPlayerInfo getPlayer(String name) {
        return PROFILE_OBJECT.getPlayer(name, IPlayerInfo.class).orElse(null);
    }

    public static IPlayerInfo getPlayer(Player player) {
        return PROFILE_OBJECT.getPlayer(player.getUniqueId(), IPlayerInfo.class).orElse(null);
    }

    public static double distance(Location location1, Location location2) {
        if (!location1.getWorld().equals(location2.getWorld())) return -1;
        return location1.distance(location2);
    }

    public static String getProgressBar(int current, int max, int totalBars, char symbol, org.bukkit.ChatColor completedColor, org.bukkit.ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat("" + completedColor + symbol, progressBars) + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }

    public static List<HotbarItem> getHotbarItem(Player player) {
        List<HotbarItem> toReturn = new ArrayList<>();
        for (HotbarItem hotbarItem : HOTBAR_ITEMS)
            if (hotbarItem.getPlayer().equals(player))
                toReturn.add(hotbarItem);
        return toReturn;
    }

    public static void pushAway(Player player, Location loc) {
        Vector vector = player.getLocation().toVector().subtract(loc.toVector()).normalize().multiply(1).setY(.1);
        if (player.isInsideVehicle()) {
            player.getVehicle().setVelocity(vector);
            return;
        }
        player.setVelocity(vector);
    }

    public static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
        if (useSubCardinalDirections)
            return RADIAL[Math.round(yaw / 45f) & 0x7].getOppositeFace();

        return AXIS[Math.round(yaw / 90f) & 0x3].getOppositeFace();
    }

    public static List<VeranyItem> toVeranyItem(List<ItemStack> itemStacks) {
        List<VeranyItem> toReturn = new ArrayList<>();
        for (ItemStack itemStack : itemStacks)
            toReturn.add(VeranyItem.fromItemStack(itemStack));
        return toReturn;
    }

    public static List<IPlayerInfo> getOnlinePlayers() {
        List<IPlayerInfo> toReturn = new ArrayList<>();
        for (IPlayerInfo registeredPlayer : PROFILE_OBJECT.getRegisteredPlayers(IPlayerInfo.class))
            if (registeredPlayer.getPlayer() != null && registeredPlayer.getPlayer().isOnline())
                toReturn.add(registeredPlayer);
        return toReturn;
    }

    @SneakyThrows
    private static void loadMessages(VeranyProject project) {
        System.out.println("Loading messages...");
        long current = System.currentTimeMillis();
        for (Document document : project.getConnection().getCollection("network", "messages").find()) {
            String key = document.getString("key");
            List<LanguageData> languageData = new ArrayList<>();
            for (AbstractLanguage language : Verany.LANGUAGES) {
                languageData.add(new LanguageData(language, document.getString(language.getName())));
            }
            Verany.MESSAGES.add(new MessageData(key, languageData));
        }
        System.out.println("Loading messages complete. (" + Verany.MESSAGES.size() + " - " + (System.currentTimeMillis() - current) + "ms)");
    }

    private static void loadLanguages(VeranyProject project) {
        System.out.println("Loading languages...");
        long current = System.currentTimeMillis();
        MongoCollection<Document> collection = project.getConnection().getCollection("network", "languages");
        for (AbstractLanguage language : Verany.LANGUAGES) {
            if (collection.find(Filters.eq("name", language.getName())).first() != null || !language.isEnabled())
                continue;
            String json = Verany.GSON.toJson(language);
            collection.insertOne(Verany.GSON.fromJson(json, Document.class));
        }
        for (Document document : collection.find()) {
            if (AbstractLanguage.getLanguage(document.getString("name")).isPresent() || !document.getBoolean("enabled"))
                continue;
            AbstractLanguage language = Verany.getLanguage(document);
            System.out.println("registered new language " + language.getName());
        }
        System.out.println("Loading languages complete. (" + Verany.LANGUAGES.size() + " - " + (System.currentTimeMillis() - current) + "ms)");
    }

    public static void reloadMessages(VeranyProject project) {
        Verany.MESSAGES.clear();
        loadLanguages(project);
        loadMessages(project);
    }


    public static String serializeHex(String text) {
        return serializer.serialize(TextComponent.ofChildren(MiniMessage.get().parse(text)));
    }

}
