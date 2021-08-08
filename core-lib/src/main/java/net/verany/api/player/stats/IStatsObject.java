package net.verany.api.player.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.verany.api.interfaces.IDefault;
import net.verany.api.interfaces.VeranyCloneable;
import net.verany.api.player.IVeranyPlayer;
import net.verany.api.player.permission.duration.AbstractGroupTime;
import net.verany.api.player.permission.duration.GroupTime;

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

    int getRanking(AbstractStatsType<Integer> points, long statsTime, Class<? extends IDefault<UUID>> player);

    int getVictoryChance(int playedGames, int wins);

    @Getter
    enum StatsTime {
        ALL_TIME(GroupTime.LIFETIME),
        MONTHLY(GroupTime.MONTHS.of(1)),
        WEEKLY(GroupTime.DAYS.of(7)),
        DAILY(GroupTime.DAYS.of(1)),
        SEASON(GroupTime.SEASON);

        @Setter
        private AbstractGroupTime.GroupDuration time;

        StatsTime(AbstractGroupTime.GroupDuration time) {
            this.time = time;
        }

        public StatsTime of(int amount) {
            setTime(new AbstractGroupTime.GroupDuration(amount, time.getMillis() * amount, time.getKey()));
            return this;
        }

    }


}
