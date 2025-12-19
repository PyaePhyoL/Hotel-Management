package org.bytesync.hotelmanagement.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bytesync.hotelmanagement.model.enums.PaymentMethod;

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
    private String notes;
    private Long reservationId;
    private List<String> voucherIds = new ArrayList<>();
}
