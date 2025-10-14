package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.dto.RoomDto;
import org.bytesync.hotelmanagement.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Integer> {
}
