package org.bytesync.hotelmanagement.dto.finance;

import org.bytesync.hotelmanagement.enums.IncomeType;

import java.time.LocalDate;

public record PaymentFilterDto(
        LocalDate from,
        LocalDate to,
        String query,
        IncomeType type
) {
}
