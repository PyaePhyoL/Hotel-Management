package org.bytesync.hotelmanagement.api.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.VoucherDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.impl.finance.VoucherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/finance/api/vouchers")
public class VouchersApi {

    private final VoucherService voucherService;

    @GetMapping("/{reservationId}/{isPaid}")
    public ResponseEntity<ResponseMessage<PageResult<VoucherDto>>> getVoucherListForReservation(@PathVariable Long reservationId,
                                                                                                @PathVariable boolean isPaid,
                                                                                                @RequestParam(required = false, defaultValue = "0") int page,
                                                                                                @RequestParam(required = false, defaultValue = "10") int size) {
        var vouchers = voucherService.getVoucherListByReservation(reservationId, isPaid, page, size);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", vouchers));
    }


    @PostMapping("/selected")
    public ResponseEntity<ResponseMessage<List<VoucherDto>>> getSelectedVouchers(@RequestBody List<Long> voucherIds) {
        var vouchers = voucherService.getSelectedVoucherDtos(voucherIds);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", vouchers));
    }

}
