package net.verany.volcano.round;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

@Builder
@Setter
@Getter
public class RoundInfo {

    private final String roundId;
    @Builder.Default
    private final long start = System.currentTimeMillis();
    private long end;
    private final List<UUID> players;
    private final String mapName;
    private final Document properties;
    private final String gameMode;

}
