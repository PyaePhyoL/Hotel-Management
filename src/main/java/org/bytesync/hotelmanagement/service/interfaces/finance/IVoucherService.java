package org.bytesync.hotelmanagement.service.interfaces.finance;

import org.bytesync.hotelmanagement.dto.finance.VoucherDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.enums.VoucherType;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.Voucher;

import java.util.List;

public interface IVoucherService {

    void createVoucher(Reservation reservation);

    PageResult<VoucherDto> getVoucherListByReservation(Long reservationId, boolean isPaid, int page, int size);

    List<VoucherDto> getSelectedVoucherDtos(List<Long> voucherIds);

    List<Voucher> getVouchers(List<Long>  voucherIds);

    void createAdditionalVoucher(Reservation reservation, int price, VoucherType type);
}
