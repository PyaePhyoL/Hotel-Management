package org.bytesync.hotelmanagement.dto.finance;

import org.bytesync.hotelmanagement.enums.VoucherType;

public record VoucherCreatForm(
        Long reservationId,
        VoucherType type,
        Integer price,
        String notes
) {
}
