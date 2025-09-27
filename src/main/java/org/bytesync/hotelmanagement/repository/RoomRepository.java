package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Integer> {
}
