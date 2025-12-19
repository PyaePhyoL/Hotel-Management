package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.finance.ExpenseDto;
import org.bytesync.hotelmanagement.dto.finance.PaymentCreateForm;
import org.bytesync.hotelmanagement.dto.finance.PaymentDto;
import org.bytesync.hotelmanagement.dto.finance.RefundDto;
import org.bytesync.hotelmanagement.model.Expense;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Refund;

import java.util.ArrayList;

public class FinanceMapper {

    private FinanceMapper() {
    }

    public static Payment toPayment(PaymentCreateForm paymentCreateForm) {
        return Payment.builder()
                .paymentDate(paymentCreateForm.getPaymentDate())
                .amount(paymentCreateForm.getAmount())
                .paymentMethod(paymentCreateForm.getPaymentMethod())
                .notes(paymentCreateForm.getNotes())
                .vouchers(new ArrayList<>())
                .build();
    }

    public static PaymentDto toPaymentDto(Payment payment) {
        var reservation = payment.getReservation();
        return PaymentDto.builder()
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .notes(payment.getNotes())
                .reservationId(reservation.getId())
                .guestName(reservation.getGuest().getName())
                .roomNo(reservation.getRoom().getRoomNo())
                .build();
    }

    public static Expense toExpense(ExpenseDto form) {
        return Expense.builder()
                .title(form.getTitle())
                .date(form.getDate())
                .amount(form.getAmount())
                .type(form.getType())
                .notes(form.getNotes())
                .build();
    }

    public static ExpenseDto toExpenseDto(Expense expense) {
        return ExpenseDto.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .date(expense.getDate())
                .amount(expense.getAmount())
                .type(expense.getType())
                .notes(expense.getNotes())
                .build();
    }

    public static void updateExpense(Expense expense, ExpenseDto form) {
        expense.setTitle(form.getTitle());
        expense.setDate(form.getDate());
        expense.setAmount(form.getAmount());
        expense.setType(form.getType());
        expense.setNotes(form.getNotes());
    }

    public static Refund toRefund(RefundDto refundDto) {
        return Refund.builder()
                .refundDate(refundDto.getRefundDate())
                .amount(refundDto.getAmount())
                .notes(refundDto.getNotes())
                .build();
    }

    public static RefundDto toRefundDto(Refund refund) {
        return RefundDto.builder()
                .id(refund.getId())
                .refundDate(refund.getRefundDate())
                .amount(refund.getAmount())
                .notes(refund.getNotes())
                .build();
    }
}
