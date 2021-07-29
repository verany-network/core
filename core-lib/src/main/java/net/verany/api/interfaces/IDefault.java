package net.verany.api.interfaces;

import net.verany.api.player.stats.IStatsObject;

public interface IDefault<T> {

    void load(T key);

    void update();

    T getUniqueId();

    default IStatsObject getStatsObject() {
        return null;
    }

}
