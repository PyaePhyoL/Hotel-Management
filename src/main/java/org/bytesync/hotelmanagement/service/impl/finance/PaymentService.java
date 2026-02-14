package org.bytesync.hotelmanagement.service.impl.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.*;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.enums.DepositType;
import org.bytesync.hotelmanagement.enums.IncomeType;
import org.bytesync.hotelmanagement.enums.PaymentMethod;
import org.bytesync.hotelmanagement.exception.NotEnoughMoneyException;
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
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

import static org.bytesync.hotelmanagement.enums.IncomeType.ROOM_RENT;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.convertInstantToYangonZoneLocalDateTime;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final VoucherService voucherService;
    private static final LocalTime MORNING_START = LocalTime.of(6, 0);   // 06:00
    private static final LocalTime NIGHT_START = LocalTime.of(18, 0);  // 18:00

    @Override
    public String createPayment(PaymentCreateForm form) {
        var reservation = safeCall(reservationRepository.findById(form.getReservationId()),
                "Reservation", form.getReservationId());

        var payment = new Payment();

        payment.setDate(form.getPaymentDate());
        payment.setReservation(reservation);
        payment.setGuest(reservation.getGuest());
        payment.setNotes(form.getNotes());
        payment.setType(form.getIncomeType());

        form.getPaymentAmountMap().forEach((paymentMethod, amount) -> {
            switch (paymentMethod) {
                case CASH, KPAY -> {
                    payment.addPaymentAmount(paymentMethod, amount);
                }
                case DEPOSIT -> {
                    if(reservation.getDeposit() >= amount) {
                        var leftDeposit = reservation.getDeposit() - amount;
                        var method = DepositType.convertToPaymentMethod(reservation.getDepositType());
                        reservation.setDeposit(leftDeposit);
                        payment.addPaymentAmount(method, amount);
                    } else {
                        throw new NotEnoughMoneyException("Not enough money");
                    }
                }
                case EXPEDIA -> {
                    payment.addPaymentAmount(paymentMethod, 0);
                }
                case null, default -> {
                    throw new IllegalArgumentException("Illegal payment method");
                }
            }
        });

        updatePaidVouchers(form, payment);

        var id = paymentRepository.save(payment).getId();
        return "Payment created successfully : " + id;
    }

    private void updatePaidVouchers(PaymentCreateForm paymentCreateForm, Payment payment) {
        var vouchers = voucherService.getVouchers(paymentCreateForm.getVoucherIds());

        validateVoucherTypesConsistency(vouchers, paymentCreateForm.getIncomeType());

        vouchers.forEach(voucher -> {
            voucher.setIsPaid(true);
            payment.addVoucher(voucher);
        });
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
    public PageResult<PaymentListDto> getPaymentList(int page, int size, FinanceFilterDto filterDto) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Specification<Payment> spec = FinanceSpecification.financeFilter(filterDto);
        Page<Payment> all = paymentRepository.findAll(spec, pageable);
        var dtos = all.getContent().stream().map(FinanceMapper::toPaymentListDto).toList();
        return new  PageResult<>(dtos, all.getTotalElements(), page, size);
    }

    @Override
    public Integer getDailyIncomeAmount() {
        var today = LocalDate.now();
        var payments =paymentRepository.findByDate(today);

        return payments.stream()
                .filter(payment -> payment.getType().equals(ROOM_RENT))
                .map(Payment::getPaidAmount)
                .reduce(Integer::sum).orElse(0);
    }

    @Override
    public String updateExpediaAmount(Long id, UpdateExpediaAmountDto amountDto) {
        var payment = safeCall(paymentRepository.findById(id), "Payment", id);

        payment.getPaymentAmountMap().put(PaymentMethod.EXPEDIA, amountDto.amount());
        payment.setNotes(amountDto.notes());
        paymentRepository.save(payment);
        return "Payment updated successfully : " + id;
    }

    @Override
    public PageResult<PaymentListDto> getPaymentListByReservation(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Specification<Payment> spec = FinanceSpecification.paymentFilterByReservation(id);
        Page<Payment> paymentPage = paymentRepository.findAll(spec, pageable);

        List<PaymentListDto> dtoList = paymentPage.getContent().stream().map(FinanceMapper::toPaymentListDto).toList();
        return new PageResult<>(dtoList, paymentPage.getTotalElements(), page, size);
    }

    @Override
    public PaymentDetailsDto getPaymentDetailsById(Long id) {
        var payment = safeCall(paymentRepository.findById(id), "Payment", id);
        return FinanceMapper.toPaymentDetailsDto(payment);
    }

    @Override
    public Integer getDailyIncomeAmountByPaymentMethod(PaymentMethod paymentMethod) {
        LocalDate today = LocalDate.now();
        var payments = paymentRepository.findByDateAndIncomeType(today, ROOM_RENT);
        return payments.stream()
                .map(payment -> payment.getAmountByPaymentMethod(paymentMethod))
                .reduce(Integer::sum).orElse(0);
    }

    @Override
    public DashboardIncomeDto getDashboardIncomeDto() {
        var today = LocalDate.now();
        var payments = paymentRepository.findByDateAndIncomeType(today, ROOM_RENT);

        var dailyIncome = payments.stream()
                .map(Payment::getPaidAmount)
                .reduce(Integer::sum).orElse(0);

        var dayShiftPayments = payments.stream().filter(dayShiftFilter()).toList();
        var nightShiftPayments = payments.stream().filter(nightShiftFilter()).toList();

        var dayShiftIncome = dayShiftPayments.stream().map(Payment::getPaidAmount).reduce(Integer::sum).orElse(0);
        var dayShiftKpay = dayShiftPayments.stream().map(payment -> payment.getAmountByPaymentMethod(PaymentMethod.KPAY))
                .reduce(Integer::sum).orElse(0);
        var dayShiftCash = dayShiftPayments.stream().map(payment -> payment.getAmountByPaymentMethod(PaymentMethod.CASH))
                .reduce(Integer::sum).orElse(0);

        var nightShiftIncome = nightShiftPayments.stream().map(Payment::getPaidAmount).reduce(Integer::sum).orElse(0);
        var nightShiftKpay = nightShiftPayments.stream().map(payment -> payment.getAmountByPaymentMethod(PaymentMethod.KPAY))
                .reduce(Integer::sum).orElse(0);
        var nightShiftCash = nightShiftPayments.stream().map(payment -> payment.getAmountByPaymentMethod(PaymentMethod.CASH))
                .reduce(Integer::sum).orElse(0);

        return DashboardIncomeDto.builder()
                .dailyIncome(dailyIncome)
                .dayShiftIncome(dayShiftIncome)
                .nightShiftIncome(nightShiftIncome)
                .dayShiftKpay(dayShiftKpay)
                .dayShiftCash(dayShiftCash)
                .nightShiftKpay(nightShiftKpay)
                .nightShiftCash(nightShiftCash)
                .build();
    }

    private Predicate<Payment> dayShiftFilter() {
        return payment -> {
            var localTime = convertInstantToYangonZoneLocalDateTime(payment.getCreatedAt()).toLocalTime();
            return !localTime.isBefore(MORNING_START)
                    && localTime.isBefore(NIGHT_START);
        };
    }

    private Predicate<Payment> nightShiftFilter() {
        return payment -> {
            var localTime = convertInstantToYangonZoneLocalDateTime(payment.getCreatedAt()).toLocalTime();
            return localTime.isBefore(MORNING_START)
                    || !localTime.isBefore(NIGHT_START);
        };
    }


}