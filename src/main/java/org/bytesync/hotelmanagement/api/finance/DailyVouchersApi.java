package org.bytesync.hotelmanagement.api.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.finance.DailyVoucherService;
import org.bytesync.hotelmanagement.service.finance.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/finance/api/vouchers")
public class DailyVouchersApi {

    private final DailyVoucherService dailyVoucherService;

    @GetMapping("/{reservationId}/{isPaid}")
    public ResponseEntity<ResponseMessage> getDailyVoucherListForReservation(@PathVariable long reservationId,
                                                                             @PathVariable boolean isPaid,
                                                                             @RequestParam(required = false, defaultValue = "0") int page,
                                                                             @RequestParam(required = false, defaultValue = "10") int size) {
        var vouchers = dailyVoucherService.getDailyVouchersByReservation(reservationId, isPaid, page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Vouchers", vouchers));
    }


    @PostMapping("/selected")
    public ResponseEntity<ResponseMessage> getSelectedVouchers(@RequestBody List<String> voucherIds) {
        var vouchers = dailyVoucherService.getSelectedVouchers(voucherIds);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Vouchers", vouchers));
    }

}
