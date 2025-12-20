package org.bytesync.hotelmanagement.service.impl.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.ExpenseDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.model.Expense;
import org.bytesync.hotelmanagement.repository.ExpenseRepository;
import org.bytesync.hotelmanagement.util.mapper.FinanceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public String createExpense(ExpenseDto form) {
        var expense = FinanceMapper.toExpense(form);
        var id = expenseRepository.save(expense).getId();
        return "Expense created successfully : " + id;
    }

    public PageResult<ExpenseDto> getExpenseList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Expense> all = expenseRepository.findAll(pageable);
        var dtos = all.getContent().stream().map(FinanceMapper::toExpenseDto).toList();
        return new  PageResult<>(dtos, all.getTotalElements(), page, size);
    }

    public String updateExpense(Long id, ExpenseDto form) {
        var expense = safeCall(expenseRepository.findById(id), "Expense", id);
        FinanceMapper.updateExpense(expense, form);
        expenseRepository.save(expense);
        return "Expense updated successfully : " + expense.getId();
    }

    public String deleteExpense(Long id) {
        var expense = safeCall(expenseRepository.findById(id), "Expense", id);
        expenseRepository.delete(expense);
        return "Expense deleted successfully : " + expense.getId();
    }

    public ExpenseDto getExpenseDetailsById(Long id) {
        var expense = safeCall(expenseRepository.findById(id), "Expense", id);
        return FinanceMapper.toExpenseDto(expense);
    }

}
