package org.bytesync.hotelmanagement.dto.finance;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RefundDto {
    private LocalDate refundDate;
}
