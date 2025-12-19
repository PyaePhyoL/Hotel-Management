package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DailyVoucherRepository extends JpaRepository<Voucher, String>, JpaSpecificationExecutor<Voucher> {
}
