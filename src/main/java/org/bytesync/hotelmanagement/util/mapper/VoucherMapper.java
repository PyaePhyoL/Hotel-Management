package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.finance.VoucherDto;
import org.bytesync.hotelmanagement.model.Voucher;

public class VoucherMapper {

    private VoucherMapper() {
    }

    public static VoucherDto toDto(Voucher voucher) {
        var payment = voucher.getPayment();
        return VoucherDto.builder()
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
