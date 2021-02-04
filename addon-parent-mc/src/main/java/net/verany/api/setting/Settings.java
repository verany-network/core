package net.verany.api.setting;

import lombok.Getter;
import net.verany.api.settings.AbstractSetting;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public abstract class Settings {

    public static final List<AbstractSetting<?>> VALUES = new CopyOnWriteArrayList<>();

    public static final AbstractSetting<Boolean> JUKEBOX = new SettingWrapper<>("jukebox", "SOUND", Boolean.class, true, Material.JUKEBOX, true);
    public static final AbstractSetting<Boolean> MUSIC_SPECIAL_SOUNDS = new SettingWrapper<>("music_special", "SOUND", Boolean.class, true, Material.NOTE_BLOCK, true);
    public static final AbstractSetting<Boolean> INVENTORY_SOUNDS = new SettingWrapper<>("inventory_sounds", "SOUND", Boolean.class, true, Material.CHEST, true);
    //public static final AbstractSetting<Boolean> TELEPORT_SOUND = new SettingWrapper<>("teleportSound", "sounds", Boolean.class, true, Material.CHEST, true);



    public static List<AbstractSetting<?>> getSettingByCategory(String category) {
        List<AbstractSetting<?>> toReturn = new ArrayList<>();
        VALUES.forEach(abstractChunkSetting -> {
            if (abstractChunkSetting.getCategory().equalsIgnoreCase(category))
                toReturn.add(abstractChunkSetting);
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
