package org.bytesync.hotelmanagement.dto;

import java.util.List;

public record PageResult<T>(
        List<T> contents,
        long totalItems,
        int page,
        int size
) {

    public int getTotalPages() {
        return (int) Math.ceil((double) totalItems / size);
    }
}
