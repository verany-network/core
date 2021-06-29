package net.verany.api.example.player.setting;

import net.verany.api.settings.AbstractSetting;
import net.verany.api.settings.SettingWrapper;
import org.bukkit.Material;

public class ExampleSetting {

    public static final AbstractSetting<Boolean> EXAMPLE_SETTING = new SettingWrapper<>("example", "category", Boolean.class, true, Material.DIRT);
    public static final AbstractSetting<Integer> EXAMPLE_SETTING_1 = new SettingWrapper<>("example1", "category", Integer.class, 10, Material.GRASS);

}
