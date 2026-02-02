package org.bytesync.hotelmanagement.dto.reservation;

import lombok.Builder;
import lombok.Data;
import org.bytesync.hotelmanagement.dto.guest.GuestDto;
import org.bytesync.hotelmanagement.dto.guest.ContactDto;
import org.bytesync.hotelmanagement.dto.room.RoomDto;
import org.bytesync.hotelmanagement.enums.DepositType;
import org.bytesync.hotelmanagement.enums.Status;
import org.bytesync.hotelmanagement.enums.StayType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReservationDetails {
    private Long id;
    private LocalDateTime checkInDateTime;
    private LocalDateTime checkOutDateTime;
    private Integer daysOfStay;
    private Integer price;
    private DepositType depositType;
    private Integer deposit;
    private Integer discount;
    private StayType stayType;
    private Integer noOfGuests;
    private Status status;
    private GuestDto guestDetails;
    private RoomDto roomDetails;

    private Integer totalPrice;
    private Integer leftPrice;
    private Integer refundPrice;

    private List<ContactDto> contacts;
    private String notes;

    private String registerStaff;
    private String checkInStaff;
    private String checkOutStaff;
}
