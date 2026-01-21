package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    @Query("""
    select p from Payment p
    where p.incomeType = 'ROOM_RENT_PAYMENT'
    and FUNCTION('YEAR', p.paymentDate) = :year
    and FUNCTION('MONTH', p.paymentDate) = :month
""")
    List<Payment> findAllRoomRentPaymentsInMonthOfYear(int year, int month);

    List<Payment> findByPaymentDate(LocalDate paymentDate);
}
