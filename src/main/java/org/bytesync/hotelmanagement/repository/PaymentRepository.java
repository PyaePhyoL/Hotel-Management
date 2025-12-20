package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("""
select p from Payment p
where FUNCTION('YEAR', p.paymentDate) = :year
and FUNCTION('MONTH', p.paymentDate) = :month
""")
    List<Payment> findAllInMonthOfYear(int year, int month);

    List<Payment> findByPaymentDate(LocalDate paymentDate);
}
