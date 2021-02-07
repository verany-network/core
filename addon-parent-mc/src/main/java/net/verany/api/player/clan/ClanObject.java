package net.verany.api.player.clan;

import lombok.Getter;
import lombok.NonNull;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.clan.data.IClanData;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class ClanObject extends DatabaseLoader implements IClanObject {

    private UUID key;

    public ClanObject(VeranyProject project, String collection, String database) {
        super(project, collection, database);
    }

    @Override
    public void load(UUID key) {
        this.key = key;
    }

    @Override
    public void update() {

    }

    @Override
    public String getClanId() {
        return null;
    }

    @Override
    public String getClanRank() {
        return null;
    }

    @Override
    public IClanData getClanData() {
        return null;
    }

    @Override
    public void setClanData(@NonNull IClanData clanData) {

    }
}
