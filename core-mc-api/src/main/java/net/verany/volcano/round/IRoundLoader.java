package net.verany.volcano.round;

import lombok.Getter;
import net.verany.api.loader.database.DatabaseLoadObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface IRoundLoader {

    void load();

    void update();

    List<RoundInfo> getRounds();

    List<RoundInfo> getRounds(UUID uuid);

    void addRound(RoundInfo info);

    RoundInfo getRound(String roundId);

    @Getter
    class RoundLoaderObject extends DatabaseLoadObject {

        private final List<RoundInfo> rounds = new ArrayList<>();

        public RoundLoaderObject() {
            super("rounds");
        }

        public RoundInfo getRound(String id) {
            return rounds.stream().filter(roundInfo -> roundInfo.getRoundId().equals(id)).findFirst().orElse(null);
        }
    }

}
