package net.verany.api.player.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.verany.api.interfaces.IDefault;
import net.verany.api.module.VeranyProject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public interface IStatsObject extends IDefault<UUID> {

    <T> List<T> getStatsData(AbstractStatsType<T> statsType, StatsTime statsTime);

    <T> List<T> getStatsData(AbstractStatsType<T> statsType, long date);

    <T> void setStatsData(AbstractStatsType<T> statsData, T value);

    int getStatsValue(AbstractStatsType<Integer> statsData, StatsTime time);

    int getStatsValue(AbstractStatsType<Integer> statsData, long date);

    String getKd(int kills, int deaths);

    int getRanking(AbstractStatsType<Integer> points, StatsTime statsTime);

    int getVictoryChance(int playedGames, int wins, StatsTime time);

    @AllArgsConstructor
    @Getter
    enum StatsTime implements Cloneable {
        ALL_TIME(Long.MAX_VALUE),
        MONTHLY(TimeUnit.DAYS.toMillis(30)),
        WEEKLY(TimeUnit.DAYS.toMillis(7)),
        DAILY(TimeUnit.DAYS.toMillis(1));

        @Setter
        private long time;

        @SneakyThrows
        public StatsTime of(int amount) {
            StatsTime toReturn = (StatsTime) clone();
            long currentTime = toReturn.getTime();
            toReturn.setTime(currentTime * amount);
            return toReturn;
        }
    }


}
