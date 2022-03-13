package net.verany.api.player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.executor.PlayerExecutor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.TextComponent;
import net.verany.api.Verany;
import net.verany.api.achievement.AchievementQueue;
import net.verany.api.achievements.VeranyAchievement;
import net.verany.api.actionbar.AbstractActionbar;
import net.verany.api.actionbar.NumberActionbar;
import net.verany.api.bossbar.AbstractBossBar;
import net.verany.api.chat.request.ChatRequest;
import net.verany.api.chat.request.ChatRequestCallback;
import net.verany.api.event.events.PlayerLanguageUpdateEvent;
import net.verany.api.event.events.PlayerPrefixUpdateEvent;
import net.verany.api.hotbar.HotbarItem;
import net.verany.api.interfaces.IDefault;
import net.verany.api.inventory.IInventoryBuilder;
import net.verany.api.language.AbstractLanguage;
import net.verany.api.language.EnumLanguage;
import net.verany.api.language.LanguageWrapper;
import net.verany.api.language.VeranyLanguage;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.locationmanager.VeranyLocation;
import net.verany.api.message.AbstractComponentBuilder;
import net.verany.api.message.KeyBuilder;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.module.VeranyProject;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.afk.AfkObject;
import net.verany.api.player.afk.IAFKObject;
import net.verany.api.player.ban.IBanEntry;
import net.verany.api.player.friend.FriendObject;
import net.verany.api.player.friend.IFriendObject;
import net.verany.api.player.leveling.CreditsObject;
import net.verany.api.player.leveling.ICreditsObject;
import net.verany.api.player.leveling.ILevelObject;
import net.verany.api.player.leveling.LevelObject;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.api.player.permission.PermissionObject;
import net.verany.api.player.verification.VerificationObject;
import net.verany.api.player.verifictation.IVerificationObject;
import net.verany.api.plugin.IVeranyPlugin;
import net.verany.api.prefix.AbstractPrefixPattern;
import net.verany.api.prefix.PrefixPattern;
import net.verany.api.settings.AbstractSetting;
import net.verany.api.skin.AbstractSkinData;
import net.verany.api.skin.SkinData;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Getter
@Setter
public class PlayerInfo extends DatabaseLoader implements IPlayerInfo {

    private final String name;

    private UUID uniqueId;
    private IVeranyPlugin plugin;

    private Player player = null;
    private IBanEntry banEntry;
    private IPermissionObject permissionObject;
    private ICreditsObject creditsObject;
    private ILevelObject levelObject;
    private IFriendObject friendObject;
    private IVerificationObject verificationObject;
    private IAFKObject afkObject;

    private ICloudPlayer cloudPlayer;
    private PlayerExecutor playerExecutor;

    private String defaultActionbar = null;
    private AbstractBossBar defaultBossBar = null;
    private AbstractSkinData skinData;
    private BossBar bossBar = null;

    private long currentActionbarTime = -1;
    private long lastActionbarTime = -1;

    private final AchievementQueue achievementQueue;

    private final Map<String, Integer> pageMap = new HashMap<>();
    private final Map<AbstractSetting<?>, Object> tempSettingMap = new HashMap<>();
    private final List<AbstractActionbar> actionbarQueue = new ArrayList<>();

    public PlayerInfo(IVeranyPlugin plugin, String name) {
        super(plugin, "players");
        this.plugin = plugin;
        this.name = name;
        this.achievementQueue = new AchievementQueue(this, (VeranyPlugin) plugin);
    }

    @Override
    public void load(UUID key) {
        this.uniqueId = key;

        permissionObject = new PermissionObject(getProject());
        permissionObject.load(key);

        friendObject = new FriendObject(getProject());
        friendObject.load(key);

        verificationObject = new VerificationObject(getProject());
        verificationObject.load(key);

        afkObject = new AfkObject((VeranyPlugin) plugin);
        afkObject.load(key);

        load();

        IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
        if (playerManager != null) {
            cloudPlayer = playerManager.getOnlinePlayer(uniqueId);
            if (cloudPlayer != null)
                playerExecutor = playerManager.getPlayerExecutor(cloudPlayer);
        }

        levelObject = new LevelObject(this);
        levelObject.load(key);

        creditsObject = new CreditsObject(this);
        creditsObject.load(key);

        if (!name.equals(getName()))
            getDataOptional(PlayerEntry.class).ifPresent(playerData -> playerData.setName(name));

        //setSkinData();

    }

