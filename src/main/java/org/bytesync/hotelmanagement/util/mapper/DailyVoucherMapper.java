package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.finance.DailyVoucherDto;
import org.bytesync.hotelmanagement.model.DailyVoucher;

public class DailyVoucherMapper {

    private DailyVoucherMapper() {
    }

    public static DailyVoucherDto toDto(DailyVoucher voucher) {
        return DailyVoucherDto.builder()
                .voucherNo(voucher.getVoucherNo())
                .date(voucher.getDate())
                .reservationId(voucher.getReservation().getId())
                .guestName(voucher.getGuest().getName())
                .roomNo(voucher.getRoom().getRoomNo())
                .price(voucher.getPrice())
                .isPaid(voucher.getIsPaid())
                .build();
    }
}
