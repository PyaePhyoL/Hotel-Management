package org.bytesync.hotelmanagement.dto.reservation;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReservationGuestInfo {

    private Long reservationId;
    private LocalDateTime checkInTime;
    private Integer guestId;
    private String guestName;
    private String guestPhone;
    private Integer noOfGuests;
    private Integer daysOfStay;
}
