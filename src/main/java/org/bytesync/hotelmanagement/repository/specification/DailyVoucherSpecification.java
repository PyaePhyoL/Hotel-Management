package org.bytesync.hotelmanagement.repository.specification;

import org.bytesync.hotelmanagement.model.DailyVoucher;
import org.springframework.data.jpa.domain.Specification;

public class DailyVoucherSpecification {

    public static Specification<DailyVoucher> byReservationId(Long reservationId, boolean isPaid) {
        return (root, cq, cb) -> {
            return cb.and(
                    cb.equal(root.get("reservation").get("id"), reservationId),
                    cb.equal(root.get("isPaid"), isPaid)
            );
        };
    }
}
