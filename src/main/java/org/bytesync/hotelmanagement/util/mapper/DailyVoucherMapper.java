package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.finance.DailyVoucherDto;
import org.bytesync.hotelmanagement.model.Voucher;

public class DailyVoucherMapper {

    private DailyVoucherMapper() {
    }

    public static DailyVoucherDto toDto(Voucher voucher) {
        var payment = voucher.getPayment();
        return DailyVoucherDto.builder()
                .voucherNo(voucher.getVoucherNo())
                .paymentId(null == payment ? null : payment.getId())
                .date(voucher.getDate())
                .reservationId(voucher.getReservation().getId())
                .guestName(voucher.getGuestName())
                .roomNo(voucher.getRoomNo())
                .price(voucher.getPrice())
                .isPaid(voucher.getIsPaid())
                .build();
    }
}
