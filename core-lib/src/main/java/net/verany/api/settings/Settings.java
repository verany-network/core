package net.verany.api.settings;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public abstract class Settings {

    public static final List<AbstractSetting<?>> VALUES = new CopyOnWriteArrayList<>();

    public static List<AbstractSetting<?>> getSettingByCategory(String category) {
        List<AbstractSetting<?>> toReturn = new ArrayList<>();
        VALUES.forEach(abstractSetting -> {
            if (abstractSetting.getCategory().equalsIgnoreCase(category))
                toReturn.add(abstractSetting);
        });
        return toReturn;
    }

    public static <T> AbstractSetting<T> getSettingByKey(String key) {
        for (AbstractSetting<?> value : VALUES)
            if (value.getKey().equalsIgnoreCase(key))
                return (AbstractSetting<T>) value;
        return null;
    }

    public static <T> List<AbstractSetting<T>> getSettingsByClass(String category, Class<T> tClass) {
        List<AbstractSetting<T>> toReturn = new ArrayList<>();
        for (AbstractSetting<?> value : VALUES)
            if ((!category.equals("*") && value.getCategory().equalsIgnoreCase(category)) && value.getTClass().toString().equals(tClass.toString()))
                toReturn.add((AbstractSetting<T>) value);
        return toReturn;
    }

    public static <T> List<AbstractSetting<T>> getSettingByKeys(List<String> keys) {
        List<AbstractSetting<T>> toReturn = new ArrayList<>();
        for (String key : keys)
            toReturn.add(getSettingByKey(key));
        return toReturn;
    }
}
