package org.bytesync.hotelmanagement.repository.specification;

import org.bytesync.hotelmanagement.model.Payment;
import org.springframework.data.jpa.domain.Specification;

public class PaymentSpecification {

    public static Specification<Payment> filterByReservation(Long reservationId) {
        return (root, cq, cb) ->
                cb.equal(root.get("reservation").get("id"), reservationId);
    }
}
