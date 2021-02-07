package net.verany.api.stats;

import lombok.Getter;

/**
 * @param <T> module of your project
 */
@Getter
public abstract class AbstractStatsData<T> {

    private final Class<T> tClass;
    private final String key;
    private final T defaultValue;
    private final String category;

    /**
     * Constructor of stats
     *
     * @param tClass       class of your stats type
     * @param key          key of your stats
     * @param defaultValue default value of {@code tClass}
     * @param category     category of your project
     */
    public AbstractStatsData(Class<T> tClass, String key, T defaultValue, String category) {
        this.tClass = tClass;
        this.key = key;
        this.defaultValue = defaultValue;
        this.category = category;
    }
}
