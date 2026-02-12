package org.bytesync.hotelmanagement.dto.finance;

import lombok.Builder;
import lombok.Data;
import org.bytesync.hotelmanagement.enums.PaymentMethod;
import org.bytesync.hotelmanagement.enums.IncomeType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class PaymentDetailsDto {
    private Long id;
    private LocalDate paymentDate;
    private Map<PaymentMethod, Integer> paymentAmountMap;
    private IncomeType incomeType;
    private String notes;
    private Long reservationId;
    private String guestName;
    private Long roomNo;

    private List<VoucherDto> vouchers;
}
