package net.verany.api.settings;

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

    public static class TempSettingWrapper<T> extends AbstractSetting<T> {

        public TempSettingWrapper(String key, Class<T> tClass, T defaultValue) {
            super(key, "temp_" + key, tClass, defaultValue, Material.AIR);
        }
    }
}
