package net.verany.api.plugin;

import net.verany.api.settings.AbstractSetting;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSettingLoader extends HashMap<String, Object> {

    public abstract <T> void setSettingValue(AbstractSetting<T> setting, T value);

    public abstract <T> T getSettingValue(AbstractSetting<T> setting);

    public abstract void loadSettings(Map<String, Object> settingMap);
}
