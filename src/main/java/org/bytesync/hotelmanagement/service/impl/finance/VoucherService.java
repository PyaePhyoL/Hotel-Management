package org.bytesync.hotelmanagement.service.impl.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.VoucherDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.Voucher;
import org.bytesync.hotelmanagement.enums.VoucherType;
import org.bytesync.hotelmanagement.repository.VoucherRepository;
import org.bytesync.hotelmanagement.repository.specification.DailyVoucherSpecification;
import org.bytesync.hotelmanagement.util.mapper.VoucherMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.bytesync.hotelmanagement.enums.PaymentMethod.DEPOSIT;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.getDaysBetween;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;

    public void createVoucher(Reservation reservation) {

        var baseVoucher = createBasicVoucherFromReservation(reservation);

        switch (baseVoucher.getType()) {
            case DAILY -> changeForDailyVoucher(baseVoucher);
            case CASH_DOWN -> changeForCashDownVoucher(baseVoucher);
            case SECTION ->  {}
        }

        reservation.addVoucher(baseVoucher);

        voucherRepository.save(baseVoucher);
    }


    public PageResult<VoucherDto> getVoucherListByReservation(long reservationId, boolean isPaid, int page, int size) {
        var pageable = PageRequest.of(page, size).withSort(Sort.Direction.ASC, "date");
        var spec = DailyVoucherSpecification.byReservationId(reservationId, isPaid);

        Page<Voucher> vouchers = voucherRepository.findAll(spec, pageable);
        List<VoucherDto> dtos = vouchers.stream()
                .map(VoucherMapper::toDto).toList();
        return new PageResult<>(dtos, vouchers.getTotalElements(), page, size);
    }

    public List<VoucherDto> getSelectedVoucherDtos(List<Long> voucherIds) {
        return getVouchers(voucherIds).stream()
                .map(VoucherMapper::toDto)
                .toList();
    }

    public List<Voucher> getVouchers(List<Long>  voucherIds) {
        List<Voucher> vouchers = new ArrayList<>();
        voucherIds.forEach(id -> {
            voucherRepository.findById(id).ifPresent(vouchers::add);
        });
        return vouchers;
    }

    public void createExtendVoucher(Reservation reservation, int price) {
        var extendVoucher = createBasicVoucherFromReservation(reservation);
        extendVoucher.setPrice(price);
        extendVoucher.setType(VoucherType.EXTEND);
        voucherRepository.save(extendVoucher);
    }

    private Voucher createBasicVoucherFromReservation(Reservation reservation) {
        var voucherType = VoucherType.getVoucherTypeFromStayType(reservation.getStayType());
        return Voucher.builder()
                .reservation(reservation)
                .date(LocalDate.now())
                .guestName(reservation.getGuest().getName())
                .roomNo(reservation.getRoom().getRoomNo())
                .price(reservation.getPrice())
                .isPaid(false)
                .type(voucherType)
                .build();
    }

    private void changeForDailyVoucher(Voucher baseVoucher) {
        boolean isPaid = processDailyPayment(baseVoucher);
        baseVoucher.setIsPaid(isPaid);
    }


    private boolean processDailyPayment(Voucher dailyVoucher) {
        var reservation = dailyVoucher.getReservation();
        var deposit = reservation.getDeposit();
        var pricePerNight = reservation.getPrice();
        var paid = false;
        if(deposit >= pricePerNight) {
            deposit = deposit - pricePerNight;
            paid = true;
            reservation.setDeposit(deposit);
            createPaymentToDailyVoucher(dailyVoucher);
        }
        return paid;
    }

    private void createPaymentToDailyVoucher(Voucher dailyVoucher) {
        var reservation = dailyVoucher.getReservation();
        var payment = Payment.builder()
                .paymentDate(LocalDate.now())
                .amount(reservation.getPrice())
                .paymentMethod(DEPOSIT)
                .notes("Automatic paid from deposit")
                .vouchers(new ArrayList<>())
                .build();
        payment.setReservation(reservation);
        payment.addDailyVoucher(dailyVoucher);
    }

    private void changeForCashDownVoucher(Voucher voucher) {
        var price = calculatePriceForNormalStay(voucher.getReservation());
        voucher.setPrice(price);
    }

    private int calculatePriceForNormalStay(Reservation reservation) {
        int days = getDaysBetween(
                reservation.getCheckInDateTime().toLocalDate(),
                reservation.getCheckOutDateTime().toLocalDate());

        return days * reservation.getPrice();
    }

}
