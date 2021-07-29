package net.verany.api.season;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Getter
public enum Season {

    SPRING(new GregorianCalendar(2021, Calendar.APRIL, 20, 0, 0, 0), new GregorianCalendar(2021, Calendar.JUNE, 21, 0, 0)),
    SUMMER(new GregorianCalendar(2021, Calendar.JUNE, 21, 0, 0), new GregorianCalendar(2021, Calendar.SEPTEMBER, 22, 0, 0)),
    FALL(new GregorianCalendar(2021, Calendar.SEPTEMBER, 22, 0, 0), new GregorianCalendar(2021, Calendar.DECEMBER, 21, 0, 0)),
    WINTER(new GregorianCalendar(2021, Calendar.DECEMBER, 21, 0, 0), new GregorianCalendar(2021, Calendar.APRIL, 20, 0, 0, 0)),
    ERROR(null, null);

    private final Calendar start, end;

    Season(Calendar start, Calendar end) {
        this.start = start;
        this.end = end;

        if (start == null || end == null) return;

        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(System.currentTimeMillis()));
        this.start.set(Calendar.YEAR, year);
        this.end.set(Calendar.YEAR, year + (start.get(Calendar.MONTH) == Calendar.DECEMBER ? 1 : 0));
    }

    public static Season getCurrentSeason() {
        Season toReturn = ERROR;
        for (Season value : values()) {
            if (value.isBetween()) {
                toReturn = value;
                break;
            }
        }
        return toReturn;
    }

    private boolean isBetween() {
        return getCurrentDate().after(start.getTime()) && getCurrentDate().before(end.getTime());
    }

    private static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

}
