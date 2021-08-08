package net.verany.api.player;

import com.velocitypowered.api.proxy.Player;
import net.verany.api.player.friend.IFriendObject;

public interface IPlayerInfo extends IVeranyPlayer {

    Player getPlayer();

    String getIp();

    String hashIp(String address);

    void setIp(String address);

}
