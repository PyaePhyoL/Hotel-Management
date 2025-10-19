package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.FinanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vouchers/api")
public class FinanceManagementApi {

    private final FinanceService financeService;

    @GetMapping("/{reservationId}")
    public ResponseEntity<ResponseMessage> getDailyVoucherListForReservation(@PathVariable long reservationId,
                                                                             @RequestParam(required = false, defaultValue = "0") int page,
                                                                             @RequestParam(required = false, defaultValue = "10") int size ) {
        var vouchers = financeService.getDailyVouchersByReservation(reservationId, page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "", vouchers));
    }

}
