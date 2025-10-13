package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
