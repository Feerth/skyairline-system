package com.skyairlines.util;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {

    private static final Locale SPANISH_LOCALE = new Locale("es", "ES");
    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    private DateUtils() {
    }

    public static String formatDateFull(OffsetDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        ZonedDateTime zdt = dateTime.atZoneSameInstant(DEFAULT_ZONE);
        return zdt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy", SPANISH_LOCALE)).toUpperCase(SPANISH_LOCALE);
    }

    public static String formatDateShort(OffsetDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        ZonedDateTime zdt = dateTime.atZoneSameInstant(DEFAULT_ZONE);
        return zdt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String formatTime(OffsetDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        ZonedDateTime zdt = dateTime.atZoneSameInstant(DEFAULT_ZONE);
        return zdt.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public static String formatDateTime(OffsetDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        ZonedDateTime zdt = dateTime.atZoneSameInstant(DEFAULT_ZONE);
        return zdt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public static String getCurrentDateFormatted() {
        return formatDateFull(OffsetDateTime.now());
    }

    public static OffsetDateTime parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, SPANISH_LOCALE);
        return OffsetDateTime.parse(dateStr, formatter);
    }
}