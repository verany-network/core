package net.verany.api.player.stats;

public class StatsType<T> extends AbstractStatsType<T> {

    public StatsType(String key, Class<T> tClass) {
        super(key, tClass);
    }
}
