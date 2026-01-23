package org.bytesync.hotelmanagement.service.interfaces.finance;

import org.bytesync.hotelmanagement.dto.finance.RefundDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;

import java.time.LocalDate;

public interface IRefundService {

    String createDepositRefund(Long reservationId, RefundDto refundDto);

    String createPaymentRefund(Long paymentId, RefundDto refundDto);

    PageResult<RefundDto> getRefundList(int page, int size, LocalDate from, LocalDate to);
}
