package net.verany.api.player;

import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.executor.PlayerExecutor;
import net.verany.api.achievements.VeranyAchievement;
import net.verany.api.actionbar.AbstractActionbar;
import net.verany.api.bossbar.AbstractBossBar;
import net.verany.api.hotbar.HotbarItem;
import net.verany.api.inventory.IInventoryBuilder;
import net.verany.api.locationmanager.VeranyLocation;
import net.verany.api.message.AbstractComponentBuilder;
import net.verany.api.player.afk.IAFKObject;
import net.verany.api.player.friend.IFriendObject;
import net.verany.api.skin.AbstractSkinData;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.List;

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

    void addItem(HotbarItem hotbarItem);

    void passAchievement(VeranyAchievement achievement);

    boolean hasAchievement(VeranyAchievement achievement);

    BossBar addBossBar(BossBar bossBar);

    BossBar removeBossBar(NamespacedKey key);

    boolean hasBosBar(NamespacedKey key);

    List<BossBar> getBossBars();

    BossBar getBossBarByKey(NamespacedKey key);

    void sendMessage(AbstractComponentBuilder... componentBuilder);

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

    ICloudPlayer getCloudPlayer();

    PlayerExecutor getPlayerExecutor();

    IAFKObject getAfkObject();

    IFriendObject getFriendObject();

    String getDefaultActionbar();

    AbstractBossBar getDefaultBossBar();

    BossBar getBossBar();

    void setBossBar(BossBar bossBar);

    void setDefaultActionbar(String actionbar);

    void playSound(Location location, Sound sound);

    void playSound(Location location, Sound sound, float volume, float pitch);

    void playSound(Sound sound);

    void playSound(Sound sound, float volume, float pitch);

    void teleport(VeranyLocation location);

}
