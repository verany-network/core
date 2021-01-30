package net.verany.api.player.party;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface IParty {

    void invite(UUID uuid);

    boolean isInvited(UUID uuid);

    boolean isInvitationExpired(UUID uuid);

    void join(UUID uuid);

    void leave(UUID uuid);

    boolean isLeader(UUID uuid);

    Player getLeader();

    List<Player> getMember();

    List<Player> getPlayers();

    String getId();

}
