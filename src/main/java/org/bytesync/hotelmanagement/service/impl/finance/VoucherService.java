package org.bytesync.hotelmanagement.service.impl.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.VoucherCreatForm;
import org.bytesync.hotelmanagement.dto.finance.VoucherDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.Voucher;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.bytesync.hotelmanagement.repository.VoucherRepository;
import org.bytesync.hotelmanagement.specification.DailyVoucherSpecification;
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
import static org.bytesync.hotelmanagement.enums.IncomeType.ROOM_RENT;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.getCurrentYangonZoneLocalDateTime;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class VoucherService implements IVoucherService {

    private final VoucherRepository voucherRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public void createVoucherFromReservation(Reservation reservation) {

        var voucher = switch (reservation.getStayType()) {
            case SECTION -> createVoucherForSectionStay(reservation);
            case NORMAL -> createVoucherForNormalStay(reservation);
            case LONG -> createVoucherForLongStay(reservation);
        };

        reservation.addVoucher(voucher);

        voucherRepository.save(voucher);
    }


    @Override
    public void createAdditionalVoucher(VoucherCreatForm form) {
        var reservation = safeCall(reservationRepository.findById(form.reservationId()), "Reservation", form.reservationId());
        var extendVoucher = createVoucherForNormalStay(reservation);
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

    private Voucher createVoucherForSectionStay(Reservation reservation) {
        var voucher = getVoucherFromReservation(reservation);
        var price = voucher.getPrice() - reservation.getDiscount();
        voucher.setPrice(price);
        return voucher;
    }

    private Voucher createVoucherForNormalStay(Reservation reservation) {
        var voucher = getVoucherFromReservation(reservation);
        var price = (reservation.getPrice() * reservation.getDaysOfStay()) - reservation.getDiscount();
        voucher.setPrice(price);
        return voucher;
    }

    private Voucher createVoucherForLongStay(Reservation reservation) {
        var voucher = getVoucherFromReservation(reservation);
        processDailyPayment(voucher);
        return voucher;
    }

    private Voucher getVoucherFromReservation(Reservation reservation) {
        var now = getCurrentYangonZoneLocalDateTime();
        return Voucher.builder()
                .reservation(reservation)
                .date(now.toLocalDate())
                .guestName(reservation.getGuest().getName())
                .roomNo(reservation.getRoom().getRoomNo())
                .price(reservation.getPrice())
                .isPaid(false)
                .type(ROOM_RENT)
                .build();
    }


    private void processDailyPayment(Voucher dailyVoucher) {
        var reservation = dailyVoucher.getReservation();
        var deposit = reservation.getDeposit();
        var pricePerNight = reservation.getPrice();

        if(deposit >= pricePerNight) {
            deposit = deposit - pricePerNight;
            dailyVoucher.setIsPaid(true);
            reservation.setDeposit(deposit);
            createPaymentToDailyVoucher(dailyVoucher);
        }
    }

    private void createPaymentToDailyVoucher(Voucher dailyVoucher) {
        var reservation = dailyVoucher.getReservation();
        var payment = Payment.builder()
                .date(LocalDate.now())
                .amount(reservation.getPrice())
                .paymentMethod(DEPOSIT)
                .type(ROOM_RENT)
                .notes("Automatic paid from deposit")
                .vouchers(new ArrayList<>())
                .build();
        payment.setReservation(reservation);
        payment.addDailyVoucher(dailyVoucher);
    }

}
