package org.bytesync.hotelmanagement.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Relation;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.enums.PaymentMethod;
import org.bytesync.hotelmanagement.model.enums.StayType;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class ReservationForm {
    @NotBlank(message = "Guest Name cannot be blank")
    private String guestName;
    @NotBlank(message = "NRC cannot be blank")
    private String guestNrc;
    @NotBlank(message = "Phone cannot be blank")
    private String phoneNumber;
    private Integer noOfGuests;
    private StayType stayType;
    private LocalDateTime checkInTime;
    @NotNull(message = "Please select a room")
    private Integer roomId;
    @NotBlank(message = "Please enter the name of register staff")
    private String staffName;
    @NotNull(message = "Price cannot be blank")
    private Integer pricePerNight;
    private Integer depositAmount;

    private List<Relation> relations;
}
