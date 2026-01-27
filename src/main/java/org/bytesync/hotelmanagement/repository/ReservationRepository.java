package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long>, JpaSpecificationExecutor<Reservation> {

    @Query("""
    select r from Reservation r
    where r.status = 'ACTIVE'
    and r.stayType = 'LONG'
""")
    List<Reservation> findAllActiveLongReservations();

    @Query("""
    select r from Reservation r
    where r.status = 'BOOKING'
""")
    List<Reservation> findAllBookingReservations();

    @Query("""
    select r from Reservation r
    where FUNCTION('DATE', r.checkInDateTime) = :today
""")
    List<Reservation> findByCheckInDate(LocalDate today);

    @Query("""
    select COUNT(r) from Reservation r
    where r.status = 'ACTIVE'
""")
    Integer countAllActive();

}
