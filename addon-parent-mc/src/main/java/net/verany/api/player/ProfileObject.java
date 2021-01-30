package net.verany.api.player;

import lombok.Getter;
import net.verany.api.plugin.IProfileObject;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class ProfileObject implements IProfileObject {

    private final List<IPlayerInfo> registeredPlayers = new CopyOnWriteArrayList<>();

    @Override
    public Optional<IPlayerInfo> getPlayer(UUID uuid) {
        return registeredPlayers.stream().filter(iPlayerInfo -> iPlayerInfo.getUniqueId().equals(uuid)).findFirst();
    }

    @Override
    public Optional<IPlayerInfo> getPlayer(String name) {
        return registeredPlayers.stream().filter(iPlayerInfo -> iPlayerInfo.getName().toLowerCase(Locale.ROOT).equals(name.toLowerCase(Locale.ROOT))).findFirst();
    }

}
