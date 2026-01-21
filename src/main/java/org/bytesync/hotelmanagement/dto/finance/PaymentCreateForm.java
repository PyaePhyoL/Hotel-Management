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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateForm {
    private LocalDate paymentDate;
    private Integer amount;
    private PaymentMethod paymentMethod;
    private IncomeType incomeType;
    private String notes;
    private Long reservationId;
    private List<Long> voucherIds = new ArrayList<>();
}
