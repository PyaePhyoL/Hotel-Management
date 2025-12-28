package org.bytesync.hotelmanagement.api.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.VoucherCreatForm;
import org.bytesync.hotelmanagement.dto.finance.VoucherDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.interfaces.finance.IVoucherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/finance/api/vouchers")
public class VouchersApi {

    private final IVoucherService voucherService;

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

    @PostMapping
    public ResponseEntity<ResponseMessage<Void>> createAdditionalVoucher(@RequestBody VoucherCreatForm form) {
        voucherService.createAdditionalVoucher(form);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.CREATED.value(),
                "New Voucher created successfully",
                null));
    }

}
