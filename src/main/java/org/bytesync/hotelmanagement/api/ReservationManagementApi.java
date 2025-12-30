package org.bytesync.hotelmanagement.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.dto.reservation.ExtraHoursDto;
import org.bytesync.hotelmanagement.dto.reservation.ReservationDetails;
import org.bytesync.hotelmanagement.dto.reservation.ReservationForm;
import org.bytesync.hotelmanagement.dto.reservation.ReservationInfo;
import org.bytesync.hotelmanagement.enums.Status;
import org.bytesync.hotelmanagement.service.interfaces.hotel.IReservationService;
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

    private final IReservationService reservationService;

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
    public ResponseEntity<ResponseMessage<Void>> checkout(@PathVariable Long id,
                                                    @RequestParam LocalDateTime checkout) {
        var message = reservationService.checkoutReservation(id, checkout);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ResponseMessage<Void>> cancelReservation(@PathVariable Long id) {
        var message = reservationService.cancelReservation(id);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage<Void>> deleteReservation(@PathVariable Long id) {
        var message = reservationService.delete(id);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null
        ));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseMessage<ReservationDetails>> getDetailsById(@PathVariable Long id) {
        var detail = reservationService.getDetailsById(id);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                "",
                detail));
    }

    @PutMapping("/{reservationId}/change-room/{roomId}")
    public ResponseEntity<ResponseMessage<Void>> changeRoom(@PathVariable Long reservationId,
                                                            @PathVariable Long roomId,
                                                            @RequestParam(required = false, defaultValue = "0") Integer extraPrice) {
        var message = reservationService.changeRoom(reservationId, roomId, extraPrice);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage<Void>> updateReservation(@PathVariable Long id,
                                                             @Valid @RequestBody ReservationForm form) {
        var message = reservationService.update(id, form);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null));
    }

    @PostMapping("/extend-hours/{id}")
    public ResponseEntity<ResponseMessage<Void>> extendHoursInSection(@PathVariable Long id,
                                                                      @RequestBody ExtraHoursDto extraHoursDto) {
        var message = reservationService.extendHours(id, extraHoursDto);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null
        ));
    }

    @PostMapping("/extend-days/{id}")
    public ResponseEntity<ResponseMessage<Void>> extendDays(@PathVariable Long id,
                                                            @RequestParam Integer days) {
        var message = reservationService.extendDays(id, days);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                message,
                null
        ));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseMessage<PageResult<ReservationInfo>>> searchReservations(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "true") boolean status,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
       var list = reservationService.search(query, page, size, status);
       return ResponseEntity.ok(new ResponseMessage<>(
               HttpStatus.OK.value(),
               "",
               list
       ));
    }

}
