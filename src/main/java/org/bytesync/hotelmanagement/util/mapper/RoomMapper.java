package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.room.RoomDto;
import org.bytesync.hotelmanagement.dto.room.RoomOverviewDetails;
import org.bytesync.hotelmanagement.dto.room.RoomSelectList;
import org.bytesync.hotelmanagement.model.Room;

public class RoomMapper {
    private RoomMapper() {
    }

    public static RoomDto toDto(Room room) {
        if (room == null) return null;
        return RoomDto.builder()
                .no(room.getRoomNo())
                .basePrice(room.getBasePrice())
                .capacity(room.getCapacity())
                .floor(room.getFloor())
                .currentStatus(room.getCurrentStatus())
                .build();
    }

    public static RoomOverviewDetails toRoomOverDetails(Room room) {
        return RoomOverviewDetails.builder()
                .no(room.getRoomNo())
                .roomType(room.getRoomType())
                .basePrice(room.getBasePrice())
                .notes(room.getNotes())
                .capacity(room.getCapacity())
                .floor(room.getFloor())
                .currentStatus(room.getCurrentStatus())
                .currentReservationId(room.getCurrentReservationId())
                .build();
    }

    public static RoomSelectList toRoomSelectList(Room room) {
        var name = "%d (%s) - %d MMK".formatted(room.getRoomNo(), room.getFloor().getValue(), room.getBasePrice());
        return new RoomSelectList(
                room.getRoomNo(),
                name
        );
    }
}
