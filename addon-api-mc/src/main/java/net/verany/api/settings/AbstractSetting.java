package net.verany.api.settings;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public abstract class AbstractSetting<T> {
    private final String key;
    private final String category;
    private final Class<T> tClass;
    private final Material material;
    private final T defaultValue;
    private boolean inInventory = true;

    public AbstractSetting(String key, String category, Class<T> tClass, T defaultValue, Material material) {
        this.key = key;
        this.category = category;
        this.tClass = tClass;
        this.material = material;
        this.defaultValue = defaultValue;
    }

    public AbstractSetting(String key, String category, Class<T> tClass, T defaultValue, Material material, boolean inInventory) {
        this.key = key;
        this.category = category;
        this.tClass = tClass;
        this.material = material;
        this.defaultValue = defaultValue;
        this.inInventory = inInventory;
    }
}
