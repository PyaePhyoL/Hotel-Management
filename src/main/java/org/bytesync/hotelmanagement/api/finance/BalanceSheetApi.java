package org.bytesync.hotelmanagement.api.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.finance.BalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/finance/api/balance")
public class BalanceSheetApi {

    private final BalanceService balanceService;

    @GetMapping("/{year}/{month}")
    public ResponseEntity<ResponseMessage> getBalanceSheet(@PathVariable int year, @PathVariable int month) {
        var monthlyBalance = balanceService.getMonthlyBalance(year, month);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Monthly Balance Sheet", monthlyBalance));
    }
}
