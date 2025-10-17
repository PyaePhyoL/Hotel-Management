package org.bytesync.hotelmanagement.dto.room;

import lombok.Builder;
import org.bytesync.hotelmanagement.model.enums.Floor;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;

@Builder
public record RoomDto(
        Integer no,
        Integer basePrice,
        Integer addOnPrice,
        Integer capacity,
        Floor floor,
        RoomStatus currentStatus
) {
}
