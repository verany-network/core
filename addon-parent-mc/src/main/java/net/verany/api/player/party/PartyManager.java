package net.verany.api.player.party;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class PartyManager {

    private final List<IParty> parties = new ArrayList<>();

    public void addParty(IParty party) {
        parties.add(party);
    }

    public void removeParty(IParty party) {
        parties.remove(getParty(party.getId()));
    }

    public IParty getParty(UUID leader) {
        for (IParty party : parties)
            if (party.getLeader().getUniqueId().equals(leader))
                return party;
        return null;
    }

    public IParty getParty(String id) {
        for (IParty party : parties)
            if (party.getId().equals(id))
                return party;
        return null;
    }

}
