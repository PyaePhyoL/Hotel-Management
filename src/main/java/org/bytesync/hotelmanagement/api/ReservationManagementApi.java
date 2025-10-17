package org.bytesync.hotelmanagement.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.dto.reservation.ReservationForm;
import org.bytesync.hotelmanagement.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> getReservations(@RequestParam(required = false, defaultValue = "false") boolean active,
                                                           @RequestParam(required = false, defaultValue = "0") int page,
                                                           @RequestParam(required = false, defaultValue = "10") int size) {
        var message = reservationService.getAll(active, page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Reservations List", message));
    }

    @PutMapping("/checkout/{id}")
    public ResponseEntity<ResponseMessage> checkout(@PathVariable long id,
                                                    @RequestParam LocalDateTime checkout) {
        var message = reservationService.checkoutReservation(id, checkout);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Reservation checkout", message));
    }
}
