package org.bytesync.hotelmanagement.dto.finance;

import java.time.LocalDate;

public record BalanceSheetFilterDto(
        LocalDate from,
        LocalDate to
) {
}
