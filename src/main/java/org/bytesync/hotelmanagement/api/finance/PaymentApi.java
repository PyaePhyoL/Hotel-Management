package org.bytesync.hotelmanagement.api.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.PaymentCreateForm;
import org.bytesync.hotelmanagement.dto.finance.PaymentDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.impl.finance.PaymentService;
import org.bytesync.hotelmanagement.service.interfaces.finance.IPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/finance/api/payment")
public class PaymentApi {

    private final IPaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage<Void>> createPayment(@RequestBody PaymentCreateForm paymentCreateForm) {
        var status = HttpStatus.CREATED;
        var message = paymentService.createPayment(paymentCreateForm);
        return ResponseEntity.status(status).body(new ResponseMessage<>(status.value(), message, null));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage<PageResult<PaymentDto>>> getPaymentList(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        var paymentList = paymentService.getPaymentList(page, size);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", paymentList));
    }

}
