package org.bytesync.hotelmanagement.dto.finance;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record DailyVoucherDto (
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
