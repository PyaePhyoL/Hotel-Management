package org.bytesync.hotelmanagement.dto.output;

public record ResponseMessage<T>(
        Integer code,
        String message,
        T response
) {
}
