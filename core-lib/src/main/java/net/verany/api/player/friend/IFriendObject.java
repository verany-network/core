package net.verany.api.player.friend;

import net.verany.api.interfaces.IDefault;
import net.verany.api.player.friend.data.FriendData;
import net.verany.api.settings.AbstractSetting;

import java.util.List;
import java.util.UUID;

public interface IFriendObject extends IDefault<UUID> {

    int ONLINE = 1;
    int OFFLINE = 0;

    List<FriendData> getFriends();

    List<FriendData> getRequests();

    List<FriendData> getBlocked();

    List<FriendData> getBestFriends();

    void invite(UUID target);

    void addRequest(UUID requester);

    void accept(UUID uuid);

    void deny(UUID uuid);

    boolean isFriend(UUID uuid);

    void removeFriend(UUID uuid);

    void setBestFriend(UUID uuid);

    boolean isBestFriend(UUID uuid);

    int getStatus();

    void setStatus(int status);

    boolean hasStatus(int status);

    <T> T getSettingValue(AbstractSetting<T> setting);

    <T> void setSettingValue(AbstractSetting<T> setting, T value);

}
