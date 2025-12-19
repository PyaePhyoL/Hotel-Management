package org.bytesync.hotelmanagement.service.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.*;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.exception.NotEnoughMoneyException;
import org.bytesync.hotelmanagement.model.Expense;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Refund;
import org.bytesync.hotelmanagement.model.enums.ExpenseType;
import org.bytesync.hotelmanagement.repository.*;
import org.bytesync.hotelmanagement.util.mapper.FinanceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class PaymentService {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;
    private final DailyVoucherService dailyVoucherService;


    public String createPayment(PaymentCreateForm paymentCreateForm) {
        var reservation = safeCall(reservationRepository.findById(paymentCreateForm.getReservationId()),
                "Reservation", paymentCreateForm.getReservationId());

        var dailyVouchers = dailyVoucherService.getDailyVouchers(paymentCreateForm.getVoucherIds());

        var payment = FinanceMapper.toPayment(paymentCreateForm);

        payment.setReservation(reservation);
        dailyVouchers.forEach(voucher -> {
            voucher.setIsPaid(true);
            payment.addDailyVoucher(voucher);
        });
        var id = paymentRepository.save(payment).getId();
        return "Payment created successfully : " + id;
    }

    public PageResult<PaymentDto> getPaymentList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("paymentDate").descending());
        Page<Payment> all = paymentRepository.findAll(pageable);
        var dtos = all.getContent().stream().map(FinanceMapper::toPaymentDto).toList();
        return new  PageResult<>(dtos, all.getTotalElements(), page, size);
    }







    public Integer getDailyIncomeAmount() {
        var today = LocalDate.now();
        var payments =paymentRepository.findByPaymentDate(today);

        return payments.stream().map(Payment::getAmount).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
    }



    public PaymentCreateForm getSectionPaymentForm(Long reservationId) {
        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);

        PaymentCreateForm form = new PaymentCreateForm();
        form.setPaymentDate(LocalDate.now());
        form.setAmount(reservation.getPricePerNight());
        return form;
    }
}