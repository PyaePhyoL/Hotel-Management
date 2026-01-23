package org.bytesync.hotelmanagement.specification;

import jakarta.persistence.criteria.Predicate;
import org.bytesync.hotelmanagement.enums.ExpenseType;
import org.bytesync.hotelmanagement.enums.IncomeType;
import org.bytesync.hotelmanagement.enums.RefundType;
import org.bytesync.hotelmanagement.model.Expense;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Refund;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinanceSpecification {

    public static Specification<Payment> paymentFilterByReservation(Long reservationId) {
        return (root, cq, cb) ->
                cb.equal(root.get("reservation").get("id"), reservationId);
    }

    public static Specification<Payment> paymentFilterByDate(LocalDate from,
                                                             LocalDate to,
                                                             IncomeType incomeType) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(incomeType != null) {
                predicates.add(cb.equal(root.get("incomeType"), incomeType));
            }

            if(from != null && to != null) {
                predicates.add(cb.between(root.get("date"), from, to));
            } else if (from != null) {
                predicates.add(cb.equal(root.get("date"), from));
            } else if (to != null) {
                predicates.add(cb.equal(root.get("date"), to));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Expense> expenseFilterByDate(LocalDate from,
                                                             LocalDate to,
                                                             ExpenseType expenseType) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(expenseType != null) {
                predicates.add(cb.equal(root.get("type"), expenseType));
            }

            if(from != null && to != null) {
                predicates.add(cb.between(root.get("date"), from, to));
            } else if (from != null) {
                predicates.add(cb.equal(root.get("date"), from));
            } else if (to != null) {
                predicates.add(cb.equal(root.get("date"), to));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Refund> refundFilterByDate(LocalDate from, LocalDate to, RefundType refundType) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(refundType != null) {
                predicates.add(cb.equal(root.get("type"), refundType));
            }

            if(from != null && to != null) {
                predicates.add(cb.between(root.get("date"), from, to));
            } else if (from != null) {
                predicates.add(cb.equal(root.get("date"), from));
            } else if (to != null) {
                predicates.add(cb.equal(root.get("date"), to));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
