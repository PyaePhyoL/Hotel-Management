package org.bytesync.hotelmanagement.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bytesync.hotelmanagement.dto.guest.ContactDto;
import org.bytesync.hotelmanagement.enums.StayType;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class ReservationForm {
    @NotBlank(message = "Guest Name cannot be blank")
    private String guestName;
    @NotBlank(message = "NRC cannot be blank")
    private String guestNrc;
    @NotBlank(message = "Phone cannot be blank")
    private String phone;
    private Integer noOfGuests;
    private StayType stayType;
    private LocalDateTime checkInDateTime;
    private LocalDateTime checkOutDateTime;
    @NotNull(message = "Please select a room")
    private Long roomId;
    @NotNull(message = "Price cannot be blank")
    private Integer price;
    private Integer extraPrice;
    private Integer discount;
    private Integer deposit;
    private String note;
    private List<ContactDto> contacts;
}
