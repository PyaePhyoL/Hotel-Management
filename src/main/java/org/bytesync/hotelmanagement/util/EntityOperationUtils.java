package org.bytesync.hotelmanagement.util;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

public class EntityOperationUtils {

    public static<T,ID> T safeCall(Optional<T> entity, String domain, ID id) {
        return entity.orElseThrow(() -> new EntityNotFoundException("There is no %s with id %s".formatted(domain, id)));
    }
}
