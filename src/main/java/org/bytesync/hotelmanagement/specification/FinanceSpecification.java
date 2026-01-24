package org.bytesync.hotelmanagement.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.bytesync.hotelmanagement.dto.finance.ExpenseFilterDto;
import org.bytesync.hotelmanagement.dto.finance.PaymentFilterDto;
import org.bytesync.hotelmanagement.dto.finance.FinanceFilterDto;
import org.bytesync.hotelmanagement.enums.ExpenseType;
import org.bytesync.hotelmanagement.enums.IncomeType;
import org.bytesync.hotelmanagement.enums.RefundType;
import org.bytesync.hotelmanagement.model.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.getCurrentYangonZoneLocalDateTime;

public class FinanceSpecification {

    public static Specification<Payment> paymentFilterByReservation(Long reservationId) {
        return (root, cq, cb) ->
                cb.equal(root.get("reservation").get("id"), reservationId);
    }

    public static <T> Specification<T> financeFilter(FinanceFilterDto dto) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Class<T> entityClass = root.getModel().getBindableJavaType();

            if(entityClass.equals(Refund.class)) {
                if(dto.type() != null) {
                    var refundType = RefundType.valueOf(dto.type());
                    predicates.add(cb.equal(root.get("type"), refundType));
                }
            }

            if(entityClass.equals(Payment.class)) {
                if(dto.type() != null) {
                    var incomeType = IncomeType.valueOf(dto.type());
                    predicates.add(cb.equal(root.get("type"), incomeType));
                }
            }

            if(entityClass.equals(Expense.class)) {
                if (dto.type() != null) {
                    var expenseType = ExpenseType.valueOf(dto.type());
                    predicates.add(cb.equal(root.get("type"), expenseType));
                }
            }

            if(dto.from() != null && dto.to() != null) {
                predicates.add(cb.between(root.get("date"), dto.from(), dto.to()));
            } else if (dto.from() != null) {
                predicates.add(cb.equal(root.get("date"), dto.from()));
            } else if (dto.to() != null) {
                predicates.add(cb.equal(root.get("date"), dto.to()));
            } else {
                LocalDate today = getCurrentYangonZoneLocalDateTime().toLocalDate();
                predicates.add(cb.equal(root.get("date"), today));
            }

            try {
                Integer amount = Integer.parseInt(dto.query());
                predicates.add(cb.equal(root.get("amount"), amount));
            } catch (NumberFormatException e) {
                if(dto.query() != null) {
                    String likeKeyword = "%" + dto.query().toLowerCase() + "%";

                    if(entityClass.equals(Payment.class) || entityClass.equals(Refund.class)) {
                        Join<Refund, Guest> guestJoin = root.join("guest", JoinType.INNER);
                        predicates.add(cb.or(cb.like(cb.lower(guestJoin.get("name")), likeKeyword),
                                cb.like(root.get("notes"), likeKeyword)));
                    }

                    if(entityClass.equals(Expense.class)) {
                        predicates.add(cb.or(
                                cb.like(root.get("title"), likeKeyword),
                                cb.like(root.get("notes"), likeKeyword)
                        ));
                    }
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
