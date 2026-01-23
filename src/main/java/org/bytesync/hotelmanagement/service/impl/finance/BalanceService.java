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
import org.bytesync.hotelmanagement.util.mapper.FinanceMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.bytesync.hotelmanagement.enums.IncomeType.ROOM_RENT;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.dateFormat;

@Service
@RequiredArgsConstructor
public class BalanceService implements IBalanceService {

    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;
    private final RefundRepository refundRepository;

    @Override
    public BalanceSummarySheet getBalanceSummarySheet(LocalDate from, LocalDate to) {
        Specification<Payment> paymentSpecification = FinanceSpecification.paymentFilterByDate(from, to, ROOM_RENT);
        Specification<Expense> expenseSpecification = FinanceSpecification.expenseFilterByDate(from, to, null);
        Specification<Refund> refundSpecification = FinanceSpecification.refundFilterByDate(from, to, null);

        var totalIncome = paymentRepository.findAll(paymentSpecification).stream()
                .filter(payment -> payment.getIncomeType().equals(ROOM_RENT))
                .map(Payment::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var totalExpense = expenseRepository.findAll(expenseSpecification).stream()
                .map(Expense::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var totalRefund = refundRepository.findAll(refundSpecification).stream()
                .map(Refund::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
        var profit =  totalIncome - totalExpense;

        return BalanceSummarySheet.builder()
                .period(getPeriodString(from, to))
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .totalRefund(totalRefund)
                .profit(profit)
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
}
