package net.verany.api.player.friend.settings;

import net.verany.api.setting.SettingWrapper;
import net.verany.api.settings.AbstractSetting;

public class FriendSetting {

    public static final AbstractSetting<Boolean> REQUESTS = new SettingWrapper<>("friend_requests", "friends", Boolean.class, true, true);

}