    private void load() {
        load(new LoadInfo<>("user", PlayerEntry.class, new PlayerEntry(uniqueId, name, VeranyLanguage.ENGLISH.getName(), PrefixPattern.BLUE.getKey(), 0, 0, 0, new ArrayList<>(), new HashMap<>(), new ArrayList<>())));
        getDataOptional(PlayerEntry.class).ifPresent(playerEntry -> loadSettings(playerEntry.getSettingValues()));
    }

    @Override
    public void update() {
        save("user");

        permissionObject.update();
        friendObject.update();
        afkObject.update();

        player = null;
    }

    @Override
    public void removeMetadata(String key) {
        plugin.removeMetadata(player, key);
    }

    @Override
    public String getNameWithColor() {
        return ChatColor.valueOf(permissionObject.getCurrentGroup().getGroup().getColor()) + getName();
    }

    @Override
    public String getGroupWithColor() {
        return ChatColor.valueOf(permissionObject.getCurrentGroup().getGroup().getColor()) + permissionObject.getCurrentGroup().getGroup().getName();
    }

    @Override
    public long getOnlineTime() {
        if (getDataOptional(PlayerEntry.class).isEmpty()) return 0;
        return getDataOptional(PlayerEntry.class).get().getOnlineTime();
    }

    @Override
    public long getPlayTime() {
        if (getDataOptional(PlayerEntry.class).isEmpty()) return 0;
        return getDataOptional(PlayerEntry.class).get().getPlayTime();
    }

    @Override
    public long getFirstJoined() {
        if (getDataOptional(PlayerEntry.class).isEmpty()) return 0;
        return getDataOptional(PlayerEntry.class).get().getFirstJoined();
    }

    @Override
    public long getLastOnline() {
        if (getDataOptional(PlayerEntry.class).isEmpty()) return 0;
        return getDataOptional(PlayerEntry.class).get().getLastOnline();
    }

    @Override
    public void addOnlineTime() {
        getDataOptional(PlayerEntry.class).ifPresent(playerData -> playerData.setOnlineTime(getOnlineTime() + 1000));
    }

    @Override
    public void addPlayTime() {
        getDataOptional(PlayerEntry.class).ifPresent(playerData -> playerData.setPlayTime(getPlayTime() + 1000));
    }

    @Override
    public int getGlobalRank() {
        List<IPlayerInfo> registered = Verany.PROFILE_OBJECT.getRegisteredPlayers(IPlayerInfo.class);
        int rank = 1;
        for (IPlayerInfo playerInfo : Verany.sortList(registered, Comparator.comparingInt(IPlayerInfo::getPoints).reversed())) {
            if (playerInfo.getName().equals(getName())) break;
            rank++;
        }
        return rank;
    }

    @Override
    public <T> void setTempSetting(AbstractSetting<T> setting, T value) {
        tempSettingMap.put(setting, value);
    }

    @Override
    public <T> T getTempSetting(AbstractSetting<T> setting) {
        if (!tempSettingMap.containsKey(setting))
            setTempSetting(setting, setting.getDefaultValue());
        return (T) tempSettingMap.get(setting);
    }

    @Override
    public String getServer() {
        return getCloudPlayer().getConnectedService().getServerName();
    }

    @Override
    public String getPrefix(VeranyModule module) {
        return Verany.getPrefix(module.prefix(), getPrefixPattern());
    }

    @Override
    public String getPrefix(String prefix) {
        return Verany.getPrefix(prefix, getPrefixPattern());
    }

    @Override
    public AbstractPrefixPattern getPrefixPattern() {
        if (getDataOptional(PlayerEntry.class).isEmpty()) return PrefixPattern.BLUE;
        return PrefixPattern.getPatternByKey(getDataOptional(PlayerEntry.class).get().getPrefix());
    }

    @Override
    public void setPrefixPattern(AbstractPrefixPattern pattern) {
        if (getDataOptional(PlayerEntry.class).isEmpty()) return;
        PlayerEntry playerData = getDataOptional(PlayerEntry.class).get();
        playerData.setPrefix(pattern.getKey());
        update(PlayerEntry.class, playerData);
        Bukkit.getPluginManager().callEvent(new PlayerPrefixUpdateEvent(pattern, getPlayer()));
    }

