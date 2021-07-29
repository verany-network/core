package net.verany.api.player;

import lombok.Getter;
import net.verany.api.interfaces.IDefault;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class ProfileObject implements IProfileObject {

    private final Map<String, List<? extends IVeranyPlayer>> registeredPlayers = new HashMap<>();

    @Override
    public <T extends IVeranyPlayer> Optional<T> getPlayer(UUID uuid, Class<T> tClass) {
        return getRegisteredPlayers(tClass).stream().filter(iPlayerInfo -> iPlayerInfo.getUniqueId().equals(uuid)).findFirst();
    }

    @Override
    public <T extends IVeranyPlayer> Optional<T> getPlayer(UUID uuid) {
        throw new UnsupportedOperationException("No longer supported!");
    }

    @Override
    public <T extends IVeranyPlayer> Optional<T> getPlayer(String name, Class<T> tClass) {
        return getRegisteredPlayers(tClass).stream().filter(iPlayerInfo -> iPlayerInfo.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public <T extends IVeranyPlayer> Optional<T> getPlayer(String name) {
        throw new UnsupportedOperationException("No longer supported!");
    }

    @Override
    public <T extends IVeranyPlayer> List<T> getRegisteredPlayers() {
        throw new UnsupportedOperationException("No longer supported!");
    }

    @Override
    public <T extends IVeranyPlayer> void setPlayer(Class<T> tClass, T player) {
        getRegisteredPlayers(tClass).add(player);
    }

    @Override
    public <T extends IVeranyPlayer> List<T> getRegisteredPlayers(Class<T> tClass) {
        if (!registeredPlayers.containsKey(tClass.getName()))
            registeredPlayers.put(tClass.getName(), new ArrayList<>());
        return registeredPlayers.get(tClass.getName()).stream().map(iVeranyPlayer -> (T) iVeranyPlayer).collect(Collectors.toList());
    }


}
