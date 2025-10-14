package org.bytesync.hotelmanagement.dto;

import lombok.Builder;
import org.bytesync.hotelmanagement.model.enums.Floor;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;

@Builder
public record RoomDto(
        Integer no,
        Double basePrice,
        Double addOnPrice,
        Integer capacity,
        Floor floor,
        RoomStatus currentStatus
) {
}
