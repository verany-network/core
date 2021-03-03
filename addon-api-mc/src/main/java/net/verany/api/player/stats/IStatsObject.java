package net.verany.api.player.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.interfaces.IDefault;
import net.verany.api.module.VeranyProject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public interface IStatsObject extends IDefault<UUID> {

    <T> List<T> getStatsData(AbstractStatsType<T> statsType, StatsTime statsTime);

    <T> void setStatsData(AbstractStatsType<T> statsData, T value);

    int getStatsValue(AbstractStatsType<Integer> statsData, StatsTime time);

    @AllArgsConstructor
    @Getter
    enum StatsTime {
        ALL_TIME(Long.MAX_VALUE),
        MONTHLY(TimeUnit.DAYS.toMillis(30)),
        WEEKLY(TimeUnit.DAYS.toMillis(7)),
        DAILY(TimeUnit.DAYS.toMillis(1));

        private final long time;
    }


}
