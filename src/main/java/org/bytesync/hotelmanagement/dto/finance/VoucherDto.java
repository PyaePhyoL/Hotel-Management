package org.bytesync.hotelmanagement.dto.finance;

import lombok.Builder;
import org.bytesync.hotelmanagement.enums.VoucherType;

import java.time.LocalDate;

@Builder
public record VoucherDto(
    Long voucherNo,
    VoucherType type,
    Long paymentId,
    LocalDate date,
    Long reservationId,
    String guestName,
    Long roomNo,
    Integer price,
    Boolean isPaid
) {

}
