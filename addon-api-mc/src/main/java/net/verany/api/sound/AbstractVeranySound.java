package net.verany.api.sound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public abstract class AbstractVeranySound {

    private final String key;
    private final Sound sound;
    private final float volume;
    private final float pitch;
    private Material material;

}
