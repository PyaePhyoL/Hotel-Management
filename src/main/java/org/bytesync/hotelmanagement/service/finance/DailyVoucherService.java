package org.bytesync.hotelmanagement.service.finance;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.DailyVoucherDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.model.Voucher;
import org.bytesync.hotelmanagement.repository.DailyVoucherRepository;
import org.bytesync.hotelmanagement.repository.specification.DailyVoucherSpecification;
import org.bytesync.hotelmanagement.util.mapper.DailyVoucherMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyVoucherService {

    private final DailyVoucherRepository dailyVoucherRepository;

    public PageResult<DailyVoucherDto> getDailyVouchersByReservation(long reservationId, boolean isPaid, int page, int size) {
        var pageable = PageRequest.of(page, size).withSort(Sort.Direction.ASC, "date");
        var spec = DailyVoucherSpecification.byReservationId(reservationId, isPaid);

        Page<Voucher> vouchers = dailyVoucherRepository.findAll(spec, pageable);
        List<DailyVoucherDto> dtos = vouchers.stream()
                .map(DailyVoucherMapper::toDto).toList();
        return new PageResult<>(dtos, vouchers.getTotalElements(), page, size);
    }

    public List<DailyVoucherDto> getSelectedVouchers(List<String> voucherIds) {
        return getDailyVouchers(voucherIds).stream()
                .map(DailyVoucherMapper::toDto)
                .toList();
    }

    public List<Voucher> getDailyVouchers(List<String>  voucherIds) {
        List<Voucher> vouchers = new ArrayList<>();
        voucherIds.forEach(id -> {
            dailyVoucherRepository.findById(id).ifPresent(vouchers::add);
        });
        return vouchers;
    }
}
