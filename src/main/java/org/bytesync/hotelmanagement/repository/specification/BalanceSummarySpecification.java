package org.bytesync.hotelmanagement.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BalanceSummarySpecification {

    public static <T> Specification<T> filterByDate(LocalDate from, LocalDate to) {
        return (root, cq, cb) -> {
            if(from != null && to != null) {
                return cb.between(root.get("date"), from, to);
            } else if (from != null) {
                return cb.equal(root.get("date"), from);
            } else if (to != null) {
                return cb.equal(root.get("date"), to);
            }
            return null;
        };
    }

}
