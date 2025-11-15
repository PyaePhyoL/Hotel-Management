package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.finance.PaymentCreateForm;
import org.bytesync.hotelmanagement.dto.finance.PaymentDto;
import org.bytesync.hotelmanagement.model.Payment;

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
                .dailyVouchers(new ArrayList<>())
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
}
