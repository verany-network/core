package net.verany.api.player;

import net.verany.api.interfaces.IDefault;
import net.verany.api.language.EnumLanguage;
import net.verany.api.message.KeyBuilder;
import net.verany.api.placeholder.Placeholder;
import net.verany.api.player.leveling.ICreditsObject;
import net.verany.api.player.party.IPartyObject;
import net.verany.api.player.permission.IPermissionObject;
import net.verany.api.player.verifictation.IVerificationObject;
import net.verany.api.prefix.AbstractPrefixPattern;

import java.util.UUID;

public interface IVeranyPlayer extends IDefault<UUID> {

    ICreditsObject getCreditsObject();

    IPermissionObject getPermissionObject();

    IVerificationObject getVerificationObject();

    String getName();

    String getNameWithColor();

    String getGroupWithColor();

    long getOnlineTime();

    long getPlayTime();

    long getFirstJoined();

    long getLastOnline();

    void addOnlineTime();

    void addPlayTime();

    EnumLanguage getLanguage();

    EnumLanguage setLanguage(EnumLanguage language);
    
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

}
