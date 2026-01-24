package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.enums.IncomeType;
import org.bytesync.hotelmanagement.enums.PaymentMethod;
import org.bytesync.hotelmanagement.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    @Query("""
    select p from Payment p
    where p.type = 'ROOM_RENT'
    and FUNCTION('YEAR', p.date) = :year
    and FUNCTION('MONTH', p.date) = :month
""")
    List<Payment> findAllRoomRentPaymentsInMonthOfYear(int year, int month);

    List<Payment> findByDate(LocalDate date);

    @Query("""
    select p from Payment p
    where p.type = :type
    and p.date = :today
    and p.paymentMethod = :paymentMethod
""")
    List<Payment> findByDateAndPaymentMethodAndIncomeType(LocalDate today, PaymentMethod paymentMethod, IncomeType type);
}
