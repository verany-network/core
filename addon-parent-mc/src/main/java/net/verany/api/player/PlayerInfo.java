package net.verany.api.player;

import com.google.gson.Gson;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.executor.PlayerExecutor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.verany.api.Verany;
import net.verany.api.achievement.AchievementQueue;
import net.verany.api.achievement.VeranyAchievementType;
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
import net.verany.api.language.EnumLanguage;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.locationmanager.VeranyLocation;
import net.verany.api.message.AbstractComponentBuilder;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.afk.AfkObject;
import net.verany.api.player.afk.IAFKObject;
import net.verany.api.player.clan.IClanObject;
import net.verany.api.player.friend.FriendObject;
import net.verany.api.player.friend.IFriendObject;
import net.verany.api.player.leveling.CreditsObject;
import net.verany.api.player.leveling.ICreditsObject;
import net.verany.api.player.leveling.ILevelObject;
import net.verany.api.player.leveling.LevelObject;
import net.verany.api.player.party.IPartyObject;
import net.verany.api.player.party.PartyObject;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.api.player.permission.PermissionObject;
import net.verany.api.player.verification.VerificationObject;
import net.verany.api.player.verifictation.IVerificationObject;
import net.verany.api.plugin.IVeranyPlugin;
import net.verany.api.prefix.AbstractPrefixPattern;
import net.verany.api.prefix.PrefixPattern;
import net.verany.api.settings.Settings;
import net.verany.api.settings.AbstractSetting;
import net.verany.api.skin.AbstractSkinData;
import net.verany.api.skin.SkinData;
import net.verany.api.sound.AbstractVeranySound;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Setter
@Getter
public class PlayerInfo extends DatabaseLoader implements IPlayerInfo {

    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    private UUID uniqueId;
    private final String name;

    private final Map<String, Integer> pageMap = new HashMap<>();
    private final Map<AbstractSetting<?>, Object> tempSettingMap = new HashMap<>();
    private final List<AbstractActionbar> actionbarQueue = new ArrayList<>();
    private final List<BossBar> activeBossBars = new ArrayList<>();

    private AchievementQueue achievementQueue = new AchievementQueue(this, getProject());

    private Player player = null;
    private IPermissionObject permissionObject;
    private ICreditsObject creditsObject;
    private ILevelObject levelObject;
    private IFriendObject friendObject;
    private IPartyObject partyObject;
    private IClanObject clanObject;
    private IVerificationObject verificationObject;
    private IAFKObject afkObject;

    private String defaultActionbar = null;
    private boolean shouldLoad = false;
    private AbstractBossBar defaultBossBar = null;
    private BossBar bossBar = null;

    private long currentActionbarTime = -1;
    private long lastActionbarTime = -1;

    public PlayerInfo(VeranyProject project, String name) {
        super(project, "players");
        this.name = name;
    }

    @Override
    public void load(UUID key) {
        this.uniqueId = key;

        permissionObject = new PermissionObject(this, getProject());
        permissionObject.load(key);

        friendObject = new FriendObject(getProject());
        friendObject.load(key);

        partyObject = new PartyObject(getProject());
        partyObject.load(key);

        verificationObject = new VerificationObject(getProject());
        verificationObject.load(key);

        afkObject = new AfkObject(getProject());
        afkObject.load(key);

        load();

        levelObject = new LevelObject(this);
        levelObject.load(key);

        creditsObject = new CreditsObject(this);
        creditsObject.load(key);

        if (!name.equals(getName()))
            getData(PlayerData.class).setName(name);

        if (!Verany.shutdown)
            if (Bukkit.getPlayer(key) != null) {
                player = Bukkit.getPlayer(uniqueId);
                setSkinData();
                sendUpdate();
            }

    }

    @Override
    public void update() {
        save("user_info");

        permissionObject.update();
        friendObject.update();
        partyObject.update();
        afkObject.update();

        player = null;
        load(uniqueId);
    }

