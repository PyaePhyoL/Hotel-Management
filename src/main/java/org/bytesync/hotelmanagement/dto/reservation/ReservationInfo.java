package org.bytesync.hotelmanagement.dto.reservation;

import lombok.Builder;
import lombok.Data;
import org.bytesync.hotelmanagement.enums.Status;
import org.bytesync.hotelmanagement.enums.StayType;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationInfo {
    private Long id;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private StayType stayType;
    private Status status;
    private Integer daysOfStay;
    private String guestName;
    private String guestPhone;
    private Long roomNo;
}
