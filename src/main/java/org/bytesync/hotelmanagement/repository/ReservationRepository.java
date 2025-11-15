package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo;
import org.bytesync.hotelmanagement.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long>, JpaSpecificationExecutor<Reservation> {

    @Query("""
    select new org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo(
    r.id,
    r.checkInTime,
    g.id,
    g.name,
    g.phoneList,
    r.noOfGuests,
    r.daysOfStay
    )
    from Reservation r
    left join fetch r.guest g
    left join fetch g.phoneList phoneList
    where r.id = :id
""")
    Optional<ReservationGuestInfo> findReservationGuestInfoById(Long id);

    @Query("""
    select r from Reservation r
    where r.isActive = true
""")
    List<Reservation> findAllActiveReservations();
}
