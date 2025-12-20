package org.bytesync.hotelmanagement.repository.specification;

import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ReservationSpecification {

    public static Specification<Reservation> filterByStatus(List<Status> statusList) {
        return (root, cq, cb) -> {
            if(statusList != null && !statusList.isEmpty()) {
                return root.get("status").in(statusList);
            } else {
                return null;
            }
        };
    }
}
