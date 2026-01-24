package org.bytesync.hotelmanagement.service.interfaces.finance;

import org.bytesync.hotelmanagement.dto.finance.RefundDto;
import org.bytesync.hotelmanagement.dto.finance.FinanceFilterDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;

public interface IRefundService {

    String createDepositRefund(Long reservationId, RefundDto refundDto);

    String createPaymentRefund(Long paymentId, RefundDto refundDto);

    PageResult<RefundDto> getRefundList(int page, int size, FinanceFilterDto filterDto);
}
