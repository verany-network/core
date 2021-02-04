package net.verany.api.player.permission.duration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.verany.api.AbstractVerany;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Getter
public abstract class AbstractGroupTime {
    private final TimeUnit timeUnit;
    private final String key;

    public GroupDuration of(int amount) {
        return new GroupDuration(amount, timeUnit.toMillis(amount), key);
    }

    @AllArgsConstructor
    @Getter
    public static class GroupDuration {
        private final int amount;
        private final long millis;
        private final String key;
    }

}
