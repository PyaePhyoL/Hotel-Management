package org.bytesync.hotelmanagement.dto.room;

public record RoomSelectList(
        Long no,
        String name,
        String roomType,
        Integer price
) {
}
