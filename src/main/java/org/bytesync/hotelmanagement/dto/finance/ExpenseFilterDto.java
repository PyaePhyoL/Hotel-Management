package org.bytesync.hotelmanagement.dto.finance;

import org.bytesync.hotelmanagement.enums.ExpenseType;

import java.time.LocalDate;

public record ExpenseFilterDto(
        LocalDate from,
        LocalDate to,
        String query,
        ExpenseType type
) {
}
