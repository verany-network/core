package net.verany.api.player.permission.duration;

import java.util.concurrent.TimeUnit;

public class GroupTime extends AbstractGroupTime {

    public static final AbstractGroupTime HOURS = new GroupTime(TimeUnit.HOURS);
    public static final AbstractGroupTime DAYS = new GroupTime(TimeUnit.DAYS);
    public static final GroupDuration LIFETIME = new GroupDuration(-1);

    public GroupTime(TimeUnit timeUnit) {
        super(timeUnit);
    }

}
