package org.bytesync.hotelmanagement.api.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.RefundDto;
import org.bytesync.hotelmanagement.dto.finance.FinanceFilterDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.interfaces.finance.IRefundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/finance/api/refund")
public class RefundApi {

    private final IRefundService refundService;

    @PostMapping("/deposit/{reservationId}")
    public ResponseEntity<ResponseMessage<Void>> createDepositRefund(@PathVariable Long reservationId,
                                                                     @RequestBody RefundDto refundDto) {
        var status = HttpStatus.CREATED;
        var message = refundService.createDepositRefund(reservationId, refundDto);
        return ResponseEntity.status(status).body(new ResponseMessage<>(
                status.value(),
                message,
                null));
    }

    @PostMapping("/payment/{paymentId}")
    public ResponseEntity<ResponseMessage<Void>> createPaymentRefund(@PathVariable Long paymentId,
                                                                     @RequestBody RefundDto refundDto) {
        var status = HttpStatus.CREATED;
        var message = refundService.createPaymentRefund(paymentId, refundDto);
        return ResponseEntity.status(status).body(new ResponseMessage<>(
                status.value(),
                message,
                null
        ));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage<PageResult<RefundDto>>> getRefundList(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                @RequestParam(required = false, defaultValue = "10") int size,
                                                                                @RequestParam LocalDate from,
                                                                                @RequestParam LocalDate to,
                                                                                @RequestParam(required = false) String query,
                                                                                @RequestParam(required = false) String type) {
        FinanceFilterDto filterDto = new FinanceFilterDto(from, to, query, type);
        var refundList = refundService.getRefundList(page, size, filterDto);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                "",
                refundList));
    }
}
