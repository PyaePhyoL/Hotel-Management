package org.bytesync.hotelmanagement.service.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.RefundDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.exception.NotEnoughMoneyException;
import org.bytesync.hotelmanagement.model.Refund;
import org.bytesync.hotelmanagement.repository.RefundRepository;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.bytesync.hotelmanagement.util.mapper.FinanceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final ReservationRepository reservationRepository;


    @Transactional
    public String createRefund(Long reservationId, RefundDto refundDto) {
        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);
        var deposit = reservation.getDeposit();
        if(deposit < refundDto.getAmount()) {
            throw new NotEnoughMoneyException("Not enough money");
        }
        var refund = FinanceMapper.toRefund(refundDto);
        refund.setReservation(reservation);
        refundRepository.save(refund);

        var leftDeposit = deposit - refund.getAmount();
        reservation.setDeposit(leftDeposit);
        reservationRepository.save(reservation);
        return "Refund successfully : " + reservation.getId();
    }


    public PageResult<RefundDto> getRefundList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.DESC, "refundDate"));
        Page<Refund> refundPage = refundRepository.findAll(pageable);
        var dtos =refundPage.getContent().stream().map(FinanceMapper::toRefundDto).toList();
        return new PageResult<>(dtos, refundPage.getTotalElements(), page, size);
    }
}
