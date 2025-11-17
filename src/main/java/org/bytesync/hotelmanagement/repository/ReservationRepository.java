package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long>, JpaSpecificationExecutor<Reservation> {

    @Query("""
    select r from Reservation r
    where r.status = 'ACTIVE'
    and r.stayType != 'SECTION'
""")
    List<Reservation> findAllActiveReservations();

    @Query("""
    select r from Reservation r
    where r.status = 'BOOKING'
""")
    List<Reservation> findAllBookingReservations();
}
