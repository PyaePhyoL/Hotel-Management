package org.bytesync.hotelmanagement.dto.finance;

import org.bytesync.hotelmanagement.enums.IncomeType;

public record VoucherCreatForm(
        Long reservationId,
        IncomeType type,
        Integer price,
        String notes
) {
}
