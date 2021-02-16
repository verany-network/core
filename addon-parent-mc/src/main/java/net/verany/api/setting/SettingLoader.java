package net.verany.api.setting;

import net.verany.api.plugin.AbstractSettingLoader;
import net.verany.api.settings.AbstractSetting;

import java.util.Map;

public abstract class SettingLoader extends AbstractSettingLoader {

    @Override
    public <T> void setSettingValue(AbstractSetting<T> setting, T value) {
        put(setting.getKey(), value);
    }

    @Override
    public <T> T getSettingValue(AbstractSetting<T> setting) {
        if (!containsKey(setting.getKey()))
            setSettingValue(setting, setting.getDefaultValue());
        return (T) get(setting.getKey());
    }

    @Override
    public void loadSettings(Map<String, Object> settingMap) {
        settingMap.forEach((s, o) -> {
            AbstractSetting settings = Settings.getSettingByKey(s);
            if (settings != null)
                setSettingValue(settings, o);
        });
    }

}
