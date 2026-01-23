package org.bytesync.hotelmanagement.service.impl.guest;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.guest.GuestRecordDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.model.GuestRecord;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.repository.GuestRecordRepository;
import org.bytesync.hotelmanagement.specification.GuestRecordSpecification;
import org.bytesync.hotelmanagement.service.interfaces.guest.IGuestRecordService;
import org.bytesync.hotelmanagement.util.mapper.GuestRecordMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class GuestRecordService implements IGuestRecordService {

    private final GuestRecordRepository guestRecordRepository;

    @Override
    @Transactional
    public void createGuestRecord(Reservation reservation) {
        GuestRecord guestRecord = GuestRecord.builder()
                .guest(reservation.getGuest())
                .room(reservation.getRoom())
                .checkInTime(reservation.getCheckInDateTime())
                .daysOfStay(reservation.getDaysOfStay())
                .current(true)
                .reservation(reservation)
                .build();
        guestRecordRepository.save(guestRecord);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateGuestRecordWhenCheckout(Long reservationId, LocalDateTime checkoutTime) {
        var guestRecord = safeCall(guestRecordRepository.findByReservationId(reservationId), "Guest Record's guest", reservationId);

        int daysOfStay = (int) ChronoUnit.DAYS.between(guestRecord.getCheckInTime(), checkoutTime);
        guestRecord.setCheckOutTime(checkoutTime);
        guestRecord.setDaysOfStay(daysOfStay);
        guestRecord.setCurrent(false);
        guestRecordRepository.save(guestRecord);
    }

    @Override
    public PageResult<GuestRecordDto> getAll(boolean isCurrent, int page, int size) {
        var pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC, "checkInTime");
        var spec = GuestRecordSpecification.currentOrAll(isCurrent);
        Page<GuestRecord> records = guestRecordRepository.findAll(spec, pageable);
        List<GuestRecordDto> dtos = records.stream().map(GuestRecordMapper::toDto).toList();
        return new PageResult<>(dtos, records.getTotalElements(), page, size);
    }

    @Override
    public PageResult<GuestRecordDto> search(String query, int page, int size, boolean isCurrent) {
        var pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC, "checkInTime");
        var spec = GuestRecordSpecification.search(query, isCurrent);
        Page<GuestRecord> records = guestRecordRepository.findAll(spec, pageable);
        List<GuestRecordDto> dtos = records.stream().map(GuestRecordMapper::toDto).toList();
        return new PageResult<>(dtos, records.getTotalElements(), page, size);
    }
}