    @Override
    public void createLog(PlayerLog log) {

    }

    @Override
    public List<PlayerLog> getLogs() {
        if (getDataOptional(PlayerEntry.class).isEmpty()) return new ArrayList<>();
        return getDataOptional(PlayerEntry.class).get().getLogs().stream().map(s -> Verany.GSON.fromJson(s, PlayerLog.class)).collect(Collectors.toList());
    }

    @Override
    public List<PlayerLog> getLogs(PlayerLog.LogType logType) {
        return getLogs().stream().filter(playerLog -> playerLog.getLogType().equals(logType)).collect(Collectors.toList());
    }

    @Override
    public <T extends IDefault<UUID>> T getPlayer(Class<T> tClass) {
        return Verany.getPlayer(uniqueId, tClass);
    }

    @Override
    public void addPoints(int amount) {
        getDataOptional(PlayerEntry.class).ifPresent(playerData -> playerData.setPoints(getPoints() + amount));
    }

    @Override
    public int getPoints() {
        if (getDataOptional(PlayerEntry.class).isPresent()) return getDataOptional(PlayerEntry.class).get().getPoints();
        return 0;
    }

    @Override
    public EnumLanguage getLanguage() {
        AtomicReference<EnumLanguage> language = new AtomicReference<>();
        getDataOptional(PlayerEntry.class).ifPresentOrElse(playerData -> language.set(EnumLanguage.valueOf(playerData.getLanguage())), () -> language.set(EnumLanguage.ENGLISH));
        return language.get();
    }

    @Override
    public EnumLanguage setLanguage(EnumLanguage language) {
        getDataOptional(PlayerEntry.class).ifPresent(playerData -> {
            playerData.setLanguage(language.name());
            Bukkit.getPluginManager().callEvent(new PlayerLanguageUpdateEvent(getLanguage(), language, getPlayer()));
        });
        return language;
    }

    @Override
    public AbstractLanguage getCurrentLanguage() {
        AtomicReference<AbstractLanguage> language = new AtomicReference<>();
        getDataOptional(PlayerEntry.class).ifPresentOrElse(playerData -> language.set(LanguageWrapper.getLanguage(playerData.getLanguage()).orElse(VeranyLanguage.ENGLISH)), () -> language.set(VeranyLanguage.ENGLISH));
        return language.get();
    }

    @Override
    public AbstractLanguage setCurrentLanguage(AbstractLanguage language) {
        getDataOptional(PlayerEntry.class).ifPresent(playerData -> {
            playerData.setLanguage(language.getName());
            Bukkit.getPluginManager().callEvent(new PlayerLanguageUpdateEvent(getPlayer()));
        });
        return language;
    }

    @Override
    public String getKey(String key, Placeholder... placeholders) {
        return getKey(key, getPrefixPattern(), placeholders);
    }

    @Override
    public String getKey(String key, AbstractPrefixPattern prefixPattern, Placeholder... placeholders) {
        AbstractLanguage language = getCurrentLanguage();
        String message = Verany.getMessage(key, language);

        if (message == null) {
            Verany.createMessage(getProject(), key);
            Verany.reloadMessages(getProject());
            message = key;
        }

        message = message.replace("$f", ChatColor.valueOf(prefixPattern.getColor().firstColor()).toString());
        message = message.replace("$s", ChatColor.valueOf(prefixPattern.getColor().secondColor()).toString());

        if (placeholders != null)
            for (Placeholder placeholder : placeholders)
                message = message.replace(placeholder.target(), placeholder.replacement().toString());

        return Verany.serializeHex(message);
    }

    @Override
    public String getKey(KeyBuilder keyBuilder) {
        String key = keyBuilder.key();
        if (key == null) throw new MissingFormatArgumentException("Cannot use KeyBuilder without key");
        AbstractPrefixPattern pattern = keyBuilder.prefixPattern();
        Placeholder[] placeholders = keyBuilder.placeholders();
        return getKey(key, pattern == null ? getPrefixPattern() : pattern, placeholders);
    }

    @Override
    public String[] getKeyArray(String key, char regex, Placeholder... placeholders) {
        return getKeyArray(key, regex, getPrefixPattern(), placeholders);
    }

    @Override
    public String[] getKeyArray(String key, char regex, AbstractPrefixPattern prefixPattern, Placeholder... placeholders) {
        return getKey(key, prefixPattern, placeholders).split(Character.toString(regex));
    }

