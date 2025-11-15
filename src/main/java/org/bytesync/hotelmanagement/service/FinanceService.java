package org.bytesync.hotelmanagement.service;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.DailyVoucherDto;
import org.bytesync.hotelmanagement.dto.finance.PaymentCreateForm;
import org.bytesync.hotelmanagement.dto.finance.PaymentDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.model.DailyVoucher;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.repository.DailyVoucherRepository;
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

import java.util.ArrayList;
import java.util.List;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final DailyVoucherRepository dailyVoucherRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

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
        paymentRepository.save(payment);
        return "Payment created successfully";
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
}