package net.verany.api.player.stats;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.verany.api.Verany;
import net.verany.api.loader.database.DatabaseLoadObject;
import net.verany.api.loader.database.DatabaseLoader;
import net.verany.api.module.VeranyProject;
import net.verany.api.player.IPlayerInfo;
import net.verany.api.player.IVeranyPlayer;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class StatsObject extends DatabaseLoader implements IStatsObject {

    private static final Map<UUID, IStatsObject> cachedObjects = new HashMap<>();

    private UUID uniqueId;

    public StatsObject(VeranyProject project) {
        super(project, "stats");
    }

    @Override
    public void load(UUID key) {
        this.uniqueId = key;

        load(new LoadInfo<>("stats_data", StatsLoadData.class, new StatsLoadData(key)));
    }

    @Override
    public void update() {
        save("stats_data");
    }

    @Override
    public <T> List<T> getStatsData(AbstractStatsType<T> statsType, StatsTime statsTime) {
        List<T> toReturn = new ArrayList<>();
        checkStats(statsType);
        for (StatsLoadData.StatsData statsData : getData(StatsLoadData.class).getStringStatsDataMap().get(statsType.getKey()))
            if ((System.currentTimeMillis() - statsData.getTimestamp()) < statsTime.getTime())
                toReturn.add(Verany.GSON.fromJson(statsData.getValue(), statsType.getTClass()));
        return toReturn;
    }

    @Override
    public <T> List<T> getStatsData(AbstractStatsType<T> statsType, long date) {
        List<T> toReturn = new ArrayList<>();
        checkStats(statsType);
        for (StatsLoadData.StatsData statsData : getData(StatsLoadData.class).getStringStatsDataMap().get(statsType.getKey()))
            if (statsData.getTimestamp() >= date)
                toReturn.add(Verany.GSON.fromJson(statsData.getValue(), statsType.getTClass()));
        return toReturn;
    }

    @Override
    public <T> void setStatsData(AbstractStatsType<T> statsType, T value) {
        if (getDataOptional(StatsLoadData.class).isEmpty()) return;
        checkStats(statsType);
        getDataOptional(StatsLoadData.class).get().getStringStatsDataMap().get(statsType.getKey()).add(new StatsLoadData.StatsData(Verany.GSON.toJson(value)));
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
    public int getStatsValue(AbstractStatsType<Integer> statsData, long date) {
        int toReturn = 0;
        try {
            for (Double statsDatum : getStatsData(new StatsType<>(statsData.getKey(), Double.class), date))
                toReturn += statsDatum;
        } catch (ClassCastException exception) {
            for (Integer statsDatum : getStatsData(new StatsType<>(statsData.getKey(), Integer.class), date))
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
    public int getRanking(AbstractStatsType<Integer> statsType, StatsTime statsTime, Class<? extends IVeranyPlayer> playerClass) {
        int rank = 1;
        for (IStatsObject statsObject : getStatsObjects(getProject(), statsType, statsTime, playerClass)) {
            if (statsObject.getUniqueId().equals(uniqueId)) break;
            rank++;
        }
        return rank;
    }

    @Override
    public int getVictoryChance(int playedGames, int wins) {
        int percent = 0;
        if (playedGames != 0)
            percent = ((wins * 100) / playedGames);
        return percent;
    }

    private <T> void checkStats(AbstractStatsType<T> statsType) {
        if (getDataOptional(StatsLoadData.class).isEmpty()) return;
        if (!getDataOptional(StatsLoadData.class).get().getStringStatsDataMap().containsKey(statsType.getKey()))
            getDataOptional(StatsLoadData.class).get().getStringStatsDataMap().put(statsType.getKey(), new ArrayList<>());
    }

    @Getter
    public static class StatsLoadData extends DatabaseLoadObject {

        private final Map<String, List<StatsData>> stringStatsDataMap = new HashMap<>();

        public StatsLoadData(UUID uuid) {
            super(uuid);
        }

        @RequiredArgsConstructor
        @Getter
        public static class StatsData {
            private final String value;
            private final long timestamp = System.currentTimeMillis();
        }

    }

    public static List<IStatsObject> getStatsObjects(VeranyProject project, AbstractStatsType<Integer> statsType, IStatsObject.StatsTime statsTime, Class<? extends IVeranyPlayer> playerClass) {
        List<IPlayerInfo> players = Verany.PROFILE_OBJECT.getRegisteredPlayers(IPlayerInfo.class);
        List<IStatsObject> sortData = new ArrayList<>();
        for (IPlayerInfo player : players) {
            if (player.getSkinData() == null) continue;
            IStatsObject statsObject;
            if (player.getPlayer(playerClass) != null) {
                statsObject = player.getPlayer(playerClass).getStatsObject();
            } else {
                if (cachedObjects.containsKey(player.getUniqueId()))
                    statsObject = cachedObjects.get(player.getUniqueId());
                else {
                    statsObject = new StatsObject(project);
                    statsObject.load(player.getUniqueId());
                    cachedObjects.put(player.getUniqueId(), statsObject);
                }
            }
            sortData.add(statsObject);
        }
        List<IStatsObject> statsObjects = Verany.sortList(sortData.stream().distinct().collect(Collectors.toList()), Comparator.comparingInt(value -> value.getStatsValue(statsType, statsTime)));
        Collections.reverse(statsObjects);
        return statsObjects;
    }

}
