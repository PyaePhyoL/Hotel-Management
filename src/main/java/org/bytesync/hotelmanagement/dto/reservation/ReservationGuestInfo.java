package org.bytesync.hotelmanagement.dto.reservation;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class ReservationGuestInfo {

    private Long reservationId;
    private LocalDateTime checkInTime;
    private Integer guestId;
    private String guestName;
    private Set<String> guestPhoneList;
    private Integer noOfGuests;
    private Integer daysOfStay;
}
