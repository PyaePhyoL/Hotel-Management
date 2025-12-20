package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.dto.room.RoomDto;
import org.bytesync.hotelmanagement.dto.room.RoomSelectList;
import org.bytesync.hotelmanagement.model.Room;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Long>, JpaSpecificationExecutor<Room> {

    @Query("""
    select new org.bytesync.hotelmanagement.dto.room.RoomDto(
    r.roomNo,
    r.basePrice,
    r.capacity,
    r.floor,
    r.currentStatus
    )
    from Room r
    where r.currentStatus = :status
""")
    List<RoomDto> findAllRoomDtosByStatus(RoomStatus status);

    @Query("""
    select r from Room r
    where r.currentStatus = 'AVAILABLE'
""")
    List<Room> findAllRoomForSelectList();
}
