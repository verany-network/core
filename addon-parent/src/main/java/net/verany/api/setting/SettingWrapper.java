package net.verany.api.setting;

import net.verany.api.settings.AbstractSetting;
import net.verany.api.settings.Settings;

public class SettingWrapper<T> extends AbstractSetting<T> {
    public SettingWrapper(String key, String category, Class<T> tClass,T defaultValue) {
        super(key, category, tClass, defaultValue);
        Settings.VALUES.add(this);
    }

    public SettingWrapper(String key, String category, Class<T> tClass, T defaultValue, boolean inInventory) {
        super(key, category, tClass, defaultValue, inInventory);
        Settings.VALUES.add(this);
    }
}
