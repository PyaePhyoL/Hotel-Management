package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.RoomDto;
import org.bytesync.hotelmanagement.model.Room;

public class RoomMapper {
    private RoomMapper() {
    }

    public static RoomDto toDto(Room room) {
        return RoomDto.builder()
                .no(room.getNo())
                .basePrice(room.getBasePrice())
                .addOnPrice(room.getAddOnPrice())
                .capacity(room.getCapacity())
                .floor(room.getFloor())
                .currentStatus(room.getCurrentStatus())
                .build();
    }
}
