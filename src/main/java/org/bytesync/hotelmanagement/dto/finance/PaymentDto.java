package org.bytesync.hotelmanagement.dto.finance;

import lombok.Builder;
import lombok.Data;
import org.bytesync.hotelmanagement.enums.PaymentMethod;
import org.bytesync.hotelmanagement.enums.IncomeType;

import java.time.LocalDate;

@Data
@Builder
public class PaymentDto {
    private Long id;
    private LocalDate paymentDate;
    private Integer amount;
    private PaymentMethod paymentMethod;
    private IncomeType incomeType;
    private String notes;
    private Long reservationId;
    private String guestName;
    private Long roomNo;
}
