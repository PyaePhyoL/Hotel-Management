package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund,Long> {
}
