package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RefundRepository extends JpaRepository<Refund,Long> {

    @Query("""
    select r from Refund r
    where FUNCTION('YEAR', r.refundDate) = :year
    and FUNCTION('MONTH', r.refundDate) = :month
""")
    List<Refund> findAllInMonthOfYear(int year, int month);
}
