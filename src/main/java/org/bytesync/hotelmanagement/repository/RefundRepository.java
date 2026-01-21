package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RefundRepository extends JpaRepository<Refund,Long>, JpaSpecificationExecutor<Refund> {

    @Query("""
    select r from Refund r
    where FUNCTION('YEAR', r.date) = :year
    and FUNCTION('MONTH', r.date) = :month
""")
    List<Refund> findAllInMonthOfYear(int year, int month);
}
