package org.bytesync.hotelmanagement.dto.finance;

import lombok.Builder;
import lombok.Data;
import org.bytesync.hotelmanagement.model.enums.PaymentMethod;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PaymentCreateForm {
    private LocalDate paymentDate;
    private Integer amount;
    private PaymentMethod paymentMethod;
    private String notes;
    private Long reservationId;
    private List<String> voucherIds;
}
