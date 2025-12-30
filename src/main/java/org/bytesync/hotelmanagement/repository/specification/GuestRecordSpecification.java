package org.bytesync.hotelmanagement.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.bytesync.hotelmanagement.model.Guest;
import org.bytesync.hotelmanagement.model.GuestRecord;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GuestRecordSpecification {

    public static Specification<GuestRecord> currentOrAll(Boolean current) {
        return (root, cq, cb) -> {
            if (cq != null) {
                cq.orderBy(cb.asc(root.get("checkInTime")));
                return cb.or(cb.equal(root.get("current"), current));
            } else {
                return null;
            }
        };
    }

    public static Specification<GuestRecord> search(String query, boolean isCurrent) {
        return (root, cq, cb) -> {
            String likeKeyword = "%" + query.toLowerCase() + "%";

            List<Predicate> predicates = new ArrayList<>();

            Join<GuestRecord, Guest> guestJoin = root.join("guest", JoinType.INNER);

            Join<Guest, String> phoneJoin = guestJoin.joinSet("phoneList", JoinType.LEFT);

            predicates.add(cb.equal(root.get("current"), isCurrent));

            predicates.add(cb.or(
                    cb.like(cb.lower(guestJoin.get("name")), likeKeyword),
                    cb.like(cb.lower(guestJoin.get("email")), likeKeyword),
                    cb.like(cb.lower(guestJoin.get("nrc")), likeKeyword),
                    cb.like(cb.lower(guestJoin.get("passport")), likeKeyword),
                    cb.like(cb.lower(guestJoin.get("occupation")), likeKeyword),
                    cb.like(cb.lower(guestJoin.get("address")), likeKeyword),
                    cb.like(cb.lower(phoneJoin), likeKeyword)
            ));

            return cb.and(predicates.toArray(new Predicate[0]));


        };
    }
}
