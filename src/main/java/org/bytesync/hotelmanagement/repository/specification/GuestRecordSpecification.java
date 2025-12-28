package org.bytesync.hotelmanagement.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.bytesync.hotelmanagement.model.Guest;
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

    public static Specification<GuestRecord> search(String query) {
        return (root, cq, cb) -> {
            String likeKeyword = "%" + query.toLowerCase() + "%";

            Join<GuestRecord, Guest> guestJoin = root.join("guest", JoinType.INNER);

            Join<Guest, String> phoneJoin = guestJoin.joinSet("phoneList", JoinType.LEFT);

            return cb.or(
                    cb.like(cb.lower(guestJoin.get("name")), likeKeyword),
                    cb.like(cb.lower(guestJoin.get("email")), likeKeyword),
                    cb.like(cb.lower(guestJoin.get("nrc")), likeKeyword),
                    cb.like(cb.lower(guestJoin.get("passport")), likeKeyword),
                    cb.like(cb.lower(guestJoin.get("occupation")), likeKeyword),
                    cb.like(cb.lower(guestJoin.get("address")), likeKeyword),
                    cb.like(cb.lower(phoneJoin), likeKeyword)
            );
        };
    }
}
