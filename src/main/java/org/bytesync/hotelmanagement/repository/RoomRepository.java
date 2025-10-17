package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.dto.room.RoomDto;
import org.bytesync.hotelmanagement.dto.room.RoomSelectList;
import org.bytesync.hotelmanagement.model.Room;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Integer>, JpaSpecificationExecutor<Room> {

    @Query("""
    select new org.bytesync.hotelmanagement.dto.room.RoomDto(
    r.no,
    r.basePrice,
    r.addOnPrice,
    r.capacity,
    r.floor,
    r.currentStatus
    )
    from Room r
    where r.currentStatus = :status
""")
    List<RoomDto> findAllRoomDtosByStatus(RoomStatus status);

    @Query("""
    select new org.bytesync.hotelmanagement.dto.room.RoomSelectList(
    r.no,
    CONCAT(r.no, ' (', r.floor , ')')
    )
    from Room r
""")
    List<RoomSelectList> findAllRoomForSelectList();
}
