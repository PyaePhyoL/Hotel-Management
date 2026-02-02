package org.bytesync.hotelmanagement.dto.guest;

import org.bytesync.hotelmanagement.enums.GuestStatus;

public record GuestStatusDto(
        String name,
        String nrc,
        String phone,
        GuestStatus status
) {
}
