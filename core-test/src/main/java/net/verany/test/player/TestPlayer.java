package net.verany.test.player;

import net.verany.api.chat.request.ChatRequest;
import net.verany.api.chat.request.ChatRequestCallback;
import net.verany.api.interfaces.IDefault;
import net.verany.api.language.AbstractLanguage;
import net.verany.api.language.EnumLanguage;
import net.verany.api.message.KeyBuilder;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyPlugin;
import net.verany.api.module.VeranyProject;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.VeranyPlayer;
import net.verany.api.player.ban.IBanEntry;
import net.verany.api.player.friend.IFriendObject;
import net.verany.api.player.leveling.ICreditsObject;
import net.verany.api.player.leveling.ILevelObject;
import net.verany.api.player.verifictation.IVerificationObject;
import net.verany.api.prefix.AbstractPrefixPattern;
import net.verany.api.settings.AbstractSetting;

import java.util.List;
import java.util.UUID;

public class TestPlayer extends VeranyPlayer {

    public TestPlayer(VeranyPlugin plugin, String name) {
        super(plugin, name);
    }

    @Override
    public IBanEntry getBanEntry() {
        return null;
    }

    @Override
    public void removeMetadata(String key) {

    }

    @Override
    public ICreditsObject getCreditsObject() {
        return null;
    }

    @Override
    public ILevelObject getLevelObject() {
        return null;
    }

    @Override
    public IVerificationObject getVerificationObject() {
        return null;
    }

    @Override
    public String getNameWithColor() {
        return null;
    }

    @Override
    public String getGroupWithColor() {
        return null;
    }

    @Override
    public long getOnlineTime() {
        return 0;
    }

    @Override
    public long getPlayTime() {
        return 0;
    }

    @Override
    public long getFirstJoined() {
        return 0;
    }

    @Override
    public long getLastOnline() {
        return 0;
    }

    @Override
    public void addOnlineTime() {

    }

    @Override
    public void addPlayTime() {

    }

    @Override
    public int getGlobalRank() {
        return 0;
    }

    @Override
    public <T> void setTempSetting(AbstractSetting<T> setting, T value) {

    }

    @Override
    public <T> T getTempSetting(AbstractSetting<T> setting) {
        return null;
    }

    @Override
    public String getServer() {
        return null;
    }

    @Override
    public String getPrefix(VeranyModule module) {
        return null;
    }

    @Override
    public String getPrefix(String prefix) {
        return null;
    }

    @Override
    public AbstractPrefixPattern getPrefixPattern() {
        return null;
    }

    @Override
    public void setPrefixPattern(AbstractPrefixPattern pattern) {

    }

    @Override
    public void createLog(PlayerLog log) {

    }

    @Override
    public List<PlayerLog> getLogs() {
        return null;
    }

    @Override
    public List<PlayerLog> getLogs(PlayerLog.LogType logType) {
        return null;
    }

    @Override
    public <T extends IDefault<UUID>> T getPlayer(Class<T> tClass) {
        return null;
    }

    @Override
    public void addPoints(int amount) {

    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public IFriendObject getFriendObject() {
        return null;
    }

    @Override
    public EnumLanguage getLanguage() {
        return null;
    }

    @Override
    public EnumLanguage setLanguage(EnumLanguage language) {
        return null;
    }

    @Override
    public AbstractLanguage getCurrentLanguage() {
        return null;
    }

    @Override
    public AbstractLanguage setCurrentLanguage(AbstractLanguage language) {
        return null;
    }

    @Override
    public String getKey(String key, Placeholder... placeholders) {
        return null;
    }

    @Override
    public String getKey(String key, AbstractPrefixPattern prefixPattern, Placeholder... placeholders) {
        return null;
    }

    @Override
    public String getKey(KeyBuilder keyBuilder) {
        return null;
    }

    @Override
    public String[] getKeyArray(String key, char regex, Placeholder... placeholders) {
        return new String[0];
    }

    @Override
    public String[] getKeyArray(String key, char regex, AbstractPrefixPattern prefixPattern, Placeholder... placeholders) {
        return new String[0];
    }

    @Override
    public String[] getKeyArray(KeyBuilder keyBuilder) {
        return new String[0];
    }

    @Override
    public String sendKey(String key, Placeholder... placeholders) {
        return null;
    }

    @Override
    public String sendKey(String key, AbstractPrefixPattern prefixPattern, Placeholder... placeholders) {
        return null;
    }

    @Override
    public String sendKey(String prefix, String key, Placeholder... placeholders) {
        return null;
    }

    @Override
    public String sendKey(String prefix, String key, AbstractPrefixPattern prefixPattern, Placeholder... placeholders) {
        return null;
    }

    @Override
    public String sendKey(KeyBuilder keyBuilder) {
        return null;
    }

    @Override
    public <T> void requestChatMessage(VeranyProject plugin, ChatRequest<T> request, ChatRequestCallback callback) {

    }
}