    @Override
    public String[] getKeyArray(KeyBuilder keyBuilder) {
        return getKey(keyBuilder).split(String.valueOf(keyBuilder.regex()));
    }

    @Override
    public String sendKey(String key, Placeholder... placeholders) {
        return sendKey("", key, placeholders);
    }

    @Override
    public String sendKey(String key, AbstractPrefixPattern prefixPattern, Placeholder... placeholders) {
        return sendKey("", key, prefixPattern, placeholders);
    }

    @Override
    public String sendKey(String prefix, String key, Placeholder... placeholders) {
        return sendKey(prefix, key, getPrefixPattern(), placeholders);
    }

    @Override
    public String sendKey(String prefix, String key, AbstractPrefixPattern prefixPattern, Placeholder... placeholders) {
        String message = prefix + getKey(key, prefixPattern, placeholders);
        sendMessage(message);
        return message;
    }

    @Override
    public String sendKey(KeyBuilder keyBuilder) {
        String message = getKey(keyBuilder);
        sendMessage(message);
        return message;
    }

    @Override
    public <T> void requestChatMessage(VeranyProject plugin, ChatRequest<T> request, ChatRequestCallback callback) {
        Document document = new Document();
        document.append("request", request);
        document.append("callback", callback);
        document.append("timestamp", System.currentTimeMillis() + request.getWaitMillis());

        getPlugin().setMetadata(player, "chat.request", document);

        if (request.getMessage() != null && request.getMessage().length != 0)
            sendMessage(request.getMessage());
    }


    @Override
    public void registerPage(String... pages) {
        for (String page : pages)
            pageMap.put(page, 1);
    }

    @Override
    public int getPage(String page) {
        if (!pageMap.containsKey(page))
            registerPage(page);
        return pageMap.get(page);
    }

    @Override
    public int nextPage(String page) {
        pageMap.put(page, pageMap.get(page) + 1);
        return pageMap.get(page);
    }

    @Override
    public int previousPage(String page) {
        if (getPage(page) == 1) return 1;
        pageMap.put(page, pageMap.get(page) - 1);
        return pageMap.get(page);
    }

    @Override
    public int switchPage(String page, IInventoryBuilder.PageSwitchHandler.Type type) {
        return switch (type) {
            case NEXT -> nextPage(page);
            case PREVIOUS -> previousPage(page);
            default -> 1;
        };
    }

    @Override
    public void sendMessage(String... messages) {
        if (player != null)
            player.sendMessage(messages);
    }

    @Override
    public void setSkinData() {
        String[] skinTexture = getSkinTexture();
        if (skinTexture == null) return;
        this.skinData = new SkinData(skinTexture[1], skinTexture[0]);
    }

    private String[] getSkinTexture() {
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uniqueId.toString() + "?unsigned=false");
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject textureProperty = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();

