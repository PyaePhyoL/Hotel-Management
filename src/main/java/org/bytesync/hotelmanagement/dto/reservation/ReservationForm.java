package org.bytesync.hotelmanagement.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.enums.PaymentMethod;
import org.bytesync.hotelmanagement.model.enums.StayType;

import java.time.LocalDateTime;


@Data
public class ReservationForm {
    @NotBlank(message = "Guest Name cannot be blank")
    private String guestName;
    @NotBlank(message = "National ID cannot be blank")
    private String guestNrc;
    private Integer noOfGuests;
    private StayType stayType;
    private LocalDateTime checkInTime;
    private Integer roomId;
    private String staffName;
    private Double pricePerNight;
    private Double depositAmount;
}
