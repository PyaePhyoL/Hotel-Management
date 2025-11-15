package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.DailyVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface DailyVoucherRepository extends JpaRepository<DailyVoucher, String>, JpaSpecificationExecutor<DailyVoucher> {
}
