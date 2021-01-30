package net.verany.api.player.party;

import lombok.Getter;
import net.verany.api.Verany;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.party.data.Party;

import java.util.UUID;

@Getter
public class PartyObject extends DatabaseLoader implements IPartyObject{

    private UUID key;

    private IParty party = null;

    public PartyObject(VeranyProject project) {
        super(project, "party");
    }

    @Override
    public void load(UUID key) {
        this.key = key;
    }

    @Override
    public void update() {

    }


    @Override
    public void createParty() {

    }

    @Override
    public void invite(UUID uuid) {

    }

    @Override
    public void accept(UUID uuid) {

    }

    @Override
    public boolean hasParty() {
        return false;
    }

    @Override
    public boolean isInParty() {
        return false;
    }

    @Override
    public void leave() {

    }

    @Override
    public void dissolve() {

    }

}