            return new String[]{texture, signature};
        } catch (Exception e) {
            System.err.println("Could not get skin data from session servers!");
            return null;
        }
    }

    @Override
    public void setItem(int index, HotbarItem hotbarItem) {
        Verany.HOTBAR_ITEMS.removeIf(item -> item.getItemStack().getType().equals(hotbarItem.getItemStack().getType()) && item.getPlayer().equals(hotbarItem.getPlayer()) && item.getItemStack().getItemMeta().getDisplayName().equals(hotbarItem.getItemStack().getItemMeta().getDisplayName()));
        getPlayer().getInventory().setItem(index, hotbarItem.getItemStack());
        Verany.HOTBAR_ITEMS.add(hotbarItem);
    }

    @Override
    public void passAchievement(VeranyAchievement achievement) {
        getDataOptional(PlayerEntry.class).ifPresent(playerData -> {
            playerData.getPassedAchievements().add(achievement.getName());
            achievementQueue.addToQueue(achievement);
        });
    }

    @Override
    public boolean hasAchievement(VeranyAchievement achievement) {
        if (getDataOptional(PlayerEntry.class).isEmpty()) return false;
        return getDataOptional(PlayerEntry.class).get().getPassedAchievements().contains(achievement);
    }

    @Override
    public BossBar addBossBar(BossBar bossBar) {
        return null;
    }

    @Override
    public BossBar removeBossBar(NamespacedKey key) {
        return null;
    }

    @Override
    public boolean hasBosBar(NamespacedKey key) {
        return false;
    }

    @Override
    public List<BossBar> getBossBars() {
        return null;
    }

    @Override
    public BossBar getBossBarByKey(NamespacedKey key) {
        return null;
    }

    @Override
    public void sendMessage(AbstractComponentBuilder... componentBuilder) {
        TextComponent mainMessage = new TextComponent();
        for (AbstractComponentBuilder abstractComponentBuilder : componentBuilder) {
            TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(abstractComponentBuilder.getText()));
            if (abstractComponentBuilder.getClickEvent() != null)
                textComponent.setClickEvent(abstractComponentBuilder.getClickEvent());
            if (abstractComponentBuilder.getHoverEvent() != null)
                textComponent.setHoverEvent(abstractComponentBuilder.getHoverEvent());
            for (TextComponent extra : abstractComponentBuilder.getExtras())
                textComponent.addExtra(extra);
            mainMessage.addExtra(textComponent);
        }
        getPlayer().sendMessage(mainMessage);
    }

    @Override
    public void addActionbar(AbstractActionbar data) {
        if (!actionbarQueue.isEmpty()) {
            if (data instanceof NumberActionbar dataActionbar && actionbarQueue.get(0) instanceof NumberActionbar actionbar) {
                if (dataActionbar.getText().equals(actionbar.getText())) {
                    actionbar.setAmount(actionbar.getAmount() + dataActionbar.getAmount());

                    currentActionbarTime = dataActionbar.getTime() + System.currentTimeMillis();
                    actionbarQueue.set(0, actionbar);
                    return;
                }
            }
        } else {
            currentActionbarTime = data.getTime() + System.currentTimeMillis();
        }
        if (getDataByText(data.getText()) != 0)
            data.setExtra(" (" + (getDataByText(data.getText()) + 1) + ")");
        actionbarQueue.add(data);
    }

    private int getDataByText(String text) {
        int amount = 0;
        for (AbstractActionbar data : actionbarQueue)
            if (data.getText().equalsIgnoreCase(text))
                amount++;
        return amount;
    }

    @Override
    public void setActionbar(AbstractActionbar data) {
        plugin.setMetadata(player, "actionbar_set", data);
        currentActionbarTime = data.getTime() + System.currentTimeMillis();
    }

    @Override
    public void forceActionbar(AbstractActionbar data) {
        List<AbstractActionbar> tempList = new ArrayList<>(actionbarQueue);

        if (!actionbarQueue.isEmpty())
            lastActionbarTime = actionbarQueue.get(0).getTime() + actionbarQueue.get(0).getCreated();
        currentActionbarTime = data.getTime() + System.currentTimeMillis();

        actionbarQueue.clear();
        actionbarQueue.add(data);
        actionbarQueue.addAll(tempList);
    }

    @Override
    public void sendInfoActionbar(AbstractActionbar data) {
        if (data instanceof NumberActionbar) {
            player.sendActionBar(MessageFormat.format(data.getText(), Verany.asDecimal(((NumberActionbar) data).getAmount())) + data.getExtra());
            return;
        }
        player.sendActionBar(data.getText() + data.getExtra());
    }

    @Override
    public void addBossBar(AbstractBossBar bossBar) {

    }

    @Override
    public void setBossBar(AbstractBossBar bossBar) {

    }

    @Override
    public void setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    @Override
    public void sendOnServer(String server) {
        getPlayerExecutor().connect(server);
    }

    @Override
    public void connectToRound(String server, String id) {

    }

    @Override
    public void sendOnRandomServer(String group) {
        Collection<ServiceInfoSnapshot> collection = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices(group);
        sendOnServer(collection.stream().skip((int) (collection.size() * Math.random())).findFirst().get().getName());
    }

    @Override
    public void playSound(Location location, Sound sound) {
        playSound(location, sound, 1, 1);
    }

    @Override
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        //if (getSettingValue(Settings.INVENTORY_SOUNDS))
        player.playSound(location, sound, volume, pitch);
    }

    @Override
    public void playSound(Sound sound) {
        playSound(player.getLocation(), sound, 1, 1);
    }

    @Override
    public void playSound(Sound sound, float volume, float pitch) {
        playSound(player.getLocation(), sound, volume, pitch);
    }


    @Override
    public void teleport(VeranyLocation location) {
        player.teleport(location.toLocation());
    }
}
