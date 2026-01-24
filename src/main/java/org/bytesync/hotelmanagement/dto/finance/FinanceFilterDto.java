package org.bytesync.hotelmanagement.dto.finance;

import java.time.LocalDate;

public record FinanceFilterDto(
        LocalDate from,
        LocalDate to,
        String query,
        String type
) {
}
