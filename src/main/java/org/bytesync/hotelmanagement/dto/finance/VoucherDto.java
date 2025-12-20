package org.bytesync.hotelmanagement.dto.finance;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record VoucherDto(
    String voucherNo,
    String paymentId,
    LocalDate date,
    Long reservationId,
    String guestName,
    Integer roomNo,
    Integer price,
    Boolean isPaid
) {

}
