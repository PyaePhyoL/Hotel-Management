package org.bytesync.hotelmanagement.dto.reservation;

import lombok.Builder;
import lombok.Data;
import org.bytesync.hotelmanagement.dto.guest.GuestDto;
import org.bytesync.hotelmanagement.dto.room.RoomDto;
import org.bytesync.hotelmanagement.model.enums.Status;
import org.bytesync.hotelmanagement.model.enums.StayType;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationDetails {
    private Long id;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Integer daysOfStay;
    private Integer pricePerNight;
    private Integer depositAmount;
    private StayType stayType;
    private String registeredStaff;
    private Integer noOfGuests;
    private Status status;
    private GuestDto guestDetails;
    private RoomDto roomDetails;

    private Integer totalPrice;
    private Integer paidPrice;
    private Integer leftPrice;
}
