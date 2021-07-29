package net.verany.api.settings;

import lombok.Getter;

@Getter
public abstract class AbstractSetting<T> {
    private final String key;
    private final String category;
    private final Class<T> tClass;
    private final T defaultValue;
    private boolean inInventory = true;

    public AbstractSetting(String key, String category, Class<T> tClass, T defaultValue) {
        this.key = key;
        this.category = category;
        this.tClass = tClass;
        this.defaultValue = defaultValue;
    }

    public AbstractSetting(String key, String category, Class<T> tClass, T defaultValue, boolean inInventory) {
        this.key = key;
        this.category = category;
        this.tClass = tClass;
        this.defaultValue = defaultValue;
        this.inInventory = inInventory;
    }
}
