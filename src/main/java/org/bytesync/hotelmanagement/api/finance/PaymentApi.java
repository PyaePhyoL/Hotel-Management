package org.bytesync.hotelmanagement.api.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.PaymentCreateForm;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.finance.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/finance/api/payment")
public class PaymentApi {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createPayment(@RequestBody PaymentCreateForm paymentCreateForm) {
        var status = HttpStatus.CREATED;
        var message = paymentService.createPayment(paymentCreateForm);
        return ResponseEntity.status(status).body(new ResponseMessage(status.value(), "Payment", message));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> getPaymentList(@RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "10") int size) {
        var paymentList = paymentService.getPaymentList(page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Payment List", paymentList));
    }

    @GetMapping("/section/form/{reservationId}")
    public ResponseEntity<ResponseMessage> getSectionPaymentForm(@PathVariable Long reservationId) {
        var form = paymentService.getSectionPaymentForm(reservationId);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Section Payment", form));
    }
}
