package org.bytesync.hotelmanagement.dto.room;

import lombok.Builder;
import org.bytesync.hotelmanagement.enums.Floor;
import org.bytesync.hotelmanagement.enums.RoomStatus;
import org.bytesync.hotelmanagement.enums.RoomType;

@Builder
public record RoomDto(
        Long no,
        Integer basePrice,
        Integer capacity,
        Floor floor,
        RoomStatus currentStatus,
        String roomType
) {
}
