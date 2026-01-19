package org.bytesync.hotelmanagement.service.impl.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.VoucherCreatForm;
import org.bytesync.hotelmanagement.dto.finance.VoucherDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.enums.PaymentType;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.Voucher;
import org.bytesync.hotelmanagement.enums.VoucherType;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.bytesync.hotelmanagement.repository.VoucherRepository;
import org.bytesync.hotelmanagement.repository.specification.DailyVoucherSpecification;
import org.bytesync.hotelmanagement.service.interfaces.finance.IVoucherService;
import org.bytesync.hotelmanagement.util.mapper.FinanceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.bytesync.hotelmanagement.enums.PaymentMethod.DEPOSIT;
import static org.bytesync.hotelmanagement.enums.PaymentType.ROOM_RENT_PAYMENT;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class VoucherService implements IVoucherService {

    private final VoucherRepository voucherRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public void createVoucherFromReservation(Reservation reservation) {

        var baseVoucher = createBasicVoucherFromReservation(reservation);

        changeForDailyVoucher(baseVoucher);

        reservation.addVoucher(baseVoucher);

        voucherRepository.save(baseVoucher);
    }

    @Override
    public void createAdditionalVoucher(VoucherCreatForm form) {
        var reservation = safeCall(reservationRepository.findById(form.reservationId()), "Reservation", form.reservationId());
        var extendVoucher = createBasicVoucherFromReservation(reservation);
        extendVoucher.setPrice(form.price());
        extendVoucher.setType(form.type());
        extendVoucher.setNotes(form.notes());
        voucherRepository.save(extendVoucher);
    }

    @Override
    public PageResult<VoucherDto> getVoucherListByReservation(Long reservationId, boolean isPaid, int page, int size) {
        var pageable = PageRequest.of(page, size).withSort(Sort.Direction.ASC, "date");
        var spec = DailyVoucherSpecification.byReservationId(reservationId, isPaid);

        Page<Voucher> vouchers = voucherRepository.findAll(spec, pageable);
        List<VoucherDto> dtos = vouchers.stream()
                .map(FinanceMapper::toVoucherDto).toList();
        return new PageResult<>(dtos, vouchers.getTotalElements(), page, size);
    }

    @Override
    public List<VoucherDto> getSelectedVoucherDtos(List<Long> voucherIds) {
        return getVouchers(voucherIds).stream()
                .map(FinanceMapper::toVoucherDto)
                .toList();
    }

    @Override
    public List<Voucher> getVouchers(List<Long>  voucherIds) {
        List<Voucher> vouchers = new ArrayList<>();
        voucherIds.forEach(id -> {
            voucherRepository.findById(id).ifPresent(vouchers::add);
        });
        return vouchers;
    }

    @Override
    public String updateVoucher(Long id, VoucherDto voucherDto) {
        var voucher = safeCall(voucherRepository.findById(id), "Voucher", id);
        voucher.setPrice(voucherDto.price());
        voucher.setType(voucherDto.type());
        voucher.setNotes(voucherDto.notes());
        voucher.setIsPaid(voucherDto.isPaid());

        voucherRepository.save(voucher);
        return "Voucher updated";
    }

    @Override
    public VoucherDto getVoucherDetails(Long id) {
        var voucher = safeCall(voucherRepository.findById(id), "Voucher", id);
        return FinanceMapper.toVoucherDto(voucher);
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
        if(baseVoucher.getType() != VoucherType.DAILY) return;
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
                .paymentType(ROOM_RENT_PAYMENT)
                .notes("Automatic paid from deposit")
                .vouchers(new ArrayList<>())
                .build();
        payment.setReservation(reservation);
        payment.addDailyVoucher(dailyVoucher);
    }


}
