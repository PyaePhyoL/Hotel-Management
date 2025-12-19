package org.bytesync.hotelmanagement.api.finance;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.ExpenseDto;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.service.finance.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/finance/api/expense")
public class ExpenseApi {

    private final ExpenseService expenseService;

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createExpense(@RequestBody @Valid ExpenseDto form) {
        var status = HttpStatus.CREATED;
        var message = expenseService.createExpense(form);
        return ResponseEntity.status(status).body(new ResponseMessage(status.value(), "Expense Create", message));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> getExpenseList(@RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "10") int size) {
        var expenseList = expenseService.getExpenseList(page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Expense List", expenseList));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseMessage> getExpenseDetails(@PathVariable String id) {
        var expense = expenseService.getExpenseDetailsById(id);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Expense Details", expense));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updateExpense(@PathVariable String id, @RequestBody ExpenseDto form) {
        var message = expenseService.updateExpense(id, form);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Expense Update", message));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteExpense(@PathVariable String id) {
        var message = expenseService.deleteExpense(id);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Expense Delete", message));
    }
}
