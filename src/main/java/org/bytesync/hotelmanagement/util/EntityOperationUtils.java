package org.bytesync.hotelmanagement.util;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class EntityOperationUtils {

    public static<T,ID> T safeCall(Optional<T> entity, String domain, ID id) {
        return entity.orElseThrow(() -> new EntityNotFoundException("There is no %s with id %s".formatted(domain, id)));
    }

    public static String timeFormat(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("HH:mm:ss").format(localDateTime);
    }
}
