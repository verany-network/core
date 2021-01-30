package net.verany.api.player;

import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.executor.PlayerExecutor;
import net.verany.api.achievements.VeranyAchievement;
import net.verany.api.actionbar.AbstractActionbar;
import net.verany.api.chat.request.ChatRequest;
import net.verany.api.chat.request.ChatRequestCallback;
import net.verany.api.hotbar.HotbarItem;
import net.verany.api.inventory.IInventoryBuilder;
import net.verany.api.language.EnumLanguage;
import net.verany.api.message.AbstractComponentBuilder;
import net.verany.api.module.VeranyModule;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.afk.IAFKObject;
import net.verany.api.player.leveling.ICreditsObject;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.api.player.verifictation.IVerificationObject;
import net.verany.api.plugin.IVeranyPlugin;
import net.verany.api.prefix.AbstractPrefixPattern;
import net.verany.api.settings.AbstractSetting;
import net.verany.api.skin.AbstractSkinData;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

    List<AbstractActionbar> getActionbarQueue();

    void sendOnServer(String server);

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

    void setDefaultActionbar(String actionbar);

    IAFKObject getAfkObject();

    int getGlobalRank();

}
