package net.verany.api.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.chat.request.ChatRequest;
import net.verany.api.chat.request.ChatRequestCallback;
import net.verany.api.interfaces.IDefault;
import net.verany.api.language.AbstractLanguage;
import net.verany.api.language.EnumLanguage;
import net.verany.api.message.KeyBuilder;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.ban.IBanEntry;
import net.verany.api.player.friend.IFriendObject;
import net.verany.api.player.leveling.ICreditsObject;
import net.verany.api.player.leveling.ILevelObject;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.api.player.stats.IStatsObject;
import net.verany.api.player.verifictation.IVerificationObject;
import net.verany.api.prefix.AbstractPrefixPattern;
import net.verany.api.settings.AbstractSetting;

import java.util.List;
import java.util.UUID;

public interface IVeranyPlayer extends IDefault<UUID> {

    String getName();

    IBanEntry getBanEntry();

    void removeMetadata(String key);

    IPermissionObject getPermissionObject();

    ICreditsObject getCreditsObject();

    ILevelObject getLevelObject();

    IVerificationObject getVerificationObject();

    String getNameWithColor();

    String getGroupWithColor();

    long getOnlineTime();

    long getPlayTime();

    long getFirstJoined();

    long getLastOnline();

    void addOnlineTime();

    void addPlayTime();

    int getGlobalRank();

    <T> void setTempSetting(AbstractSetting<T> setting, T value);

    <T> T getTempSetting(AbstractSetting<T> setting);

    <T> T getSettingValue(AbstractSetting<T> setting);

    <T> void setSettingValue(AbstractSetting<T> setting, T value);

    String getServer();

    String getPrefix(VeranyModule module);

    String getPrefix(String prefix);

    AbstractPrefixPattern getPrefixPattern();

    void setPrefixPattern(AbstractPrefixPattern pattern);

    void createLog(PlayerLog log);

    List<PlayerLog> getLogs();

    List<PlayerLog> getLogs(PlayerLog.LogType logType);

    <T extends IDefault<UUID>> T getPlayer(Class<T> tClass);

    void addPoints(int amount);

    int getPoints();

    IFriendObject getFriendObject();

    @Deprecated
    EnumLanguage getLanguage();

    @Deprecated
    EnumLanguage setLanguage(EnumLanguage language);

    AbstractLanguage getCurrentLanguage();

    AbstractLanguage setCurrentLanguage(AbstractLanguage language);

    String getKey(String key, Placeholder... placeholders);

    String getKey(String key, AbstractPrefixPattern prefixPattern, Placeholder... placeholders);

    String getKey(KeyBuilder keyBuilder);

    String[] getKeyArray(String key, char regex, Placeholder... placeholders);

    String[] getKeyArray(String key, char regex, AbstractPrefixPattern prefixPattern, Placeholder... placeholders);

    String[] getKeyArray(KeyBuilder keyBuilder);

    String sendKey(String key, Placeholder... placeholders);

    String sendKey(String key, AbstractPrefixPattern prefixPattern, Placeholder... placeholders);

    String sendKey(String prefix, String key, Placeholder... placeholders);

    String sendKey(String prefix, String key, AbstractPrefixPattern prefixPattern, Placeholder... placeholders);

    String sendKey(KeyBuilder keyBuilder);

    <T> void requestChatMessage(VeranyProject plugin, ChatRequest<T> request, ChatRequestCallback callback);

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
