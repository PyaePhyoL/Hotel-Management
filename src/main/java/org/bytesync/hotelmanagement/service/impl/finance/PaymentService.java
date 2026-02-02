package org.bytesync.hotelmanagement.service.impl.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.*;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.enums.IncomeType;
import org.bytesync.hotelmanagement.enums.PaymentMethod;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Voucher;
import org.bytesync.hotelmanagement.repository.*;
import org.bytesync.hotelmanagement.specification.FinanceSpecification;
import org.bytesync.hotelmanagement.service.interfaces.finance.IPaymentService;
import org.bytesync.hotelmanagement.util.mapper.FinanceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.bytesync.hotelmanagement.enums.IncomeType.ROOM_RENT;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final VoucherService voucherService;

    @Override
    public String createPayment(PaymentCreateForm paymentCreateForm) {
        var reservation = safeCall(reservationRepository.findById(paymentCreateForm.getReservationId()),
                "Reservation", paymentCreateForm.getReservationId());

        var vouchers = voucherService.getVouchers(paymentCreateForm.getVoucherIds());

        validateVoucherTypesConsistency(vouchers, paymentCreateForm.getIncomeType());

        var payment = FinanceMapper.toPayment(paymentCreateForm);

        payment.setReservation(reservation);
        payment.setGuest(reservation.getGuest());

        vouchers.forEach(voucher -> {
            voucher.setIsPaid(true);
            payment.addVoucher(voucher);
        });

        var id = paymentRepository.save(payment).getId();
        return "Payment created successfully : " + id;
    }

    private void validateVoucherTypesConsistency(List<Voucher> voucherList, IncomeType type) {
        if(voucherList.isEmpty()) throw new IllegalArgumentException("Voucher list is empty");

        IncomeType firstType = voucherList.getFirst().getType();

        voucherList.forEach(v -> {
            if (v.getType() != firstType) throw new IllegalArgumentException("Contain different types. Please split the vouchers");
        });

        if(firstType != type) {
            throw new IllegalArgumentException("Chosen income type does not match with voucher type");
        }
    }

    @Override
    public PageResult<PaymentDto> getPaymentList(int page, int size, FinanceFilterDto filterDto) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Specification<Payment> spec = FinanceSpecification.financeFilter(filterDto);
        Page<Payment> all = paymentRepository.findAll(spec, pageable);
        var dtos = all.getContent().stream().map(FinanceMapper::toPaymentDto).toList();
        return new  PageResult<>(dtos, all.getTotalElements(), page, size);
    }

    @Override
    public Integer getDailyIncomeAmount() {
        var today = LocalDate.now();
        var payments =paymentRepository.findByDate(today);

        return payments.stream()
                .filter(payment -> payment.getType().equals(ROOM_RENT))
                .map(Payment::getAmount)
                .filter(Objects::nonNull)
                .reduce(Integer::sum).orElse(0);
    }

    @Override
    public String updateExpenditureAmount(Long id, PaymentDto paymentDto) {
        var payment = safeCall(paymentRepository.findById(id), "Payment", id);

        if(payment.getPaymentMethod() != PaymentMethod.EXPEDIA) {
            throw new IllegalArgumentException("Payment method is not from Expedia");
        }

        payment.setAmount(paymentDto.getAmount());
        payment.setNotes(paymentDto.getNotes());
        paymentRepository.save(payment);
        return "Payment updated successfully : " + id;
    }

    @Override
    public PageResult<PaymentDto> getPaymentListByReservation(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Specification<Payment> spec = FinanceSpecification.paymentFilterByReservation(id);
        Page<Payment> paymentPage = paymentRepository.findAll(spec, pageable);

        List<PaymentDto> dtoList = paymentPage.getContent().stream().map(FinanceMapper::toPaymentDto).toList();
        return new PageResult<>(dtoList, paymentPage.getTotalElements(), page, size);
    }

    @Override
    public PaymentDetailsDto getPaymentDetailsById(Long id) {
        var payment = safeCall(paymentRepository.findById(id), "Payment", id);
        return FinanceMapper.toPaymentDetailsDto(payment);
    }

    @Override
    public Integer getDailyIncomeAmountByPaymentMethod(PaymentMethod type) {
        LocalDate today = LocalDate.now();
        var payments = paymentRepository.findByDateAndPaymentMethodAndIncomeType(today, type, ROOM_RENT);
        return payments.stream()
                .map(Payment::getAmount)
                .filter(Objects::nonNull)
                .reduce(Integer::sum).orElse(0);
    }


}