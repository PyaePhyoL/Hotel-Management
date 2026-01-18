package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomTypeRepository extends JpaRepository<RoomType, String> {
}
