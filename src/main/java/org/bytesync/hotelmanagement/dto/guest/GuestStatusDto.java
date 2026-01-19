package org.bytesync.hotelmanagement.dto.guest;

import org.bytesync.hotelmanagement.enums.GuestStatus;

public record GuestStatusDto(
        GuestStatus status
) {
}
