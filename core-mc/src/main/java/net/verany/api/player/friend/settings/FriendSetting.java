package net.verany.api.player.friend.settings;

import net.verany.api.settings.AbstractSetting;
import net.verany.api.settings.SettingWrapper;
import org.bukkit.Material;

public class FriendSetting {

    public static final AbstractSetting<Boolean> REQUESTS = new SettingWrapper<>("friend_requests", "FRIENDS", Boolean.class, true,  true);
    public static final AbstractSetting<Boolean> JOIN_QUIT_MESSAGES = new SettingWrapper<>("join_quit_messages", "FRIENDS", Boolean.class, true,  true);

}
