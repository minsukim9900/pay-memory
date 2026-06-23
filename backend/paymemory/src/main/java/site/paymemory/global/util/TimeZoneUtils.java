package site.paymemory.global.util;

import java.time.ZoneId;

public final class TimeZoneUtils {

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Seoul");

    private TimeZoneUtils() {
    }

    public static ZoneId parseZoneId(String timeZone) {
        try {
            if (timeZone == null || timeZone.isBlank()) {
                return DEFAULT_ZONE_ID;
            }

            return ZoneId.of(timeZone);
        } catch (Exception e) {
            return DEFAULT_ZONE_ID;
        }
    }
}