package org.bytesync.hotelmanagement.service.interfaces.finance;

import org.bytesync.hotelmanagement.dto.finance.ExpenseDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;

import java.time.LocalDate;

public interface IExpenseService {

    String createExpense(ExpenseDto form);

    PageResult<ExpenseDto> getExpenseList(int page, int size, LocalDate from, LocalDate to);

    ExpenseDto getExpenseDetailsById(Long id);

    String updateExpense(Long id, ExpenseDto form);

    String deleteExpense(Long id);
}
