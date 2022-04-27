package net.verany.volcano.round;

import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RoundLoader extends DatabaseLoader implements IRoundLoader {

    public RoundLoader(VeranyProject project) {
        super(project, "rounds");
    }

    @Override
    public void load() {
        load(new LoadInfo<>("rounds", RoundLoaderObject.class, new RoundLoaderObject()));
    }

    @Override
    public void update() {
        save("rounds");
    }

    @Override
    public List<RoundInfo> getRounds() {
        if (getDataOptional(RoundLoaderObject.class).isEmpty()) return new ArrayList<>();
        return getDataOptional(RoundLoaderObject.class).get().getRounds();
    }

    @Override
    public List<RoundInfo> getRounds(UUID uuid) {
        if (getRounds().isEmpty()) return new ArrayList<>();
        return getRounds().stream().filter(roundInfo -> roundInfo.getPlayers().contains(uuid)).collect(Collectors.toList());
    }

    @Override
    public void addRound(RoundInfo info) {
        if (getDataOptional(RoundLoaderObject.class).isEmpty()) return;
        getDataOptional(RoundLoaderObject.class).get().getRounds().add(info);
    }

    @Override
    public RoundInfo getRound(String roundId) {
        if (getDataOptional(RoundLoaderObject.class).isEmpty()) return null;
        return getDataOptional(RoundLoaderObject.class).get().getRound(roundId);
    }
}
