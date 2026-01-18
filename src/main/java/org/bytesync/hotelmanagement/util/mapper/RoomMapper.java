package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.room.RoomDto;
import org.bytesync.hotelmanagement.dto.room.RoomOverviewDetails;
import org.bytesync.hotelmanagement.dto.room.RoomSelectList;
import org.bytesync.hotelmanagement.model.Room;
import org.bytesync.hotelmanagement.enums.RoomStatus;
import org.bytesync.hotelmanagement.enums.Status;
import org.bytesync.hotelmanagement.enums.StayType;

public class RoomMapper {
    private RoomMapper() {
    }

    public static RoomDto toDto(Room room) {
        if (room == null) return null;
        return RoomDto.builder()
                .no(room.getRoomNo())
                .basePrice(room.getRoomType().getPrice())
                .capacity(room.getRoomType().getCapacity())
                .floor(room.getFloor())
                .currentStatus(room.getCurrentStatus())
                .build();
    }

    public static RoomOverviewDetails toRoomOverDetails(Room room) {
        return RoomOverviewDetails.builder()
                .no(room.getRoomNo())
                .roomType(room.getRoomType().getDescription())
                .basePrice(room.getRoomType().getPrice())
                .notes(room.getNotes())
                .capacity(room.getRoomType().getCapacity())
                .floor(room.getFloor())
                .currentStatus(room.getCurrentStatus())
                .currentReservationId(room.getCurrentReservationId())
                .build();
    }

    public static RoomSelectList toRoomSelectList(Room room) {
        var name = "%d (%s) - %d MMK".formatted(room.getRoomNo(), room.getFloor().getValue(), room.getRoomType().getPrice());
        return new RoomSelectList(
                room.getRoomNo(),
                name,
                room.getRoomType().getPrice()
        );
    }

    public static RoomStatus getRoomCurrentStatusFromReservation(Status status, StayType stayType) {
        return status != Status.BOOKING ? (
                switch (stayType) {
                    case NORMAL -> RoomStatus.NORMAL_STAY;
                    case SECTION ->  RoomStatus.SECTION_STAY;
                    case LONG ->  RoomStatus.LONG_STAY;
                }
        ) : RoomStatus.BOOKING;
    }
}
