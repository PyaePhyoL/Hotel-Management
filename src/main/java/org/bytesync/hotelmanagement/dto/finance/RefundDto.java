package org.bytesync.hotelmanagement.dto.finance;

import lombok.Builder;
import lombok.Data;
import org.bytesync.hotelmanagement.enums.RefundType;

import java.time.LocalDate;

@Data
@Builder
public class RefundDto {
    private Long id;
    private LocalDate refundDate;
    private RefundType type;
    private String guestName;
    private Integer amount;
    private String notes;
}
