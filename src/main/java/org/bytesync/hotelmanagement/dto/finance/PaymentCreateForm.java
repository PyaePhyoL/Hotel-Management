package org.bytesync.hotelmanagement.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bytesync.hotelmanagement.enums.PaymentMethod;
import org.bytesync.hotelmanagement.enums.IncomeType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateForm {
    private LocalDate paymentDate;
    private Map<PaymentMethod, Integer> paymentAmountMap;
    private IncomeType incomeType;
    private String notes;
    private Long reservationId;
    private List<Long> voucherIds = new ArrayList<>();
}
