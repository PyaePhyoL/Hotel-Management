package org.bytesync.hotelmanagement.repository.specification;

import org.bytesync.hotelmanagement.model.Reservation;
import org.springframework.data.jpa.domain.Specification;

public class ReservationSpecification {

    public static Specification<Reservation> filterByStatus(boolean status) {
        return (root, cq, cb) -> {
            if(status) {
                return cb.equal(root.get("isActive"), status);
            } else {
                return null;
            }
        };
    }
}
