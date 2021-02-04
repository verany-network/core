package net.verany.api.setting;

import net.verany.api.settings.AbstractSetting;
import org.bukkit.Material;

public class SettingWrapper<T> extends AbstractSetting<T> {
    public SettingWrapper(String key, String category, Class<T> tClass, T defaultValue, Material material) {
        super(key, category, tClass, defaultValue, material);
        Settings.VALUES.add(this);
    }

    public SettingWrapper(String key, String category, Class<T> tClass, T defaultValue, Material material, boolean inInventory) {
        super(key, category, tClass, defaultValue, material, inInventory);
        Settings.VALUES.add(this);
    }
}
