package net.verany.api.player.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class AbstractStatsType<T> {

    private final String key;
    private final Class<T> tClass;

}
