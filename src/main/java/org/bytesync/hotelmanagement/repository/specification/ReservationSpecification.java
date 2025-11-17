package org.bytesync.hotelmanagement.repository.specification;

import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.enums.Status;
import org.springframework.data.jpa.domain.Specification;

public class ReservationSpecification {

    public static Specification<Reservation> filterByStatus(Status status) {
        return (root, cq, cb) -> {
            if(status != null) {
                return cb.equal(root.get("status"), status);
            } else {
                return null;
            }
        };
    }
}
