package org.bytesync.hotelmanagement.repository.specification;

import org.bytesync.hotelmanagement.model.DailyVoucher;
import org.springframework.data.jpa.domain.Specification;

public class DailyVoucherSpecification {

    public static Specification<DailyVoucher> byReservationId(Long reservationId) {
        return (root, cq, cb) -> {
            return cb.equal(root.get("reservation").get("id"), reservationId);
        };
    }
}
