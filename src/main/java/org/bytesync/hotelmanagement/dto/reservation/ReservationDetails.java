package org.bytesync.hotelmanagement.dto.reservation;

import lombok.Builder;
import lombok.Data;
import org.bytesync.hotelmanagement.dto.guest.GuestDto;
import org.bytesync.hotelmanagement.dto.guest.ContactDto;
import org.bytesync.hotelmanagement.dto.room.RoomDto;
import org.bytesync.hotelmanagement.model.enums.Status;
import org.bytesync.hotelmanagement.model.enums.StayType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReservationDetails {
    private Long id;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Integer daysOfStay;
    private Integer pricePerNight;
    private Integer depositAmount;
    private Integer discountAmount;
    private StayType stayType;
    private String registeredStaff;
    private Integer noOfGuests;
    private Status status;
    private GuestDto guestDetails;
    private RoomDto roomDetails;
    private String staffName;

    private Integer totalPrice;
    private Integer paidPrice;
    private Integer leftPrice;

    private List<ContactDto> contacts;
    private String notes;
}
