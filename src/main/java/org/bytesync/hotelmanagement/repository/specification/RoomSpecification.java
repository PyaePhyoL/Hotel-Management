package org.bytesync.hotelmanagement.repository.specification;

import org.bytesync.hotelmanagement.model.Room;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.springframework.data.jpa.domain.Specification;

public class RoomSpecification {

    public static Specification<Room> roomStatusEquals(RoomStatus roomStatus) {
        if(roomStatus == null) return null;
        return (root, cq, cb) -> cb.equal(root.get("currentStatus"), roomStatus);
    }
}
