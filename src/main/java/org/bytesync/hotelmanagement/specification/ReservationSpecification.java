package org.bytesync.hotelmanagement.specification;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.bytesync.hotelmanagement.model.Guest;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.enums.Status;
import org.bytesync.hotelmanagement.model.Room;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.bytesync.hotelmanagement.enums.Status.ACTIVE;
import static org.bytesync.hotelmanagement.enums.Status.BOOKING;

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

    public static Specification<Reservation> search(String keyword, boolean status) {
        return(root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(status) {
                predicates.add(root.get("status").in("BOOKING", "ACTIVE"));
            } else {
                predicates.add(root.get("status").in("FINISHED", "CANCELED"));
            }

            try {
                var roomNo = Long.parseLong(keyword);
                Join<Reservation, Room> roomJoin = root.join("room", JoinType.INNER);
                predicates.add(cb.like(roomJoin.get("roomNo").as(String.class), "%" + roomNo + "%"));
            } catch (NumberFormatException e) {
                String likeKeyword = "%" + keyword.toLowerCase() + "%";
                Join<Reservation, Guest> guestJoin = root.join("guest", JoinType.INNER);

                predicates.add(cb.like(cb.lower(guestJoin.get("name")), likeKeyword));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Reservation> nightShiftFilter() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Expression<Integer> hourExp = cb.function("HOUR", Integer.class, root.get("checkInDateTime"));

            predicates.add(cb.or(
                    cb.greaterThanOrEqualTo(hourExp, 18),
                    cb.lessThan(hourExp, 6)
            ));

            predicates.add(cb.equal(root.get("status"), ACTIVE));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }

    public static Specification<Reservation> morningShiftFilter() {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Expression<Integer> hourExp = cb.function("HOUR", Integer.class, root.get("checkInDateTime"));

            predicates.add(cb.and(
                    cb.greaterThanOrEqualTo(hourExp, 6),
                    cb.lessThan(hourExp, 18)
            ));
            predicates.add(cb.equal(root.get("status"), ACTIVE));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }
}
