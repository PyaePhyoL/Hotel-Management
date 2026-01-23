package org.bytesync.hotelmanagement.service.impl.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.RefundDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.enums.RefundType;
import org.bytesync.hotelmanagement.exception.NotEnoughMoneyException;
import org.bytesync.hotelmanagement.model.Refund;
import org.bytesync.hotelmanagement.repository.PaymentRepository;
import org.bytesync.hotelmanagement.repository.RefundRepository;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.bytesync.hotelmanagement.service.interfaces.finance.IRefundService;
import org.bytesync.hotelmanagement.specification.FinanceSpecification;
import org.bytesync.hotelmanagement.util.mapper.FinanceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.bytesync.hotelmanagement.enums.RefundType.PAYMENT_REFUND;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class RefundService implements IRefundService {

    private final RefundRepository refundRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;


    @Transactional
    @Override
    public String createDepositRefund(Long reservationId, RefundDto refundDto) {
        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);
        var deposit = reservation.getDeposit();
        if(deposit < refundDto.getAmount()) {
            throw new NotEnoughMoneyException("Not enough money");
        }
        var refund = FinanceMapper.toRefund(refundDto, RefundType.DEPOSIT_REFUND);
        refund.setReservation(reservation);
        var refundId = refundRepository.save(refund).getId();

        var leftDeposit = deposit - refund.getAmount();
        reservation.setDeposit(leftDeposit);
        reservationRepository.save(reservation);
        return "Refund successfully! Refund Id : " + refundId;
    }

    @Transactional
    @Override
    public String createPaymentRefund(Long paymentId, RefundDto refundDto) {
        var payment = safeCall(paymentRepository.findById(paymentId), "Payment", paymentId);
        var refundAmount = refundDto.getAmount();
        if(payment.getAmount() < refundAmount) {
            throw new NotEnoughMoneyException("Not enough money");
        }

        var leftAmount= payment.getAmount() - refundAmount;

        payment.setAmount(leftAmount);
        var refund = FinanceMapper.toRefund(refundDto, PAYMENT_REFUND);
        refund.setReservation(payment.getReservation());

        paymentRepository.save(payment);
        var refundId = refundRepository.save(refund).getId();

        return "Refund successfully! Refund Id : " + refundId;
    }

    @Override
    public PageResult<RefundDto> getRefundList(int page, int size, LocalDate from, LocalDate to) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.DESC, "refundDate"));
        Specification<Refund> spec = FinanceSpecification.refundFilterByDate(from, to, PAYMENT_REFUND);
        Page<Refund> refundPage = refundRepository.findAll(spec, pageable);
        var dtos =refundPage.getContent().stream().map(FinanceMapper::toRefundDto).toList();
        return new PageResult<>(dtos, refundPage.getTotalElements(), page, size);
    }
}
