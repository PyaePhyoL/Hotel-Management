package org.bytesync.hotelmanagement.repository.specification;

import org.bytesync.hotelmanagement.model.Staff;
import org.springframework.data.jpa.domain.Specification;

public class StaffSpecification {

    public static Specification<Staff> search(String keyword) {
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
