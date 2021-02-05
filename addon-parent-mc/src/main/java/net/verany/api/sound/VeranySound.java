package net.verany.api.sound;

import org.bukkit.Sound;

public class VeranySound extends AbstractVeranySound {

    public static AbstractVeranySound INVENTORY_OPEN = new VeranySound("inventory_open", Sound.BLOCK_BAMBOO_HIT, 1, 1);
    public static AbstractVeranySound INVENTORY_CLOSE = new VeranySound("inventory_close", Sound.BLOCK_BAMBOO_FALL, 1, 1);
    public static AbstractVeranySound INVENTORY_NAVIGATION = new VeranySound("inventory_navigation", Sound.BLOCK_BAMBOO_PLACE, 1, 1);

    public VeranySound(String key, Sound sound, float volume, float pitch) {
        super(key, sound, volume, pitch);
    }
}
