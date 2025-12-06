package edu.csumb.cst338.otterbots.rockpaperscissors.database.typeConverters;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Description: convert between LocalDateTime and longs for db storage
 * Author: Josh Goldberg
 * Since: 2025.11.26
 */
public class LocalDateTypeConverter {
    /**
     * Convert a LocalDateTime object to a long
     * @param date the date to convert
     * @return a long representing milliseconds since the epoch for the specified datetime
     */
    @TypeConverter
    public long convertDateToLong(LocalDateTime date) {
        ZonedDateTime zdt = ZonedDateTime.of(date, ZoneId.systemDefault());
        return zdt.toInstant().toEpochMilli();
    }

    /**
     * Convert a long to a LocalDateTime object
     * @param epochMilli the milliseconds since the epoch to convert to a LocalDateTime
     * @return a date object specified by the milliseconds since the epoch of the input paramater
     */
    @TypeConverter
    public LocalDateTime convertLongToDate(Long epochMilli) {
        Instant instant = Instant.ofEpochMilli(epochMilli);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
