package ar.edu.itba.paw.model;

import java.time.LocalDateTime;

public final class DateUtils {
    private DateUtils() {
        throw new UnsupportedOperationException();
    }

    public static String getDateFormatted(LocalDateTime date) {
        String dateStr = date.toString();
        String year = dateStr.substring(0, 4);
        String month = dateStr.substring(5, 7);
        String day = dateStr.substring(8, 10);
        return day + "/" + month + "/" + year;
    }

    public static String getTimeFormatted(LocalDateTime date) {
        return date.toString().substring(11, 16);
    }
}
