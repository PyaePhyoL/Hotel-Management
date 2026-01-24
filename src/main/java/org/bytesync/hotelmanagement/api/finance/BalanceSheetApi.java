package org.bytesync.hotelmanagement.api.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.BalanceSummarySheet;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.interfaces.finance.IBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/finance/api/balance")
public class BalanceSheetApi {

    private final IBalanceService balanceService;

    @GetMapping()
    public ResponseEntity<ResponseMessage<BalanceSummarySheet>> getBalanceSummarySheet(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {
        var monthlyBalance = balanceService.getBalanceSummarySheet(from, to);
        return ResponseEntity.ok(new ResponseMessage<>(
                HttpStatus.OK.value(),
                "",
                monthlyBalance));
    }
}
