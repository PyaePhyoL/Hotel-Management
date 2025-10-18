package org.bytesync.hotelmanagement.repository.specification;

import org.bytesync.hotelmanagement.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> keyword(String keyword) {
        return (root, cq, cb) -> {
            String likeKeyword = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("name")), likeKeyword),
                    cb.like(cb.lower(root.get("email")), likeKeyword),
                    cb.like(cb.lower(root.get("nrc")), likeKeyword),
                    cb.like(cb.lower(root.get("role")), likeKeyword)
            );
        };
    }
}
