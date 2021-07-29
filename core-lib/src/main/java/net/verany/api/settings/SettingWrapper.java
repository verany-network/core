package net.verany.api.settings;

public class SettingWrapper<T> extends AbstractSetting<T> {
    public SettingWrapper(String key, String category, Class<T> tClass, T defaultValue) {
        super(key, category, tClass, defaultValue);
        Settings.VALUES.add(this);
    }

    public SettingWrapper(String key, String category, Class<T> tClass, T defaultValue,boolean inInventory) {
        super(key, category, tClass, defaultValue, inInventory);
        Settings.VALUES.add(this);
    }

    public static class TempSettingWrapper<T> extends AbstractSetting<T> {

        public TempSettingWrapper(String key, Class<T> tClass, T defaultValue) {
            super(key, "temp_" + key, tClass, defaultValue);
        }
    }
}
