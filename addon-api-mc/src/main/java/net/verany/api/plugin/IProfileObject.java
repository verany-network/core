package net.verany.api.plugin;

import net.verany.api.player.IPlayerInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProfileObject {

    Optional<IPlayerInfo> getPlayer(UUID uuid);

    Optional<IPlayerInfo> getPlayer(String name);

    List<IPlayerInfo> getRegisteredPlayers();

}
