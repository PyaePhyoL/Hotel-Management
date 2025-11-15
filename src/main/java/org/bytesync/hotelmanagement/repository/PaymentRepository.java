package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
