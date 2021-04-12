package net.verany.api;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.verany.api.command.CommandEntry;
import net.verany.api.command.executor.VeranyCommandExecutor;
import net.verany.api.event.EventConsumer;
import net.verany.api.gamemode.AbstractGameMode;
import net.verany.api.gamemode.GameModeObject;
import net.verany.api.gamemode.IGameModeObject;
import net.verany.api.gamemode.VeranyGameMode;
import net.verany.api.hotbar.HotbarItem;
import net.verany.api.inventory.IInventoryBuilder;
import net.verany.api.item.VeranyItem;
import net.verany.api.language.EnumLanguage;
import net.verany.api.language.LanguageData;
import net.verany.api.message.MessageData;
import net.verany.api.messaging.VeranyMessenger;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyModule.DatabaseConnection;
import net.verany.api.module.VeranyProject;
import net.verany.api.npc.INPC;
import net.verany.api.npc.NPCObject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.ProfileObject;
import net.verany.api.player.permission.group.AbstractPermissionGroup;
import net.verany.api.player.permission.group.PermissionGroup;
import net.verany.api.plugin.IProfileObject;
import net.verany.api.prefix.AbstractPrefixPattern;
import net.verany.api.prefix.PrefixPattern;
import net.verany.api.redis.RedisManager;
import net.verany.api.redis.redispub.RedisPubSub;
import net.verany.api.world.IWorldObject;
import net.verany.api.world.WorldObject;
import net.verany.volcano.VeranyServer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.JedisPool;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Verany extends AbstractVerany {

    private static final BlockFace[] AXIS = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    private static final BlockFace[] RADIAL = {BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};

    public static final List<HotbarItem> HOTBAR_ITEMS = new CopyOnWriteArrayList<>();
    public static final Map<Player, IInventoryBuilder> INVENTORY_MAP = new ConcurrentHashMap<>();
    public static final List<INPC> NPCS = new CopyOnWriteArrayList<>();
    public static final List<MessageData> MESSAGES = new ArrayList<>();
    public static final String KEY = generate(10);
    public static VeranyMessenger MESSENGER;

    public static final IWorldObject WORLD_OBJECT = new WorldObject();
    public static final IGameModeObject GAME_MODE_OBJECT = new GameModeObject();

    public static final LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build();

    @SneakyThrows
    public static void loadModule(VeranyProject project) {
        VeranyModule module = project.getClass().getAnnotation(VeranyModule.class);

        if (!isLoaded()) {
            PROFILE_OBJECT = new ProfileObject();
            EVENT_REGISTRY.setPlugin(project);
            MESSENGER = new VeranyMessenger(new URI("ws://159.69.63.105:888"));
            MESSENGER.connect();

        }
        load();

        REDIS_MANAGER = new RedisManager(new JedisPool("127.0.0.1"), new RedisPubSub());
        REDIS_MANAGER.load(project);

        DatabaseConnection connection = new DatabaseConnection(module);
        project.setModule(module);
        project.setConnection(connection);

    }

    public static void pushAway(Player player, Location loc) {
        Vector vector = player.getLocation().toVector().subtract(loc.toVector()).normalize().multiply(1).setY(.1);
        if (player.isInsideVehicle()) {
            player.getVehicle().setVelocity(vector);
            return;
        }
        player.setVelocity(vector);
    }

    public static void loadPermissionGroups(VeranyProject project) {
        PermissionGroup.VALUES.clear();
        MongoCollection<Document> collection = project.getConnection().getCollection("rank", "groups");
        for (AbstractPermissionGroup value : PermissionGroup.VALUES)
            if (collection.find(Filters.eq("name", value.getName())).first() == null)
                collection.insertOne(GSON.fromJson(GSON.toJson(value), Document.class));
        for (Document document : collection.find()) {
            AbstractPermissionGroup permissionGroup = GSON.fromJson(document.toJson(), PermissionGroup.class);
            if (PermissionGroup.getGroupByName(permissionGroup.getName()) == null)
                PermissionGroup.VALUES.add(permissionGroup);
        }
        for (AbstractPermissionGroup value : PermissionGroup.VALUES)
            for (AbstractPermissionGroup child : value.getChildren())
                value.getPermissions().addAll(child.getPermissions());
    }

    public static void updatePermissionGroup(VeranyProject project, AbstractPermissionGroup group) {
        project.getConnection().getCollection("rank", "groups").updateOne(new BasicDBObject("name", group.getName()), new BasicDBObject("$set", new BasicDBObject("permissions", group.getPermissions())));
        project.getConnection().getCollection("rank", "groups").updateOne(new BasicDBObject("name", group.getName()), new BasicDBObject("$set", new BasicDBObject("children", group.getStringChildren())));
    }

    public static String getPrefix(String s, AbstractPrefixPattern pattern) {
        boolean hasUpperCaseInMiddle = false;
        int a = 0;

        StringBuilder first = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            if (i != 0 && Character.isUpperCase(s.charAt(i))) {
                hasUpperCaseInMiddle = true;
                break;
            } else {
                first.append(s.charAt(i));
                a++;
            }

        StringBuilder second = new StringBuilder();
        if (hasUpperCaseInMiddle)
            for (int i = a; i < s.length(); i++)
                second.append(s.charAt(i));

        return format(pattern.getPattern(), first.toString(), second.toString());
    }

    public static String getPrefix(String s) {
        return getPrefix(s, PrefixPattern.BLUE);
    }

    public static <T> List<T> getListFrom(int from, int to, List<T> objects) {
        List<T> toReturn = new ArrayList<>();
        for (int i = from; i < to; i++)
            if (objects.size() > i)
                toReturn.add(objects.get(i));
        return toReturn;
    }

    public static <T> List<T> getListFrom(int from, int to, T[] objects) {
        List<T> toReturn = new ArrayList<>();
        for (int i = from; i < to; i++)

            if (objects.length > i)
                toReturn.add(objects[i]);
        return toReturn;
    }

    public static <T> List<T> getPageList(int page, int slots, List<T> objects) {
        List<T> toReturn = new ArrayList<>();
        int from = (page == 1 ? 0 : (page - 1) * slots);
        int to = (page == 1 ? slots : page * slots);
        for (int i = from; i < to; i++)
            if (objects.size() > i)
                toReturn.add(objects.get(i));
        return toReturn;
    }

    public static <T> List<T> getListFrom(int from, int to, Collection<? extends T> objects) {
        List<T> toReturn = new ArrayList<>();
        for (int i = from; i < to; i++)
            if (objects.size() > i)
                toReturn.add(Iterables.get(objects, i));
        return toReturn;
    }

    public static <T> List<T> getPageList(int page, int slots, Collection<? extends T> objects) {
        List<T> toReturn = new ArrayList<>();
        int from = (page == 1 ? 0 : (page - 1) * slots);
        int to = (page == 1 ? slots : page * slots);
        for (int i = from; i < to; i++)
            if (objects.size() > i)
                toReturn.add(Iterables.get(objects, i));
        return toReturn;
    }

    public static List<ArmorStand> spawnArmorStand(Location location, String... textLines) {
        List<ArmorStand> toReturn = new ArrayList<>();
        for (String textLine : textLines) {
            ArmorStand armorStand = location.getWorld().spawn(location.clone().subtract(0, toReturn.size() * 0.25, 0), ArmorStand.class);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(textLine);
            toReturn.add(armorStand);
        }
        return toReturn;
    }

    public static <T extends Event> void registerListener(Plugin plugin, Class<T> tClass, EventConsumer<T> eventConsumer) {
        registerListener(plugin, tClass, eventConsumer, EventPriority.NORMAL);
    }

    public static <T extends Event> void registerListener(Plugin plugin, Class<T> tClass, EventConsumer<T> eventConsumer, EventPriority priority) {
        Bukkit.getPluginManager().registerEvent(tClass, eventConsumer, priority, (listener, event) -> eventConsumer.call((T) event), plugin);
    }

    public static void registerCommand(@NonNull VeranyProject project, @NonNull CommandEntry commandEntry, @NonNull VeranyCommandExecutor executor) {
        PluginCommand pluginCommand = project.getCommand(commandEntry.getName());
        if (pluginCommand == null) return;

        pluginCommand.setExecutor((commandSender, command, s, strings) -> {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                executor.onExecute(getPlayer(player), strings);
            }
            return false;
        });

        pluginCommand.setPermission(commandEntry.getPermission());

        if (commandEntry.getTabCompleter() != null)
            pluginCommand.setTabCompleter(commandEntry.getTabCompleter());
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

    public static INPC createNPC(String name, Location location, boolean removeTablist, Player player) {
        return new NPCObject((CraftServer) Bukkit.getServer(), name, location, removeTablist, player);
    }

    public static INPC createNPC(String name, Location location, Player player) {
        return createNPC(name, location, true, player);
    }

    public static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections) {
        if (useSubCardinalDirections)
            return RADIAL[Math.round(yaw / 45f) & 0x7].getOppositeFace();

        return AXIS[Math.round(yaw / 90f) & 0x3].getOppositeFace();
    }

    public static String serializeHex(String text) {
        return serializer.serialize(TextComponent.ofChildren(MiniMessage.get().parse(text)));
    }

    public static List<VeranyItem> toVeranyItem(List<ItemStack> itemStacks) {
        List<VeranyItem> toReturn = new ArrayList<>();
        for (ItemStack itemStack : itemStacks)
            toReturn.add(VeranyItem.fromItemStack(itemStack));
        return toReturn;
    }

    public static List<IPlayerInfo> getOnlinePlayers() {
        List<IPlayerInfo> toReturn = new ArrayList<>();
        for (IPlayerInfo registeredPlayer : PROFILE_OBJECT.getRegisteredPlayers())
            if (registeredPlayer.getPlayer() != null && registeredPlayer.getPlayer().isOnline())
                toReturn.add(registeredPlayer);
        return toReturn;
    }

    public static void reloadMessages(VeranyProject project) {
        MESSAGES.clear();
        loadMessages(project);
    }

    private static void loadMessages(VeranyProject project) {
        System.out.println("Loading messages...");
        long current = System.currentTimeMillis();
        for (Document document : project.getConnection().getCollection("network", "messages").find()) {
            String key = document.getString("key");
            List<LanguageData> languageData = new ArrayList<>();
            for (EnumLanguage value : EnumLanguage.values())
                languageData.add(new LanguageData(value, document.getString(value.name().toLowerCase())));
            MESSAGES.add(new MessageData(key, languageData));
        }
        System.out.println("Loading messages complete. (" + MESSAGES.size() + " - " + (System.currentTimeMillis() - current) + "ms)");
    }

    public static void registerGameMode(VeranyProject project, AbstractGameMode gameMode) {
        VeranyServer.registerGameMode(project, gameMode);
    }

    public static void createMessage(VeranyProject project, String key) {
        Document document = new Document("key", key);
        for (EnumLanguage value : EnumLanguage.values())
            document.append(value.name().toLowerCase(), key);
        project.getConnection().getCollection("network", "messages").insertOne(document);
    }

    public static FindIterable<Document> getDocuments(VeranyProject project, String collection, String database, int limit, String... fieldNames) {
        return project.getConnection().getCollection(database, collection).find().sort(Sorts.descending(fieldNames)).limit(limit);
    }

    public static FindIterable<Document> getDocuments(VeranyProject project, String collection, int limit, String... fieldNames) {
        return getDocuments(project, collection, project.getConnection().getDatabaseManagers().get(0).getDatabaseName(), limit, fieldNames);
    }

    public static int getRanking(VeranyProject project, UUID uuid, String collection, String database, String... fieldNames) {
        int i = 1;

        FindIterable<Document> online = project.getConnection().getCollection(database, collection).find().sort(Sorts.descending(fieldNames));
        for (Document doc : online) {
            if (doc.getString("uuid").equals(uuid.toString()))
                break;
            i++;
        }
        return i;
    }

    public static String getMessage(String key, EnumLanguage language) {
        for (MessageData message : MESSAGES)
            if (message.getKey().equals(key))
                for (LanguageData languageData : message.getLanguageDataList())
                    if (languageData.getLanguage().equals(language))
                        return languageData.getMessage();
        return null;
    }

    public static <T> List<T> sortList(List<SortData<T>> list, boolean reverse) {
        List<T> toReturn = new ArrayList<>();
        for (SortData<T> tSortData : list.stream().sorted(Comparator.comparing(SortData::getKey)).collect(Collectors.toList()))
            toReturn.add(tSortData.getT());
        if (reverse)
            Collections.reverse(toReturn);
        return toReturn;
    }

    public static <T> List<T> sortList(List<T> list, Comparator<T> comparator) {
        return list.stream().sorted(comparator).collect(Collectors.toList());
    }

    /*private static <T> T getSortData(String key, String identifier, List<SortData<T>> sortData) {
        for (SortData<T> sortDatum : sortData)
            if (sortDatum.getKey().equalsIgnoreCase(key))
                return sortDatum.getT();
        return null;
    }*/

    public static Color hexToColor(int hex) {
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);
        return Color.fromBGR(r, g, b);
    }

    @AllArgsConstructor
    @Getter
    public static class SortData<T> {
        private final String key;
        private final T t;
    }

}
