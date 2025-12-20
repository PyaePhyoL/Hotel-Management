package org.bytesync.hotelmanagement.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.dto.reservation.ReservationDetails;
import org.bytesync.hotelmanagement.dto.reservation.ReservationForm;
import org.bytesync.hotelmanagement.dto.reservation.ReservationInfo;
import org.bytesync.hotelmanagement.model.enums.Status;
import org.bytesync.hotelmanagement.service.hotel.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations/api")
public class ReservationManagementApi {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ResponseMessage<Void>> createReservation(@Valid @RequestBody ReservationForm form) {
        var message = reservationService.create(form);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.CREATED.value(),
                message,
                null));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage<PageResult<ReservationInfo>>> getAllReservations(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                           @RequestParam(required = false, defaultValue = "10") int size) {
        var list = reservationService.getAll(page, size, List.of());
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                "",
                list));
    }

    @GetMapping("/list/current")
    public ResponseEntity<ResponseMessage<PageResult<ReservationInfo>>> getCurrentReservations(@RequestParam(required = false, defaultValue = "0") int page,
                                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        var statusList = List.of(Status.ACTIVE, Status.BOOKING);
        var list = reservationService.getAll(page, size, statusList);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                "",
                list));
    }

    @PutMapping("/checkout/{id}")
    public ResponseEntity<ResponseMessage<Void>> checkout(@PathVariable long id,
                                                    @RequestParam LocalDateTime checkout) {
        var message = reservationService.checkoutReservation(id, checkout);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ResponseMessage<Void>> cancelReservation(@PathVariable long id) {
        var message = reservationService.cancelReservation(id);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage<Void>> deleteReservation(@PathVariable long id) {
        var message = reservationService.delete(id);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null
        ));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseMessage<ReservationDetails>> getDetailsById(@PathVariable long id) {
        var detail = reservationService.getDetailsById(id);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                "",
                detail));
    }

    @PutMapping("/{id}/change-room/{roomId}")
    public ResponseEntity<ResponseMessage<Void>> changeRoom(@PathVariable long id, @PathVariable int roomId) {
        var message = reservationService.changeRoom(id, roomId);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage<Void>> updateReservation(@PathVariable long id,
                                                             @Valid @RequestBody ReservationForm form) {
        var message = reservationService.update(id, form);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null));
    }

}