    private void load() {
        load(new LoadInfo<>("user_info", PlayerData.class, new PlayerData(uniqueId, name, EnumLanguage.ENGLISH, PrefixPattern.BLUE.getKey(), 0, 0, 0, new ArrayList<>(), new HashMap<>(), new ArrayList<>())));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNameWithColor() {
        return permissionObject.getCurrentGroup().getGroup().getColor() + getName();
    }

    @Override
    public String getGroupWithColor() {
        return permissionObject.getCurrentGroup().getGroup().getColor() + permissionObject.getCurrentGroup().getGroup().getName();
    }

    @Override
    public long getOnlineTime() {
        return getData(PlayerData.class).getOnlineTime();
    }

    @Override
    public long getPlayTime() {
        return getData(PlayerData.class).getPlayTime();
    }

    @Override
    public long getFirstJoined() {
        return getData(PlayerData.class).getFirstJoined();
    }

    @Override
    public long getLastOnline() {
        return getData(PlayerData.class).getLastOnline();
    }

    @Override
    public void addOnlineTime() {
        getData(PlayerData.class).setOnlineTime(getOnlineTime() + 1000);
    }

    @Override
    public void addPlayTime() {
        getData(PlayerData.class).setPlayTime(getPlayTime() + 1000);
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
        switch (type) {
            case NEXT:
                return nextPage(page);
            case PREVIOUS:
                return previousPage(page);
        }
        return 1;
    }

    @Override
    public void sendMessage(String... messages) {
        if (player != null)
            player.sendMessage(messages);
    }

    @Override
    public void setSkinData() {
        List<String> data = new ArrayList<>();
        ((CraftPlayer) getPlayer()).getHandle().getProfile().getProperties().get("textures").forEach(property -> {
            data.add(property.getSignature());
            data.add(property.getValue());
        });
        getData(PlayerData.class).setSkinData(new SkinData(data.get(0), data.get(1)));
    }

    @Override
    public AbstractSkinData getSkinData() {
        if (getData(PlayerData.class).getSkinData() == null && player != null)
            setSkinData();
        return getData(PlayerData.class).getSkinData();
    }

    @Override
    public void setItem(int index, HotbarItem hotbarItem) {
        Verany.HOTBAR_ITEMS.removeIf(item -> item.getItemStack().getType().equals(hotbarItem.getItemStack().getType()) && item.getPlayer().equals(hotbarItem.getPlayer()) && item.getItemStack().getItemMeta().getDisplayName().equals(hotbarItem.getItemStack().getItemMeta().getDisplayName()));
        getPlayer().getInventory().setItem(index, hotbarItem.getItemStack());
        Verany.HOTBAR_ITEMS.add(hotbarItem);
    }

    @Override
    public void passAchievement(VeranyAchievement achievement) {
        getData(PlayerData.class).passAchievement(achievement);
        achievementQueue.addToQueue(achievement);
    }

    @Override
    public boolean hasAchievement(VeranyAchievement achievement) {
        return getData(PlayerData.class).getPassedAchievements().contains(achievement);
    }

    @Override
    public BossBar addBossBar(BossBar bossBar) {
        activeBossBars.add(bossBar);
        bossBar.addPlayer(getPlayer());
        return bossBar;
    }

    @Override
    public BossBar removeBossBar(NamespacedKey key) {
        BossBar bossBar = getBossBarByKey(key);
        bossBar.removePlayer(getPlayer());
        activeBossBars.remove(bossBar);
        return bossBar;
    }

    @Override
    public boolean hasBosBar(NamespacedKey key) {
        return getBossBarByKey(key) != null;
    }

    @Override
    public List<BossBar> getBossBars() {
        return activeBossBars;
    }

    @Override
    public BossBar getBossBarByKey(NamespacedKey key) {
        return Bukkit.getBossBar(key);
    }

    @Override
    public <T> void requestChatMessage(IVeranyPlugin plugin, ChatRequest<T> request, ChatRequestCallback callback) {
        Document document = new Document();
        document.append("request", request);
        document.append("callback", callback);
        document.append("timestamp", System.currentTimeMillis() + request.getWaitMillis());

        plugin.setMetadata(getPlayer(), "chat.request", document);

        if (request.getMessage() != null && request.getMessage().length != 0)
            sendMessage(request.getMessage());
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
    public EnumLanguage getLanguage() {
        AtomicReference<EnumLanguage> language = new AtomicReference<>();
        getDataOptional(PlayerData.class).ifPresentOrElse(playerData -> language.set(playerData.getLanguage()), () -> language.set(EnumLanguage.ENGLISH));
        return language.get();
    }

    @Override
    public EnumLanguage setLanguage(EnumLanguage language) {
        Verany.sync(getProject(), () -> {
            getData(PlayerData.class).setLanguage(language);
            Bukkit.getPluginManager().callEvent(new PlayerLanguageUpdateEvent(getLanguage(), language, getPlayer()));
            sendUpdate();
        });
        return language;
    }

    @Override
    public String getKey(String key, Placeholder... placeholders) {
        return getKey(key, getPrefixPattern(), placeholders);
    }

    @Override
    public String getKey(String key, AbstractPrefixPattern prefixPattern, Placeholder... placeholders) {
        EnumLanguage language = getLanguage();
        String message;

        if (Verany.getMessage(key, language) == null) {
            Verany.createMessage(getProject(), key);
            Verany.reloadMessages(getProject());
        }
        message = Verany.getMessage(key, language);

        message = message.replace("$f", prefixPattern.getColor().getFirstColor().toString());
        message = message.replace("$s", prefixPattern.getColor().getSecondColor().toString());

        for (Placeholder placeholder : placeholders)
            message = message.replace(placeholder.getTarget(), placeholder.getReplacement().toString());

        return Verany.serializeHex(message);
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
    public String getPrefix(VeranyModule module) {
        return Verany.getPrefix(module.prefix(), getPrefixPattern());
    }

    @Override
    public String getPrefix(String prefix) {
        return Verany.getPrefix(prefix, getPrefixPattern());
    }

    @Override
    public AbstractPrefixPattern getPrefixPattern() {
        AtomicReference<AbstractPrefixPattern> atomicReference = new AtomicReference<>();
        getDataOptional(PlayerData.class).ifPresentOrElse(playerData -> atomicReference.set(playerData.getPrefixPattern()), () -> atomicReference.set(PrefixPattern.BLUE));
        return atomicReference.get();
    }

    @Override
    public void setPrefixPattern(AbstractPrefixPattern pattern) {
        Verany.sync(getProject(), () -> {
            getData(PlayerData.class).setPrefixPattern(pattern.getKey());
            Bukkit.getPluginManager().callEvent(new PlayerPrefixUpdateEvent(getPrefixPattern(), pattern, getPlayer()));
            sendUpdate();
        });
    }

    public void sendUpdate() {
        if (!Verany.shutdown) {
            //Verany.REDIS_MANAGER.sendMessage("update~player~" + uniqueId.toString() + "~" + new Gson().toJson(getData(PlayerData.class)));
            Verany.MESSENGER.sendMessage("core", new JSONObject().put("shouldUpdate", true).put("playerData", Verany.GSON.toJson(getData(PlayerData.class))).put("uuid", uniqueId.toString()), object -> {
            });
        }
    }

    @Override
    public void addActionbar(AbstractActionbar data) {
        if (!actionbarQueue.isEmpty()) {
            if (data instanceof NumberActionbar && actionbarQueue.get(0) instanceof NumberActionbar) {
                NumberActionbar dataActionbar = (NumberActionbar) data;
                NumberActionbar actionbar = (NumberActionbar) actionbarQueue.get(0);
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

    @Override
    public void setActionbar(AbstractActionbar data) {
        getProject().setMetadata(player, "actionbar_set", data);
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
            player.sendActionBar(Verany.format(data.getText(), Verany.asDecimal(((NumberActionbar) data).getAmount())) + data.getExtra());
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
    public void sendOnServer(String server) {
        getPlayerExecutor().connect(server);
    }

    @Override
    public void sendOnRandomServer(String group) {
        Collection<ServiceInfoSnapshot> collection = CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices(group);
        sendOnServer(collection.stream().skip((int) (collection.size() * Math.random())).findFirst().get().getName());
    }

    @Override
    public String getServer() {
        return getCloudPlayer().getConnectedService().getServerName();
    }

    @Override
    public ICloudPlayer getCloudPlayer() {
        return playerManager.getOnlinePlayer(uniqueId);
    }

    @Override
    public PlayerExecutor getPlayerExecutor() {
        return playerManager.getPlayerExecutor(uniqueId);
    }

    @Override
    public void playRiptideAnimation() {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        DataWatcher dataWatcher = entityPlayer.getDataWatcher();
        dataWatcher.set(DataWatcherRegistry.a.a(7), (byte) 0x04);
    }

    @Override
    public DyeColor getFirstColor() {
        return DyeColor.LIGHT_BLUE;
    }

    @Override
    public DyeColor getSecondColor() {
        return DyeColor.CYAN;
    }

    @Override
    public <T> T getSettingValue(AbstractSetting<T> setting) {
        return getData(PlayerData.class).getSettingValue(setting);
    }

    @Override
    public <T> void setSettingValue(AbstractSetting<T> setting, T value) {
        getData(PlayerData.class).setSettingValue(setting, value);
    }

    @Override
    public void setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    @Override
    public int getGlobalRank() {
        List<IPlayerInfo> registered = Verany.PROFILE_OBJECT.getRegisteredPlayers();
        int rank = 1;
        for (IPlayerInfo playerInfo : Verany.sortList(registered, Comparator.comparingInt(IPlayerInfo::getPoints).reversed())) {
            if (playerInfo.getName().equals(getName())) break;
            rank++;
        }
        return rank;
    }

    @Override
    public void playSound(Location location, Sound sound) {
        playSound(location, sound, 1, 1);
    }

    @Override
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        if (getSettingValue(Settings.INVENTORY_SOUNDS))
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
    public void playSound(AbstractVeranySound sound) {
        playSound(player.getLocation(), sound.getSound(), sound.getVolume(), sound.getPitch());
    }

    @Override
    public void playSound(Location location, AbstractVeranySound sound) {
        playSound(location, sound.getSound(), sound.getVolume(), sound.getPitch());
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
    public void teleport(VeranyLocation location) {
        player.teleport(location.toLocation());
    }

    @Override
    public void createLog(PlayerLog log) {
        getData(PlayerData.class).addLog(log);
        update();
    }

    @Override
    public <T extends IDefault<?>> T getPlayer(Class<T> tClass) {
        return Verany.getPlayer(uniqueId.toString(), tClass);
    }

    @Override
    public void addPoints(int amount) {
        getData(PlayerData.class).setPoints(getPoints() + amount);
        sendUpdate();
    }

    @Override
    public int getPoints() {
        try {
            return getData(PlayerData.class).getPoints();
        } catch (Exception exception) {
            return 0;
        }
    }

    private int getDataByText(String text) {
        int amount = 0;
        for (AbstractActionbar data : actionbarQueue)
            if (data.getText().equalsIgnoreCase(text))
                amount++;
        return amount;
    }

    @Getter
    @Setter
    public static class PlayerData extends DatabaseLoadObject {

        private String name;
        private EnumLanguage language;
        @NotNull
        private String prefixPattern;
        private SkinData skinData;
        private Map<String, String> settingValues;
        private List<String> passedAchievements;
        private Integer credits;
        private Integer exp;
        private Integer points;
        @IgnoreField
        private long onlineTime;
        private long playTime;
        private long firstJoined;
        private long lastOnline;
        private List<String> logs;

        public PlayerData(UUID key, String name, EnumLanguage language, @NotNull String prefixPattern, int credits, Integer exp, Integer points, List<String> passedAchievements, Map<String, String> settingValues, List<String> logs) {
            super(key.toString());
            this.name = name;
            this.language = language;
            this.prefixPattern = prefixPattern;
            this.settingValues = settingValues;
            this.passedAchievements = passedAchievements;
            this.credits = credits;
            this.exp = exp;
            this.points = points;
            this.firstJoined = System.currentTimeMillis();
            this.logs = logs;
        }

        public AbstractPrefixPattern getPrefixPattern() {
            return PrefixPattern.getPatternByKey(prefixPattern);
        }

        public <T> void setSettingValue(AbstractSetting<T> setting, T value) {
            settingValues.put(setting.getKey(), new Gson().toJson(value));
        }

        public void addLog(PlayerLog log) {
            logs.add(new Gson().toJson(log));
        }

        public <T> T getSettingValue(AbstractSetting<T> setting) {
            if (!settingValues.containsKey(setting.getKey()))
                setSettingValue(setting, setting.getDefaultValue());
            return new Gson().fromJson(settingValues.get(setting.getKey()), setting.getTClass());
        }

        public List<VeranyAchievement> getPassedAchievements() {
            List<VeranyAchievement> toReturn = new ArrayList<>();
            for (String passedAchievement : passedAchievements)
                toReturn.add(VeranyAchievementType.getAchievementByName(passedAchievement));
            return toReturn;
        }

        public void passAchievement(VeranyAchievement achievement) {
            passedAchievements.add(achievement.getName());
        }

    }
}
