package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.GuestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GuestRecordRepository extends JpaRepository<GuestRecord, Long>, JpaSpecificationExecutor<GuestRecord> {

    @Query("""
    select r from GuestRecord r
    where r.reservation.id =:reservationId
""")
    Optional<GuestRecord> findByReservationId(Long reservationId);
}
