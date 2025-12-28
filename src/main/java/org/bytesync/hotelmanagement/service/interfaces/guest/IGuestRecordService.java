package org.bytesync.hotelmanagement.service.interfaces.guest;

import org.bytesync.hotelmanagement.dto.guest.GuestRecordDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.model.Reservation;

import java.time.LocalDateTime;

public interface IGuestRecordService {

    void createGuestRecord(Reservation reservation);

    void updateGuestRecordWhenCheckout(Long reservationId, LocalDateTime checkoutTime);

    PageResult<GuestRecordDto> getAll(boolean isCurrent, int page, int size);

    PageResult<GuestRecordDto> search(String query, int page, int size);
}
