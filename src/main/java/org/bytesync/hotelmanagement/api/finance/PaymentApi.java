package org.bytesync.hotelmanagement.api.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.*;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.interfaces.finance.IPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    public ResponseEntity<ResponseMessage<PageResult<PaymentListDto>>> getPaymentList(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                      @RequestParam(required = false, defaultValue = "10") int size,
                                                                                      @RequestParam(required = false) LocalDate from,
                                                                                      @RequestParam(required = false) LocalDate to,
                                                                                      @RequestParam(required = false) String query,
                                                                                      @RequestParam(required = false) String type) {
        FinanceFilterDto filterDto = new FinanceFilterDto(from, to, query, type);
        var paymentList = paymentService.getPaymentList(page, size, filterDto);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", paymentList));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage<Void>> updateExpenditureAmount(@PathVariable Long id,
                                                                         @RequestBody UpdateExpediaAmountDto amountDto) {
        var message = paymentService.updateExpediaAmount(id, amountDto);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<ResponseMessage<PageResult<PaymentListDto>>> getPaymentListByReservation(@PathVariable Long id,
                                                                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                                                                   @RequestParam(required = false, defaultValue = "10") int size) {
        var paymentList = paymentService.getPaymentListByReservation(id, page, size);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", paymentList));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ResponseMessage<PaymentDetailsDto>> getPaymentDetailsById(@PathVariable Long id) {
        var paymentDetails = paymentService.getPaymentDetailsById(id);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", paymentDetails));
    }

}
