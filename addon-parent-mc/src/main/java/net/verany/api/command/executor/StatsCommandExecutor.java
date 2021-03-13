package net.verany.api.command.executor;

import lombok.*;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.stats.AbstractStatsType;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class StatsCommandExecutor {

    private final Map<String, String> statsKeyMap = new HashMap<>();
    @NonNull
    private final VeranyProject project;
    private String header;

    public abstract void onExecute(IPlayerInfo player, IPlayerInfo target);

    public <T> void addStatsCategory(AbstractStatsType<T> statsType, String key) {
        statsKeyMap.put(statsType.getKey(), key);
    }

}
