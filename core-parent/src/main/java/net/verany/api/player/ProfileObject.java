package net.verany.api.player;

import lombok.Getter;
import net.verany.api.AbstractVerany;

import java.util.*;

@Getter
public class ProfileObject implements IProfileObject {

    @Override
    public <T extends IVeranyPlayer> Optional<T> getPlayer(UUID uuid, Class<T> tClass) {
        return getRegisteredPlayers(tClass).stream().filter(iPlayerInfo -> iPlayerInfo.getUniqueId().equals(uuid)).findFirst();
    }

    @Override
    public <T extends IVeranyPlayer> Optional<T> getPlayer(UUID uuid) {
        throw new UnsupportedOperationException("No longer supported!");
    }

    @Override
    public <T extends IVeranyPlayer> Optional<T> getPlayer(String name, Class<T> tClass) {;
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
        //registeredPlayers.get(tClass.getName()).add(player);
    }

    @Override
    public <T extends IVeranyPlayer> List<T> getRegisteredPlayers(Class<T> tClass) {
        return AbstractVerany.getPlayers(tClass);
        /*if (!registeredPlayers.containsKey(tClass.getName()))
            registeredPlayers.put(tClass.getName(), new ArrayList<>());
        List<T> toReturn = new ArrayList<>();
        for (IVeranyPlayer iVeranyPlayer : registeredPlayers.get(tClass.getName())) {
            toReturn.add((T) iVeranyPlayer);
        }
        return toReturn;*/
        //return registeredPlayers.get(tClass.getName()).stream().map(iVeranyPlayer -> (T) iVeranyPlayer).collect(Collectors.toList());
    }


}
