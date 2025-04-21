package com.example.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

public class TimeUtils {

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();


    public static ZoneId getDefaultZoneId() {
        return DEFAULT_ZONE_ID;
    }

    public static LocalDateTime toLocalDateTime(ZonedDateTime zonedDateTime) {
        return Optional.ofNullable(zonedDateTime)
                .map(zdt -> zdt.withZoneSameInstant(DEFAULT_ZONE_ID).toLocalDateTime())
                .orElse(null);
    }
}
