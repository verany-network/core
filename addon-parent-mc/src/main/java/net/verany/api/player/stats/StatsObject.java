package net.verany.api.player.stats;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.verany.api.Verany;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;

import java.util.*;

@Getter
public class StatsObject extends DatabaseLoader implements IStatsObject {

    private UUID uniqueId;

    public StatsObject(VeranyProject project) {
        super(project, "stats");
    }

    @Override
    public void load(UUID key) {
        this.uniqueId = key;

        load(new LoadInfo<>("stats_data", StatsLoadData.class, new StatsLoadData(key.toString())));
    }

    @Override
    public void update() {
        save("stats_data");
        load(uniqueId);
    }

    @Override
    public <T> List<T> getStatsData(AbstractStatsType<T> statsType, StatsTime statsTime) {
        List<T> toReturn = new ArrayList<>();
        checkStats(statsType);
        for (StatsLoadData.StatsData statsData : getData(StatsLoadData.class).getStringStatsDataMap().get(statsType.getKey()))
            if ((System.currentTimeMillis() - statsData.getTimestamp()) < statsTime.getTime())
                toReturn.add((T) statsData.getValue());
        return toReturn;
    }

    @Override
    public <T> void setStatsData(AbstractStatsType<T> statsType, T value) {
        checkStats(statsType);
        getData(StatsLoadData.class).getStringStatsDataMap().get(statsType.getKey()).add(new StatsLoadData.StatsData(value));
    }

    @Override
    public int getStatsValue(AbstractStatsType<Integer> statsData, StatsTime time) {
        int toReturn = 0;
        try {
            for (Double statsDatum : getStatsData(new StatsType<>(statsData.getKey(), Double.class), time))
                toReturn += statsDatum;
        } catch (ClassCastException exception) {
            for (Integer statsDatum : getStatsData(new StatsType<>(statsData.getKey(), Integer.class), time))
                toReturn += statsDatum;
        }
        return toReturn;
    }

    @Override
    public String getKd(int kills, int deaths) {
        String kd;
        if (deaths == 0)
            kd = kills + ".0";
        else {
            double KD = (double) kills / (double) deaths;
            KD *= 100.0D;
            KD = Math.round(KD);
            KD /= 100.0D;
            kd = String.valueOf(KD);
        }
        kd = kd.replace("NaN", "0.0").replace("Infinity", "0.0");
        return kd;
    }

    @Override
    public int getRanking(AbstractStatsType<Integer> points, StatsTime statsTime) {
        return 0;
    }

    @Override
    public int getVictoryChance(int playedGames, int wins, StatsTime time) {
        int percent = 0;
        if (playedGames != 0)
            percent = ((wins * 100) / playedGames);
        return percent;
    }

    private <T> void checkStats(AbstractStatsType<T> statsType) {
        if (!getData(StatsLoadData.class).getStringStatsDataMap().containsKey(statsType.getKey()))
            getData(StatsLoadData.class).getStringStatsDataMap().put(statsType.getKey(), new ArrayList<>());
    }

    @Getter
    public static class StatsLoadData extends DatabaseLoadObject {

        private final Map<String, List<StatsData>> stringStatsDataMap = new HashMap<>();

        public StatsLoadData(String uuid) {
            super(uuid);
        }

        @RequiredArgsConstructor
        @Getter
        public static class StatsData {
            private final Object value;
            private final long timestamp = System.currentTimeMillis();
        }

    }
}
