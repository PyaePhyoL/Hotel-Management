package org.bytesync.hotelmanagement.dto;

public record ResponseMessage(
        Integer code,
        String message,
        Object response
) {
}
