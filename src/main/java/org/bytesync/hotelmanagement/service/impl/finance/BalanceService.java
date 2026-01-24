package org.bytesync.hotelmanagement.service.impl.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.*;
import org.bytesync.hotelmanagement.model.Expense;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Refund;
import org.bytesync.hotelmanagement.repository.ExpenseRepository;
import org.bytesync.hotelmanagement.repository.PaymentRepository;
import org.bytesync.hotelmanagement.repository.RefundRepository;
import org.bytesync.hotelmanagement.service.interfaces.finance.IBalanceService;
import org.bytesync.hotelmanagement.specification.FinanceSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.bytesync.hotelmanagement.enums.IncomeType.ROOM_RENT;
import static org.bytesync.hotelmanagement.enums.RefundType.PAYMENT_REFUND;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.dateFormat;

@Service
@RequiredArgsConstructor
public class BalanceService implements IBalanceService {

    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;
    private final RefundRepository refundRepository;

    @Override
    public BalanceSummarySheet getBalanceSummarySheet(LocalDate from, LocalDate to) {
        Specification<Payment> paymentSpecification = FinanceSpecification.financeFilter(new FinanceFilterDto(from, to, null, ROOM_RENT.toString()));
        Specification<Expense> expenseSpecification = FinanceSpecification.financeFilter(new FinanceFilterDto(from, to, null, null));
        Specification<Refund> refundSpecification = FinanceSpecification.financeFilter(new FinanceFilterDto(from, to, null, PAYMENT_REFUND.toString()));

        List<Payment> paymentList = paymentRepository.findAll(paymentSpecification);
        List<Expense> expenseList = expenseRepository.findAll(expenseSpecification);
        List<Refund> refundList = refundRepository.findAll(refundSpecification);

        var totalIncome = paymentList.stream().filter(payment -> payment.getType().equals(ROOM_RENT))
                .map(Payment::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var totalExpense = expenseList.stream().map(Expense::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var totalRefund = refundList.stream().map(Refund::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var profit =  totalIncome - totalExpense;

        List<DailyBalance> dailyBalanceList = getDailyBalanceList(paymentList, expenseList, refundList, from, to);

        return BalanceSummarySheet.builder()
                .period(getPeriodString(from, to))
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .totalRefund(totalRefund)
                .profit(profit)
                .dailyBalances(dailyBalanceList)
                .build();
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

    private List<DailyBalance> getDailyBalanceList(List<Payment> paymentList,
                                                   List<Expense> expenseDtos,
                                                   List<Refund> refundDtos,
                                                   LocalDate startDate,
                                                   LocalDate endDate) {

        List<DailyBalance> dailyBalances = new ArrayList<>();

        // Pre-group incomes by date
        Map<LocalDate, Integer> incomeMap = paymentList.stream()
                .collect(Collectors.groupingBy(
                        Payment::getDate,
                        Collectors.summingInt(Payment::getAmount)
                ));

        // Pre-group expenses by date
        Map<LocalDate, Integer> expenseMap = expenseDtos.stream()
                .collect(Collectors.groupingBy(
                        Expense::getDate,
                        Collectors.summingInt(Expense::getAmount)
                ));

        Map<LocalDate, Integer> refundMap = refundDtos.stream()
                .collect(Collectors.groupingBy(
                        Refund::getDate,
                        Collectors.summingInt(Refund::getAmount)
                ));

        var current = startDate;

        while (!current.isAfter(endDate)) {
            int dailyIncome = incomeMap.getOrDefault(current, 0);
            int dailyExpense = expenseMap.getOrDefault(current, 0);
            int dailyRefund = refundMap.getOrDefault(current, 0);
            int dailyProfit = dailyIncome - dailyExpense;

            dailyBalances.add(new DailyBalance(current, dailyIncome, dailyExpense, dailyRefund, dailyProfit));

            current = current.plusDays(1); // IMPORTANT: move to next day
        }

        return dailyBalances;
    }


}
