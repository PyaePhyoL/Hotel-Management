package org.bytesync.hotelmanagement.service.impl.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.*;
import org.bytesync.hotelmanagement.repository.ExpenseRepository;
import org.bytesync.hotelmanagement.repository.PaymentRepository;
import org.bytesync.hotelmanagement.repository.RefundRepository;
import org.bytesync.hotelmanagement.service.interfaces.finance.IBalanceService;
import org.bytesync.hotelmanagement.util.mapper.FinanceMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalanceService implements IBalanceService {

    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;
    private final RefundRepository refundRepository;

    @Override
    public MonthlyBalanceSheet getMonthlyBalance(int year, int month) {
        MonthlyBalanceSheet monthlyBalanceSheet = new MonthlyBalanceSheet();
        monthlyBalanceSheet.setPeriod(getPeriodString(year, month));
        populateMoneyInMonthlyBalance(monthlyBalanceSheet, year, month);
        return monthlyBalanceSheet;
    }

    private void populateMoneyInMonthlyBalance(MonthlyBalanceSheet monthlyBalanceSheet, int year, int month) {
        var paymentDtos = getAllPaymentsByMonthOfYear(year, month);
        var expenseDtos = getAllExpensesByMonthOfYear(year, month);
        var refundDtos = getAllRefundsByMonthOfYear(year, month);

        var totalIncomes = paymentDtos.stream().map(PaymentDto::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var totalExpenses = expenseDtos.stream().map(ExpenseDto::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var totalRefunds = refundDtos.stream().map(RefundDto::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var profit =  totalIncomes - totalExpenses;

        var dailyBalances = getDailyBalanceList(paymentDtos, expenseDtos, refundDtos, year, month).stream()
                .filter(dailyBalance -> dailyBalance.getTotalIncomes() != 0 ||
                                                    dailyBalance.getTotalExpenses() != 0 ||
                                                    dailyBalance.getTotalRefunds() != 0).toList();

        monthlyBalanceSheet.setIncomes(paymentDtos);
        monthlyBalanceSheet.setExpenses(expenseDtos);
        monthlyBalanceSheet.setTotalIncome(totalIncomes);
        monthlyBalanceSheet.setTotalExpense(totalExpenses);
        monthlyBalanceSheet.setTotalRefund(totalRefunds);
        monthlyBalanceSheet.setProfit(profit);
        monthlyBalanceSheet.setDailyBalances(dailyBalances);
    }



    private String getPeriodString(int year, int month) {
        Month m = Month.of(month);
        return  String.format("%s-%d", m, year);
    }

    private List<DailyBalance> getDailyBalanceList(List<PaymentDto> paymentDtos, List<ExpenseDto> expenseDtos, List<RefundDto> refundDtos, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        var start = yearMonth.atDay(1);
        var end = yearMonth.atEndOfMonth();
        List<DailyBalance> dailyBalances = new ArrayList<>();

        // Pre-group incomes by date
        Map<LocalDate, Integer> incomeMap = paymentDtos.stream()
                .collect(Collectors.groupingBy(
                        PaymentDto::getPaymentDate,
                        Collectors.summingInt(PaymentDto::getAmount)
                ));

        // Pre-group expenses by date
        Map<LocalDate, Integer> expenseMap = expenseDtos.stream()
                .collect(Collectors.groupingBy(
                        ExpenseDto::getDate,
                        Collectors.summingInt(ExpenseDto::getAmount)
                ));

        Map<LocalDate, Integer> refundMap = refundDtos.stream()
                .collect(Collectors.groupingBy(
                        RefundDto::getRefundDate,
                        Collectors.summingInt(RefundDto::getAmount)
                ));

        var current = start;

        while (!current.isAfter(end)) {
            int dailyIncome = incomeMap.getOrDefault(current, 0);
            int dailyExpense = expenseMap.getOrDefault(current, 0);
            int dailyRefund = refundMap.getOrDefault(current, 0);
            int dailyProfit = dailyIncome - dailyExpense;

            dailyBalances.add(new DailyBalance(current, dailyIncome, dailyExpense, dailyRefund, dailyProfit));

            current = current.plusDays(1); // IMPORTANT: move to next day
        }

        return dailyBalances;
    }

    private List<PaymentDto> getAllPaymentsByMonthOfYear(int year, int month) {
        var payments = paymentRepository.findAllRoomRentPaymentsInMonthOfYear(year, month);
        return payments.stream().map(FinanceMapper::toPaymentDto).toList();
    }

    private List<ExpenseDto> getAllExpensesByMonthOfYear(int year, int month) {
        var expenses = expenseRepository.findAllInMonthOfYear(year, month);
        return expenses.stream().map(FinanceMapper::toExpenseDto).toList();
    }

    private List<RefundDto> getAllRefundsByMonthOfYear(int year, int month) {
        var refunds = refundRepository.findAllInMonthOfYear(year, month);
        return refunds.stream().map(FinanceMapper::toRefundDto).toList();
    }
}
