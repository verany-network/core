package net.verany.api.player;

import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.executor.PlayerExecutor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.achievements.VeranyAchievement;
import net.verany.api.actionbar.AbstractActionbar;
import net.verany.api.bossbar.AbstractBossBar;
import net.verany.api.chat.request.ChatRequest;
import net.verany.api.chat.request.ChatRequestCallback;
import net.verany.api.hotbar.HotbarItem;
import net.verany.api.interfaces.IDefault;
import net.verany.api.inventory.IInventoryBuilder;
import net.verany.api.language.EnumLanguage;
import net.verany.api.locationmanager.VeranyLocation;
import net.verany.api.message.AbstractComponentBuilder;
import net.verany.api.module.VeranyModule;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.afk.IAFKObject;
import net.verany.api.player.clan.IClanObject;
import net.verany.api.player.friend.IFriendObject;
import net.verany.api.player.leveling.ICreditsObject;
import net.verany.api.player.leveling.ILevelObject;
import net.verany.api.player.party.IPartyObject;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.api.player.stats.IStatsObject;
import net.verany.api.player.verifictation.IVerificationObject;
import net.verany.api.plugin.IVeranyPlugin;
import net.verany.api.prefix.AbstractPrefixPattern;
import net.verany.api.settings.AbstractSetting;
import net.verany.api.skin.AbstractSkinData;
import net.verany.api.sound.AbstractVeranySound;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface IPlayerInfo extends IVeranyPlayer {

    Player getPlayer();

    void registerPage(String... pages);

    int getPage(String page);

    int nextPage(String page);

    int previousPage(String page);

    void setPlayer(Player player);

    int switchPage(String page, IInventoryBuilder.PageSwitchHandler.Type type);

    void sendMessage(String... messages);

    AbstractSkinData getSkinData();

    void setSkinData();

    void setItem(int index, HotbarItem hotbarItem);

    void passAchievement(VeranyAchievement achievement);

    boolean hasAchievement(VeranyAchievement achievement);

    <T> void requestChatMessage(IVeranyPlugin plugin, ChatRequest<T> request, ChatRequestCallback callback);

    BossBar addBossBar(BossBar bossBar);

    BossBar removeBossBar(NamespacedKey key);

    boolean hasBosBar(NamespacedKey key);

    List<BossBar> getBossBars();

    BossBar getBossBarByKey(NamespacedKey key);

    void sendMessage(AbstractComponentBuilder... componentBuilder);

    String getPrefix(VeranyModule module);

    String getPrefix(String prefix);

    AbstractPrefixPattern getPrefixPattern();

    void setPrefixPattern(AbstractPrefixPattern pattern);

    long getCurrentActionbarTime();

    long getLastActionbarTime();

    void setLastActionbarTime(long time);

    void setCurrentActionbarTime(long time);

    void addActionbar(AbstractActionbar data);

    void setActionbar(AbstractActionbar data);

    void forceActionbar(AbstractActionbar data);

    void sendInfoActionbar(AbstractActionbar data);

    void setDefaultBossBar(AbstractBossBar bossBar);

    void addBossBar(AbstractBossBar bossBar);

    void setBossBar(AbstractBossBar bossBar);

    List<AbstractActionbar> getActionbarQueue();

    void sendOnServer(String server);

    void connectToRound(String server, String id);

    void sendOnRandomServer(String group);

    String getServer();

    ICloudPlayer getCloudPlayer();

    PlayerExecutor getPlayerExecutor();

    void playRiptideAnimation();

    DyeColor getFirstColor();

    DyeColor getSecondColor();

    <T> T getSettingValue(AbstractSetting<T> setting);

    <T> void setSettingValue(AbstractSetting<T> setting, T value);

    String getDefaultActionbar();

    AbstractBossBar getDefaultBossBar();

    BossBar getBossBar();

    void setBossBar(BossBar bossBar);

    void setDefaultActionbar(String actionbar);

    IAFKObject getAfkObject();

    int getGlobalRank();

    void playSound(Location location, Sound sound);

    void playSound(Location location, Sound sound, float volume, float pitch);

    void playSound(Sound sound);

    void playSound(Sound sound, float volume, float pitch);

    void playSound(AbstractVeranySound sound);

    void playSound(Location location, AbstractVeranySound sound);

    <T> void setTempSetting(AbstractSetting<T> setting, T value);

    <T> T getTempSetting(AbstractSetting<T> setting);

    IFriendObject getFriendObject();

    IPartyObject getPartyObject();

    IClanObject getClanObject();

    ILevelObject getLevelObject();

    void teleport(VeranyLocation location);

    void createLog(PlayerLog log);

    <T extends IDefault<?>> T getPlayer(Class<T> tClass);

    void addPoints(int amount);

    int getPoints();

    boolean isShouldLoad();

    void setShouldLoad(boolean value);

    @AllArgsConstructor
    @Getter
    class PlayerLog {

        private final LogType logType;
        private final String server;
        private final String message;
        private final long timestamp = System.currentTimeMillis();

        public enum LogType {
            CONNECT,
            DISCONNECT,
            SWITCH,
            CHAT
        }
    }

}
