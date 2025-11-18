package org.bytesync.hotelmanagement.service;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.*;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.model.DailyVoucher;
import org.bytesync.hotelmanagement.model.Expense;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.repository.DailyVoucherRepository;
import org.bytesync.hotelmanagement.repository.ExpenseRepository;
import org.bytesync.hotelmanagement.repository.PaymentRepository;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.bytesync.hotelmanagement.repository.specification.DailyVoucherSpecification;
import org.bytesync.hotelmanagement.util.mapper.DailyVoucherMapper;
import org.bytesync.hotelmanagement.util.mapper.FinanceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final DailyVoucherRepository dailyVoucherRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;

    public PageResult<DailyVoucherDto> getDailyVouchersByReservation(long reservationId, boolean isPaid, int page, int size) {
        var pageable = PageRequest.of(page, size).withSort(Sort.Direction.ASC, "date");
        var spec = DailyVoucherSpecification.byReservationId(reservationId, isPaid);

        Page<DailyVoucher> vouchers = dailyVoucherRepository.findAll(spec, pageable);
        List<DailyVoucherDto> dtos = vouchers.stream()
                .map(DailyVoucherMapper::toDto).toList();
        return new PageResult<>(dtos, vouchers.getTotalElements(), page, size);
    }

    public List<DailyVoucherDto> getSelectedVouchers(List<String> voucherIds) {
        return getDailyVouchers(voucherIds).stream()
                .map(DailyVoucherMapper::toDto)
                .toList();
    }

    public String createPayment(PaymentCreateForm paymentCreateForm) {
        var reservation = safeCall(reservationRepository.findById(paymentCreateForm.getReservationId()),
                "Reservation", paymentCreateForm.getReservationId());

        var dailyVouchers = getDailyVouchers(paymentCreateForm.getVoucherIds());

        var payment = FinanceMapper.toPayment(paymentCreateForm);

        payment.setReservation(reservation);
        dailyVouchers.forEach(voucher -> {
            voucher.setIsPaid(true);
            payment.addDailyVoucher(voucher);
        });
        var id = paymentRepository.save(payment).getId();
        return "Payment created successfully : " + id;
    }

    private List<DailyVoucher> getDailyVouchers(List<String>  voucherIds) {
        List<DailyVoucher> dailyVouchers = new ArrayList<>();
        voucherIds.forEach(id -> {
            dailyVoucherRepository.findById(id).ifPresent(dailyVouchers::add);
        });
        return dailyVouchers;
    }

    public PageResult<PaymentDto> getPaymentList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("paymentDate").descending());
        Page<Payment> all = paymentRepository.findAll(pageable);
        var dtos = all.getContent().stream().map(FinanceMapper::toPaymentDto).toList();
        return new  PageResult<>(dtos, all.getTotalElements(), page, size);
    }

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

    public List<PaymentDto> getAllPaymentsByMonthOfYear(int year, int month) {
        var payments = paymentRepository.findAllInMonthOfYear(year, month);
        return payments.stream().map(FinanceMapper::toPaymentDto).toList();
    }

    public List<ExpenseDto> getAllExpensesByMonthOfYear(int year, int month) {
        var expenses = expenseRepository.findAllInMonthOfYear(year, month);
        return expenses.stream().map(FinanceMapper::toExpenseDto).toList();
    }

    public MonthlyBalanceSheet getMonthlyBalance(int year, int month) {
        MonthlyBalanceSheet monthlyBalanceSheet = new MonthlyBalanceSheet();
        monthlyBalanceSheet.setPeriod(getPeriodString(year, month));
        populateMoneyInMonthlyBalance(monthlyBalanceSheet, year, month);
        return monthlyBalanceSheet;
    }

    private void populateMoneyInMonthlyBalance(MonthlyBalanceSheet monthlyBalanceSheet, int year, int month) {
        var paymentDtos = getAllPaymentsByMonthOfYear(year, month);
        var expenseDtos = getAllExpensesByMonthOfYear(year, month);
        var totalIncomes = paymentDtos.stream().map(PaymentDto::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var totalExpenses = expenseDtos.stream().map(ExpenseDto::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var profit =  totalIncomes - totalExpenses;

        var dailyBalances = getDailyBalanceList(paymentDtos, expenseDtos, year, month).stream()
                .filter(dailyBalance -> dailyBalance.getTotalIncomes() != 0 && dailyBalance.getTotalExpenses() != 0).toList();

        monthlyBalanceSheet.setIncomes(paymentDtos);
        monthlyBalanceSheet.setExpenses(expenseDtos);
        monthlyBalanceSheet.setTotalIncome(totalIncomes);
        monthlyBalanceSheet.setTotalExpense(totalExpenses);
        monthlyBalanceSheet.setProfit(profit);
        monthlyBalanceSheet.setDailyBalances(dailyBalances);
    }

    private String getPeriodString(int year, int month) {
        Month m = Month.of(month);
        return  String.format("%s-%d", m, year);
    }

    private List<DailyBalance> getDailyBalanceList(List<PaymentDto> paymentDtos, List<ExpenseDto> expenseDtos, int year, int month) {
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

        var current = start;

        while (!current.isAfter(end)) {
            int dailyIncome = incomeMap.getOrDefault(current, 0);
            int dailyExpense = expenseMap.getOrDefault(current, 0);
            int dailyProfit = dailyIncome - dailyExpense;

            dailyBalances.add(new DailyBalance(current, dailyIncome, dailyExpense, dailyProfit));

            current = current.plusDays(1); // IMPORTANT: move to next day
        }

        return dailyBalances;
    }

    public Integer getDailyIncomeAmount() {
        var today = LocalDate.now();
        var payments =paymentRepository.findByPaymentDate(today);

        return payments.stream().map(Payment::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
    }

    public String updateExpense(String id, ExpenseDto form) {
        var expense = safeCall(expenseRepository.findById(id), "Expense", id);
        FinanceMapper.updateExpense(expense, form);
        expenseRepository.save(expense);
        return "Expense updated successfully : " + expense.getId();
    }

    public String deleteExpense(String id) {
        var expense = safeCall(expenseRepository.findById(id), "Expense", id);
        expenseRepository.delete(expense);
        return "Expense deleted successfully : " + expense.getId();
    }
}