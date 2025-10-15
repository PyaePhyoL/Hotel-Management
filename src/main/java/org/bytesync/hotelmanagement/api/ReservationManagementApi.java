package org.bytesync.hotelmanagement.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.dto.reservation.ReservationForm;
import org.bytesync.hotelmanagement.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations/api")
public class ReservationManagementApi {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createReservation(@Valid @RequestBody ReservationForm form) {
        var message = reservationService.create(form);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.CREATED.value(), "Reservation created", message));
    }
}
