package org.bytesync.hotelmanagement.dto;

import lombok.Data;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Reservation;

import java.time.LocalDateTime;


@Data
public class ReservationForm {
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String guestNationalId;
    private int noOfGuests;
    private Reservation.StayType stayType;
    private LocalDateTime checkInDateTime;
    private int roomId;
    private Payment.PaymentMethod paymentMethod;
    private String staffName;
}
