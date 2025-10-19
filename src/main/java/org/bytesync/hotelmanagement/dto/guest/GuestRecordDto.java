package org.bytesync.hotelmanagement.dto.guest;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record GuestRecordDto(
        UUID id,
        String guestName,
        Integer roomNo,
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        Integer daysOfStay
) {
}
