package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.room.RoomDto;
import org.bytesync.hotelmanagement.dto.room.RoomOverviewDetails;
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

    public static RoomOverviewDetails toRoomOverDetails(Room room) {
        return RoomOverviewDetails.builder()
                .no(room.getNo())
                .roomType(room.getRoomType())
                .basePrice(room.getBasePrice())
                .addOnPrice(room.getAddOnPrice())
                .capacity(room.getCapacity())
                .floor(room.getFloor())
                .currentStatus(room.getCurrentStatus())
                .currentReservationId(room.getCurrentReservationId())
                .build();
    }
}
