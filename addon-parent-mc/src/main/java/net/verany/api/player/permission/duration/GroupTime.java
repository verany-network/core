package net.verany.api.player.permission.duration;

import java.util.concurrent.TimeUnit;

public class GroupTime extends AbstractGroupTime {

    public static final AbstractGroupTime HOURS = new GroupTime(TimeUnit.HOURS, "Hours");
    public static final AbstractGroupTime DAYS = new GroupTime(TimeUnit.DAYS, "days");
    public static final GroupDuration LIFETIME = new GroupDuration(-1, -1, "lifetime");

    public GroupTime(TimeUnit timeUnit, String key) {
        super(timeUnit, key);
    }
}
