package net.verany.api.player.friend;

import lombok.Getter;
import lombok.Setter;
import net.verany.api.Verany;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.friend.data.FriendData;
import net.verany.api.settings.AbstractSetting;
import net.verany.api.settings.Settings;

import java.io.Serializable;
import java.util.*;

@Getter
public class FriendObject extends DatabaseLoader implements IFriendObject {

    private UUID uniqueId;

    public FriendObject(VeranyProject project) {
        super(project, "players", "friends");
    }

    @Override
    public void load(UUID key) {
        uniqueId = key;

        Map<String, Object> defaultSettingMap = new HashMap<>();
        for (AbstractSetting<?> friend : Settings.getSettingByCategory("friends"))
            defaultSettingMap.put(friend.getKey(), friend.getDefaultValue());
        load(new LoadInfo<>("user_friend", PlayerFriend.class, new PlayerFriend(key, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "", defaultSettingMap)));
    }

    @Override
    public void update() {
        save("user_friend");
    }

    @Override
    public List<FriendData> getFriends() {
        return getData(PlayerFriend.class).getFriends();
    }

    @Override
    public List<FriendData> getRequests() {
        return getData(PlayerFriend.class).getRequests();
    }

    @Override
    public List<FriendData> getBlocked() {
        return getData(PlayerFriend.class).getBlocked();
    }

    @Override
    public List<FriendData> getBestFriends() {
        return getData(PlayerFriend.class).getBestFriends();
    }

    @Override
    public void invite(UUID target) {
        Verany.getPlayer(target).getFriendObject().addRequest(uniqueId);
    }

    @Override
    public void addRequest(UUID requester) {
        getRequests().add(new FriendData(requester));
        updateData();
    }

    @Override
    public void accept(UUID uuid) {
        getRequests().remove(getFriendData(getRequests(), uuid));
        getFriends().add(new FriendData(uuid));
        updateData();
    }

    @Override
    public void deny(UUID uuid) {
        getRequests().remove(getFriendData(getRequests(), uuid));
        updateData();
    }

    @Override
    public boolean isFriend(UUID uuid) {
        for (FriendData friend : getFriends())
            if (friend.getUuid().equals(uuid))
                return true;
        return false;
    }

    @Override
    public void removeFriend(UUID uuid) {
        getFriends().remove(getFriendData(getFriends(), uuid));
        updateData();
    }

    @Override
    public void setBestFriend(UUID uuid) {
        getBestFriends().add(new FriendData(uuid));
        updateData();
    }

    @Override
    public boolean isBestFriend(UUID uuid) {
        for (FriendData friend : getBestFriends())
            if (friend.getUuid().equals(uuid))
                return true;
        return false;
    }

    @Override
    public String getStatus() {
        return getData(PlayerFriend.class).getStatus();
    }

    @Override
    public void setStatus(String status) {
        getData(PlayerFriend.class).setStatus(status);
        updateData();
    }

    @Override
    public <T> T getSettingValue(AbstractSetting<T> setting) {
        return (T) getData(PlayerFriend.class).getSettingValue().get(setting.getKey());
    }

    @Override
    public <T> void setSettingValue(AbstractSetting<T> setting, T value) {
        getData(PlayerFriend.class).getSettingValue().put(setting.getKey(), value);
        updateData();
    }

    private void updateData() {
        Verany.REDIS_MANAGER.putObject("friends_" + uniqueId.toString(), getData(PlayerFriend.class));
    }

    private FriendData getFriendData(List<FriendData> friendData, UUID target) {
        for (FriendData friendDatum : friendData)
            if (friendDatum.getUuid().equals(target))
                return friendDatum;
        return null;
    }

    @Getter
    @Setter
    public static class PlayerFriend extends DatabaseLoadObject implements Serializable {

        private final List<FriendData> friends;
        private final List<FriendData> requests;
        private final List<FriendData> blocked;
        private final List<FriendData> bestFriends;
        private String status;
        private final Map<String, Object> settingValue;

        public PlayerFriend(UUID uuid, List<FriendData> friends, List<FriendData> requests, List<FriendData> blocked, List<FriendData> bestFriends, String status, Map<String, Object> settingValue) {
            super(uuid.toString());
            this.friends = friends;
            this.requests = requests;
            this.blocked = blocked;
            this.bestFriends = bestFriends;
            this.status = status;
            this.settingValue = settingValue;
        }

    }
}
