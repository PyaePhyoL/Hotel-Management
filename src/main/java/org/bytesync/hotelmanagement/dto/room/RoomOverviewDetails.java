package org.bytesync.hotelmanagement.dto.room;

import lombok.Builder;
import lombok.Data;
import org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo;
import org.bytesync.hotelmanagement.model.enums.Floor;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.bytesync.hotelmanagement.model.enums.RoomType;

@Data
@Builder
public class RoomOverviewDetails {
    private Integer no;
    private RoomType roomType;
    private Integer basePrice;
    private Integer capacity;
    private Floor floor;
    private RoomStatus currentStatus;
    private Long currentReservationId;
    private String notes;

    private ReservationGuestInfo guestInfo;
}
