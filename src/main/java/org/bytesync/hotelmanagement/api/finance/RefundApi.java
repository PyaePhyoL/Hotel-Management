package org.bytesync.hotelmanagement.api.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.RefundDto;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.impl.finance.RefundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/finance/api/refund")
public class RefundApi {

    private final RefundService refundService;

    @PostMapping("/{reservationId}")
    public ResponseEntity<ResponseMessage> createRefund(@PathVariable Long reservationId, @RequestBody RefundDto refundDto) {
        var status = HttpStatus.CREATED;
        var message = refundService.createRefund(reservationId, refundDto);
        return ResponseEntity.status(status).body(new ResponseMessage(status.value(), "Refund", message));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> getRefundList(@RequestParam(required = false, defaultValue = "0") int page,
                                                         @RequestParam(required = false, defaultValue = "10") int size) {
        var refundList = refundService.getRefundList(page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Refund List", refundList));
    }
}
