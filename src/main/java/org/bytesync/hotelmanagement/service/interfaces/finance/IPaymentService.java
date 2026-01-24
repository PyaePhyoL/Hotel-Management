package org.bytesync.hotelmanagement.service.interfaces.finance;

import org.bytesync.hotelmanagement.dto.finance.*;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.enums.PaymentMethod;

public interface IPaymentService {

    String createPayment(PaymentCreateForm paymentCreateForm);

    PageResult<PaymentDto> getPaymentList(int page, int size, FinanceFilterDto filterDto);

    Integer getDailyIncomeAmount();

    String updateExpenditureAmount(Long id, PaymentDto paymentDto);

    PageResult<PaymentDto> getPaymentListByReservation(Long id, int page, int size);

    PaymentDetailsDto getPaymentDetailsById(Long id);

    Integer getDailyIncomeAmountByPaymentMethod(PaymentMethod type);

}
