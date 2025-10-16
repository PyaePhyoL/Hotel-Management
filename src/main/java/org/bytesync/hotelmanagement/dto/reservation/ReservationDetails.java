package org.bytesync.hotelmanagement.dto.reservation;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.guest.GuestDto;
import org.bytesync.hotelmanagement.dto.room.RoomDto;
import org.bytesync.hotelmanagement.model.enums.StayType;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ReservationDetails {
    private Long id;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Integer daysOfStay;
    private Double pricePerNight;
    private Double depositAmount;
    private StayType stayType;
    private String registeredStaff;
    private Integer noOfGuests;

    private GuestDto guestDetails;
    private RoomDto roomDetails;
}
