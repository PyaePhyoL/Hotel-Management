package org.bytesync.hotelmanagement.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.bytesync.hotelmanagement.model.Guest;
import org.springframework.data.jpa.domain.Specification;

public class GuestSpecification {

    public static Specification<Guest> keyword(String keyword) {
        return (root, cq, cb) -> {
            String likeKeyword = "%" + keyword.toLowerCase() + "%";
            Join<Guest, String> phoneJoin = root.join("phoneList", JoinType.LEFT);

            return cb.or(
                    cb.like(cb.lower(root.get("name")), likeKeyword),
                    cb.like(cb.lower(root.get("email")), likeKeyword),
                    cb.like(cb.lower(phoneJoin), likeKeyword),
                    cb.like(cb.lower(root.get("nrc")), likeKeyword),
                    cb.like(cb.lower(root.get("passport")), likeKeyword)
            );
        };
    }
}
