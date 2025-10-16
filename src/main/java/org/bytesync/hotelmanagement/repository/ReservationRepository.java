package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo;
import org.bytesync.hotelmanagement.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long>, JpaSpecificationExecutor<Reservation> {

    @Query("""
    select new org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo(
    r.id,
    r.checkInTime,
    r.guest.id,
    r.guest.name,
    r.guest.phone,
    r.noOfGuests,
    r.daysOfStay
    )
    from Reservation r
    where r.id = :id
""")
    Optional<ReservationGuestInfo> findReservationGuestInfoById(Long id);
}
