package org.bytesync.hotelmanagement.repository.specification;

import org.bytesync.hotelmanagement.model.GuestRecord;
import org.springframework.data.jpa.domain.Specification;

public class GuestRecordSpecification {

    public static Specification<GuestRecord> currentOrAll(Boolean current) {
        return (root, cq, cb) -> {
            if (current && cq != null) {
                cq.orderBy(cb.asc(root.get("checkInTime")));
                return cb.or(cb.equal(root.get("current"), true));
            } else {
                return null;
            }
        };
    }
}
