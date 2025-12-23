package org.bytesync.hotelmanagement.repository.specification;

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

    public static Specification<Reservation> search(String keyword) {
        return(root, cq, cb) -> {

            try {
                var roomNo = Long.parseLong(keyword);
                Join<Reservation, Room> roomJoin = root.join("room", JoinType.INNER);
                return cb.like(roomJoin.get("roomNo").as(String.class), "%" + roomNo + "%");
            } catch (NumberFormatException e) {
                String likeKeyword = "%" + keyword.toLowerCase() + "%";
                Join<Reservation, Guest> guestJoin = root.join("guest", JoinType.INNER);

                return cb.like(cb.lower(guestJoin.get("name")), likeKeyword);

            }
        };
    }
}
