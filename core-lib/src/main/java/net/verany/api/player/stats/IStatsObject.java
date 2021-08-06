package net.verany.api.player.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.verany.api.interfaces.IDefault;
import net.verany.api.interfaces.VeranyCloneable;
import net.verany.api.player.IVeranyPlayer;

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

    int getRanking(AbstractStatsType<Integer> points, StatsTime statsTime, Class<? extends IDefault<UUID>> player);

    int getVictoryChance(int playedGames, int wins);

    @Getter
    enum StatsTime {
        ALL_TIME(Long.MAX_VALUE),
        MONTHLY(TimeUnit.DAYS.toMillis(30)),
        WEEKLY(TimeUnit.DAYS.toMillis(7)),
        DAILY(TimeUnit.DAYS.toMillis(1)),
        SEASON(1_0);

        @Setter
        private long time;
        private final long defaultTime;

        StatsTime(long time) {
            this.time = time;
            this.defaultTime = time;
        }

        public StatsTime of(int amount) {
            setTime(defaultTime * amount);
            return this;
        }

    }


}
