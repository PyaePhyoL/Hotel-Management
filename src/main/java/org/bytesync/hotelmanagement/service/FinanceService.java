package org.bytesync.hotelmanagement.service;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.DailyVoucherDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.model.DailyVoucher;
import org.bytesync.hotelmanagement.repository.DailyVoucherRepository;
import org.bytesync.hotelmanagement.repository.specification.DailyVoucherSpecification;
import org.bytesync.hotelmanagement.util.mapper.DailyVoucherMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final DailyVoucherRepository dailyVoucherRepository;

    public PageResult<DailyVoucherDto> getDailyVouchersByReservation(long reservationId, int page, int size) {
        var pageable = PageRequest.of(page, size).withSort(Sort.Direction.ASC, "date");
        var spec = DailyVoucherSpecification.byReservationId(reservationId);

        Page<DailyVoucher> vouchers = dailyVoucherRepository.findAll(spec, pageable);
        List<DailyVoucherDto> dtos = vouchers.stream().map(DailyVoucherMapper::toDto).toList();
        return new PageResult<>(dtos, vouchers.getTotalElements(), page, size);
    }
}