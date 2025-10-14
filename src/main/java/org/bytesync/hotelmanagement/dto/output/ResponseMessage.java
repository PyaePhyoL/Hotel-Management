package org.bytesync.hotelmanagement.dto.output;

public record ResponseMessage(
        Integer code,
        String message,
        Object response
) {
}
