package net.verany.api.player.permission.duration;

import net.verany.api.season.Season;

import java.util.concurrent.TimeUnit;

public class GroupTime extends AbstractGroupTime {

    public static final AbstractGroupTime MONTHS = new GroupTime(TimeUnit.DAYS, 30, "months");
    public static final AbstractGroupTime HOURS = new GroupTime(TimeUnit.HOURS, "hours");
    public static final AbstractGroupTime DAYS = new GroupTime(TimeUnit.DAYS, "days");
    public static final GroupDuration LIFETIME = new GroupDuration(-1, Long.MAX_VALUE, "lifetime");
    public static final GroupDuration SEASON = new GroupDuration(90, System.currentTimeMillis() - Season.getCurrentSeason().getStart().getTimeInMillis(), "season");

    public GroupTime(TimeUnit timeUnit, String key) {
        super(timeUnit, key);
    }

    public GroupTime(TimeUnit timeUnit, int multiplier, String key) {
        super(timeUnit, multiplier, key);
    }
}
