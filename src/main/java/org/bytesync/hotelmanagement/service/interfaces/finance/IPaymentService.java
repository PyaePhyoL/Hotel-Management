package org.bytesync.hotelmanagement.service.interfaces.finance;

import org.bytesync.hotelmanagement.dto.finance.PaymentCreateForm;
import org.bytesync.hotelmanagement.dto.finance.PaymentDetailsDto;
import org.bytesync.hotelmanagement.dto.finance.PaymentDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;

public interface IPaymentService {

    String createPayment(PaymentCreateForm paymentCreateForm);

    PageResult<PaymentDto> getPaymentList(int page, int size);

    Integer getDailyIncomeAmount();

    String updateExpenditureAmount(Long id, PaymentDto paymentDto);

    PageResult<PaymentDto> getPaymentListByReservation(Long id, int page, int size);

    PaymentDetailsDto getPaymentDetailsById(Long id);
}
