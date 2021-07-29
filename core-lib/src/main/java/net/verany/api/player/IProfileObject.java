package net.verany.api.player;

import net.verany.api.interfaces.IDefault;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IProfileObject {

    <T extends IVeranyPlayer> Optional<T> getPlayer(UUID uuid, Class<T> tClass);

    @Deprecated
    <T extends IVeranyPlayer> Optional<T> getPlayer(UUID uuid);

    <T extends IVeranyPlayer> Optional<T> getPlayer(String name, Class<T> tClass);

    @Deprecated
    <T extends IVeranyPlayer> Optional<T> getPlayer(String name);

    <T extends IVeranyPlayer> List<T> getRegisteredPlayers(Class<T> tClass);

    @Deprecated
    <T extends IVeranyPlayer> List<T> getRegisteredPlayers();

    <T extends IVeranyPlayer> void setPlayer(Class<T> tClass, T player);

}
