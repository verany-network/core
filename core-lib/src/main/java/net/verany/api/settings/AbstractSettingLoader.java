package net.verany.api.settings;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSettingLoader extends HashMap<String, Object> {

    public <T> void setSettingValue(AbstractSetting<T> setting, T value) {
        put(setting.getKey(), value);
    }

    public <T> T getSettingValue(AbstractSetting<T> setting) {
        if (!containsKey(setting.getKey()))
            setSettingValue(setting, setting.getDefaultValue());
        return (T) get(setting.getKey());
    }

    public void loadSettings(Map<String, Object> settingMap) {
        settingMap.forEach((s, o) -> {
            AbstractSetting settings = Settings.getSettingByKey(s);
            if (settings != null)
                setSettingValue(settings, o);
        });
    }
}