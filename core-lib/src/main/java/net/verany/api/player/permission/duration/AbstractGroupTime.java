package net.verany.api.player.permission.duration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public abstract class AbstractGroupTime {
    private final TimeUnit timeUnit;
    private final String key;
    private final int multiplier;

    public AbstractGroupTime(TimeUnit timeUnit, int multiplier, String key) {
        this.timeUnit = timeUnit;
        this.key = key;
        this.multiplier = multiplier;
    }

    public AbstractGroupTime(TimeUnit timeUnit, String key) {
        this(timeUnit, 1, key);
    }

    public GroupDuration of(int amount) {
        return new GroupDuration(amount, timeUnit.toMillis(amount) * multiplier, key);
    }

    @AllArgsConstructor
    @Getter
    public static class GroupDuration {
        private final Integer amount;
        private final Long millis;
        private final String key;
    }

}
