package net.verany.api.player;

import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import lombok.SneakyThrows;
import net.verany.api.chat.request.ChatRequest;
import net.verany.api.chat.request.ChatRequestCallback;
import net.verany.api.interfaces.IDefault;
import net.verany.api.language.AbstractLanguage;
import net.verany.api.language.EnumLanguage;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.message.KeyBuilder;
import net.verany.api.module.VeranyModule;
import net.verany.api.module.VeranyProject;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.PlayerEntry;
import net.verany.api.player.ban.IBanEntry;
import net.verany.api.player.leveling.ICreditsObject;
import net.verany.api.player.leveling.ILevelObject;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.api.player.verifictation.IVerificationObject;
import net.verany.api.prefix.AbstractPrefixPattern;
import net.verany.api.settings.AbstractSetting;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.UUID;

@Getter
public class PlayerInfo extends DatabaseLoader implements IPlayerInfo {

    private UUID uniqueId;

    public PlayerInfo(VeranyProject project, String collection) {
        super(project, collection);
    }

    @Override
    public void load(UUID key) {
        this.uniqueId = key;
    }

    @Override
    public void update() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public IBanEntry getBanEntry() {
        return null;
    }

    @Override
    public void removeMetadata(String key) {

    }

    @Override
    public IPermissionObject getPermissionObject() {
        return null;
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
    public <T> T getSettingValue(AbstractSetting<T> setting) {
        return null;
    }

    @Override
    public <T> void setSettingValue(AbstractSetting<T> setting, T value) {

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

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public String getIp() {
        return null;
    }

    @SneakyThrows
    @Override
    public String hashIp(String address) {
        return generateStrongPasswordHash(address);
    }

    @Override
    public void setIp(String address) {
        if (getDataOptional(PlayerEntry.class).isEmpty()) return;
        getDataOptional(PlayerEntry.class).get().setIp(address);
    }

    private String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

}
