package net.verany.api.season;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Getter
public enum Season {

    SPRING(new GregorianCalendar(Year.now().getValue(), Calendar.APRIL, 20, 0, 0, 0), new GregorianCalendar(Year.now().getValue(), Calendar.JUNE, 21, 0, 0)),
    SUMMER(new GregorianCalendar(Year.now().getValue(), Calendar.JUNE, 21, 0, 0), new GregorianCalendar(Year.now().getValue(), Calendar.SEPTEMBER, 22, 0, 0)),
    FALL(new GregorianCalendar(Year.now().getValue(), Calendar.SEPTEMBER, 22, 0, 0), new GregorianCalendar(Year.now().getValue(), Calendar.DECEMBER, 21, 0, 0)),
    WINTER(new GregorianCalendar(Year.now().getValue(), Calendar.DECEMBER, 21, 0, 0), new GregorianCalendar(Year.now().getValue() + 1, Calendar.APRIL, 20, 0, 0, 0)),
    WINTER2(new GregorianCalendar(Year.now().getValue() - 1, Calendar.DECEMBER, 21, 0, 0), new GregorianCalendar(Year.now().getValue(), Calendar.APRIL, 20, 0, 0, 0)),
    ERROR(null, null);

    private final Calendar start, end;

    Season(Calendar start, Calendar end) {
        this.start = start;
        this.end = end;

        /*if (start == null || end == null) return;

        int year = Year.now().getValue();
        this.start.set(Calendar.YEAR, year);
        this.end.set(Calendar.YEAR, year + (start.get(Calendar.MONTH) == Calendar.DECEMBER ? 1 : 0));*/
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
        if (start == null || end == null) return false;
        return getCurrentDate().after(start.getTime()) && getCurrentDate().before(end.getTime());
    }

    private static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

}
