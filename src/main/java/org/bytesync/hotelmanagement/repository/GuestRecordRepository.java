package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.GuestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface GuestRecordRepository extends JpaRepository<GuestRecord, Long>, JpaSpecificationExecutor<GuestRecord> {


    @Query("""
    select r from GuestRecord r
    where r.guest.id = :guestId
    and r.room.roomNo = :roomNo
""")
    Optional<GuestRecord> findByGuestIdAndRoomNo(Long guestId, Long roomNo);
}
