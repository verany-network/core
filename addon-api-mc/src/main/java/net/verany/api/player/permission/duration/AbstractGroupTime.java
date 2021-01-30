package net.verany.api.player.permission.duration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public abstract class AbstractGroupTime {
    private final TimeUnit timeUnit;

    public GroupDuration of(int amount) {
        return new GroupDuration(timeUnit.toMillis(amount));
    }

    @AllArgsConstructor
    @Getter
    public static class GroupDuration {
        private final long millis;
    }

}
