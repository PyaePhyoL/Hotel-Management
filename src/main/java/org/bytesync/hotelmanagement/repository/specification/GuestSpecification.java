package org.bytesync.hotelmanagement.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.bytesync.hotelmanagement.model.Guest;
import org.springframework.data.jpa.domain.Specification;

public class GuestSpecification {

    public static Specification<Guest> search(String query) {
        return (root, cq, cb) -> {
            String likeKeyword = "%" + query.toLowerCase() + "%";
            Join<Guest, String> phoneJoin = root.join("phoneList", JoinType.LEFT);

            return cb.or(
                    cb.like(cb.lower(root.get("name")), likeKeyword),
                    cb.like(cb.lower(root.get("email")), likeKeyword),
                    cb.like(cb.lower(root.get("nrc")), likeKeyword),
                    cb.like(cb.lower(root.get("passport")), likeKeyword),
                    cb.like(cb.lower(root.get("occupation")), likeKeyword),
                    cb.like(cb.lower(root.get("address")), likeKeyword),
                    cb.like(cb.lower(phoneJoin), likeKeyword)
                    );
        };
    }
}