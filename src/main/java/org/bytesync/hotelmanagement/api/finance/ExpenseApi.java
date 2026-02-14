package org.bytesync.hotelmanagement.api.finance;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.ExpenseDto;
import org.bytesync.hotelmanagement.dto.finance.FinanceFilterDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.interfaces.finance.IExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/finance/api/expense")
public class ExpenseApi {

    private final IExpenseService expenseService;

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage<Void>> createExpense(@RequestBody @Valid ExpenseDto form) {
        var status = HttpStatus.CREATED;
        var message = expenseService.createExpense(form);
        return ResponseEntity.status(status).body(new ResponseMessage<>(status.value(), message, null));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage<PageResult<ExpenseDto>>> getExpenseList(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                  @RequestParam(required = false, defaultValue = "10") int size,
                                                                                  @RequestParam(required = false) LocalDate from,
                                                                                  @RequestParam(required = false) LocalDate to,
                                                                                  @RequestParam(required = false) String query,
                                                                                  @RequestParam(required = false) String type) {
        FinanceFilterDto filterDto = new FinanceFilterDto(from, to, query, type);
        var expenseList = expenseService.getExpenseList(page, size, filterDto);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", expenseList));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseMessage<ExpenseDto>> getExpenseDetails(@PathVariable Long id) {
        var expense = expenseService.getExpenseDetailsById(id);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", expense));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage<Void>> updateExpense(@PathVariable Long id, @RequestBody ExpenseDto form) {
        var message = expenseService.updateExpense(id, form);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage<Void>> deleteExpense(@PathVariable Long id) {
        var message = expenseService.deleteExpense(id);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }
}
