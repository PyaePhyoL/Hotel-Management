package org.bytesync.hotelmanagement.dto.reservation;

import lombok.Data;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.enums.PaymentMethod;
import org.bytesync.hotelmanagement.model.enums.StayType;

import java.time.LocalDateTime;


@Data
public class ReservationForm {
    private Integer guestId;
    private Integer noOfGuests;
    private StayType stayType;
    private LocalDateTime checkInDateTime;
    private Integer roomId;
    private PaymentMethod paymentMethod;
    private String staffName;
}
