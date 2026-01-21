package org.bytesync.hotelmanagement.util;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class EntityOperationUtils {

    public static<T,ID> T safeCall(Optional<T> entity, String domain, ID id) {
        return entity.orElseThrow(() -> new EntityNotFoundException("There is no %s with id %s".formatted(domain, id)));
    }

    public static String timeFormat(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("HH:mm:ss").format(localDateTime);
    }

    public static String dateFormat(LocalDate date) {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy").format(date);
    }

    public static String dateTimeFormat(LocalDateTime dateTime) {
        return DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").format(dateTime);
    }

    public static int getDaysBetween(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    public static LocalDateTime getCurrentYangonZoneLocalDateTime() {
        Instant instant = Instant.now();
        ZoneId yangonZone = ZoneId.of("Asia/Yangon");

        return instant.atZone(yangonZone).toLocalDateTime();
    }
}
