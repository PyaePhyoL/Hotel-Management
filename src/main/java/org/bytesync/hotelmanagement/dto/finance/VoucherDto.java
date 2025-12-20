package org.bytesync.hotelmanagement.dto.finance;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record VoucherDto(
    Long voucherNo,
    Long paymentId,
    LocalDate date,
    Long reservationId,
    String guestName,
    Long roomNo,
    Integer price,
    Boolean isPaid
) {

}
