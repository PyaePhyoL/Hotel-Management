package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, String> {

    @Query("""
    select e from Expense e
    where FUNCTION('YEAR', e.date) = :year
    and FUNCTION('MONTH', e.date) = :month
""")
    List<Expense> findAllInMonthOfYear(int year, int month);
}
