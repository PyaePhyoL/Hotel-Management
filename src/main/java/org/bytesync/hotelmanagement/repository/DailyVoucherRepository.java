package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.DailyVoucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DailyVoucherRepository extends JpaRepository<DailyVoucher, UUID> {
}
