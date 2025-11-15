package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.PaymentCreateForm;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.FinanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/finance/api")
public class FinanceManagementApi {

    private final FinanceService financeService;

    @GetMapping("/vouchers/{reservationId}/{isPaid}")
    public ResponseEntity<ResponseMessage> getDailyVoucherListForReservation(@PathVariable long reservationId,
                                                                             @PathVariable boolean isPaid,
                                                                             @RequestParam(required = false, defaultValue = "0") int page,
                                                                             @RequestParam(required = false, defaultValue = "10") int size) {
        var vouchers = financeService.getDailyVouchersByReservation(reservationId, isPaid, page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Vouchers", vouchers));
    }


    @PostMapping("/vouchers/selected")
    public ResponseEntity<ResponseMessage> getSelectedVouchers(@RequestBody List<String> voucherIds) {
        var vouchers = financeService.getSelectedVouchers(voucherIds);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Vouchers", vouchers));
    }

    @PostMapping("/payment/create")
    public ResponseEntity<ResponseMessage> createPayment(@RequestBody PaymentCreateForm paymentCreateForm) {
        var status = HttpStatus.CREATED;
        var message = financeService.createPayment(paymentCreateForm);
        return ResponseEntity.status(status).body(new ResponseMessage(status.value(), "Payment", message));
    }

    @GetMapping("/payment/list")
    public ResponseEntity<ResponseMessage> getPaymentList(@RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "10") int size) {
        var paymentList = financeService.getPaymentList(page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Payment List", paymentList));
    }

}
