package net.verany.api.sound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Sound;

@AllArgsConstructor
@Getter
public abstract class AbstractVeranySound {

    private final String key;
    private final Sound sound;
    private final float volume;
    private final float pitch;

}
