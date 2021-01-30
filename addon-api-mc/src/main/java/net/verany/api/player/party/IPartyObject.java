package net.verany.api.player.party;

import net.verany.api.player.IPlayerLoader;

import java.util.UUID;

public interface IPartyObject extends IPlayerLoader {

    void createParty();

    void invite(UUID uuid);

    void accept(UUID uuid);

    boolean hasParty();

    boolean isInParty();

    void leave();

    void dissolve();

    IParty getParty();

}
