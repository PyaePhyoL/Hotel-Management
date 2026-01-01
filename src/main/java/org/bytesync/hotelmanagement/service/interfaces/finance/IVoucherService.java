package org.bytesync.hotelmanagement.service.interfaces.finance;

import org.bytesync.hotelmanagement.dto.finance.VoucherCreatForm;
import org.bytesync.hotelmanagement.dto.finance.VoucherDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.Voucher;

import java.util.List;

public interface IVoucherService {

    void createVoucherFromReservation(Reservation reservation);

    void createAdditionalVoucher(VoucherCreatForm form);

    PageResult<VoucherDto> getVoucherListByReservation(Long reservationId, boolean isPaid, int page, int size);

    List<VoucherDto> getSelectedVoucherDtos(List<Long> voucherIds);

    List<Voucher> getVouchers(List<Long>  voucherIds);

    String updateVoucher(Long id, VoucherDto voucherDto);

    VoucherDto getVoucherDetails(Long id);
}
