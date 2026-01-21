package org.bytesync.hotelmanagement.service.impl.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.*;
import org.bytesync.hotelmanagement.model.Expense;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Refund;
import org.bytesync.hotelmanagement.repository.ExpenseRepository;
import org.bytesync.hotelmanagement.repository.PaymentRepository;
import org.bytesync.hotelmanagement.repository.RefundRepository;
import org.bytesync.hotelmanagement.repository.specification.BalanceSummarySpecification;
import org.bytesync.hotelmanagement.service.interfaces.finance.IBalanceService;
import org.bytesync.hotelmanagement.util.EntityOperationUtils;
import org.bytesync.hotelmanagement.util.mapper.FinanceMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.dateFormat;

@Service
@RequiredArgsConstructor
public class BalanceService implements IBalanceService {

    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;
    private final RefundRepository refundRepository;

    @Override
    public BalanceSummarySheet getBalanceSummarySheet(LocalDate from, LocalDate to) {
        BalanceSummarySheet balanceSummarySheet = new BalanceSummarySheet();
        balanceSummarySheet.setPeriod(getPeriodString(from, to));
        populateMoneyInMonthlyBalance(balanceSummarySheet, from, to);
        return balanceSummarySheet;
    }

    private void populateMoneyInMonthlyBalance(BalanceSummarySheet balanceSummarySheet, LocalDate from, LocalDate to) {
        var paymentDtos = getAllPaymentsByMonthOfYear(from, to);
        var expenseDtos = getAllExpensesByMonthOfYear(from, to);
        var refundDtos = getAllRefundsByMonthOfYear(from, to);

        var totalIncomes = paymentDtos.stream().map(PaymentDto::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var totalExpenses = expenseDtos.stream().map(ExpenseDto::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var totalRefunds = refundDtos.stream().map(RefundDto::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var profit =  totalIncomes - totalExpenses;

        balanceSummarySheet.setIncomes(paymentDtos);
        balanceSummarySheet.setExpenses(expenseDtos);
        balanceSummarySheet.setTotalIncome(totalIncomes);
        balanceSummarySheet.setTotalExpense(totalExpenses);
        balanceSummarySheet.setTotalRefund(totalRefunds);
        balanceSummarySheet.setProfit(profit);
    }



    private String getPeriodString(LocalDate from, LocalDate to) {
        if(from != null && to != null) {
            return "%s ~ %s".formatted(dateFormat(from), dateFormat(to));
        } else if (from != null) {
            return dateFormat(from);
        } else if (to != null) {
            return dateFormat(to);
        }
        return  null;
    }

    private List<PaymentDto> getAllPaymentsByMonthOfYear(LocalDate from, LocalDate to) {
        Specification<Payment> spec = BalanceSummarySpecification.filterByDate(from, to);
        var payments = paymentRepository.findAll(spec);
        return payments.stream().map(FinanceMapper::toPaymentDto).toList();
    }

    private List<ExpenseDto> getAllExpensesByMonthOfYear(LocalDate from, LocalDate to) {
        Specification<Expense> spec = BalanceSummarySpecification.filterByDate(from, to);
        var expenses = expenseRepository.findAll(spec);
        return expenses.stream().map(FinanceMapper::toExpenseDto).toList();
    }

    private List<RefundDto> getAllRefundsByMonthOfYear(LocalDate from, LocalDate to) {
        Specification<Refund> spec = BalanceSummarySpecification.filterByDate(from, to);
        var refunds = refundRepository.findAll(spec);
        return refunds.stream().map(FinanceMapper::toRefundDto).toList();
    }
}
