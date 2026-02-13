package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.finance.*;
import org.bytesync.hotelmanagement.enums.RefundType;
import org.bytesync.hotelmanagement.model.Expense;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Refund;
import org.bytesync.hotelmanagement.model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class FinanceMapper {

    private FinanceMapper() {
    }

    public static PaymentListDto toPaymentListDto(Payment payment) {
        var reservation = payment.getReservation();

        var amountAndMethods = convertPaymentMapToListString(payment);

        return PaymentListDto.builder()
                .id(payment.getId())
                .paymentDate(payment.getDate())
                .amountAndMethods(amountAndMethods)
                .incomeType(payment.getType())
                .notes(payment.getNotes())
                .reservationId(reservation.getId())
                .guestName(payment.getGuest().getName())
                .roomNo(reservation.getRoom().getRoomNo())
                .build();
    }

    public static PaymentDetailsDto toPaymentDetailsDto(Payment payment) {
        var reservation = payment.getReservation();
        var vouchers = payment.getVouchers().stream().map(FinanceMapper::toVoucherDto).toList();
        var amountAndMethods = convertPaymentMapToListString(payment);
        return PaymentDetailsDto.builder()
                .id(payment.getId())
                .paymentDate(payment.getDate())
                .amountAndMethods(amountAndMethods)
                .incomeType(payment.getType())
                .notes(payment.getNotes())
                .reservationId(reservation.getId())
                .guestName(reservation.getGuest().getName())
                .roomNo(reservation.getRoom().getRoomNo())
                .vouchers(vouchers)
                .build();
    }


    private static List<String> convertPaymentMapToListString(Payment payment) {
        List<String> amountAndMethods = new ArrayList<>();
        payment.getPaymentAmountMap().forEach(
                ((paymentMethod, amount) ->
                        amountAndMethods.add("%d (%s)".formatted(amount, paymentMethod))));

        return amountAndMethods;
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

    public static Refund toRefund(RefundDto refundDto, RefundType refundType) {
        return Refund.builder()
                .date(refundDto.getRefundDate())
                .amount(refundDto.getAmount())
                .notes(refundDto.getNotes())
                .type(refundType)
                .build();
    }

    public static RefundDto toRefundDto(Refund refund) {
        return RefundDto.builder()
                .id(refund.getId())
                .refundDate(refund.getDate())
                .guestName(refund.getGuest().getName())
                .amount(refund.getAmount())
                .type(refund.getType())
                .notes(refund.getNotes())
                .build();
    }

    public static VoucherDto toVoucherDto(Voucher voucher) {
        var payment = voucher.getPayment();
        return VoucherDto.builder()
                .voucherNo(voucher.getVoucherNo())
                .type(voucher.getType())
                .paymentId(null == payment ? null : payment.getId())
                .date(voucher.getDate())
                .reservationId(voucher.getReservation().getId())
                .guestName(voucher.getGuestName())
                .roomNo(voucher.getRoomNo())
                .price(voucher.getPrice())
                .isPaid(voucher.getIsPaid())
                .notes(voucher.getNotes())
                .build();
    }
}
