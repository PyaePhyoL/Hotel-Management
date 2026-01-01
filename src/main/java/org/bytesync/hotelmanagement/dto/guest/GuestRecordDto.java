package org.bytesync.hotelmanagement.dto.guest;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record GuestRecordDto(
        Long id,
        String photoUrl,
        String guestName,
        Long roomNo,
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        Integer daysOfStay
) {
}
