package org.bytesync.hotelmanagement.service.interfaces.hotel;

import org.bytesync.hotelmanagement.dto.guest.ContactDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.reservation.*;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface IReservationService {

    ReservationGuestInfo getReservationGuestInfoById(Long id);

    String create(ReservationForm form);

    PageResult<ReservationInfo> getAll(int page, int size, List<Status> statusList);

    ReservationDetails getDetailsById(Long id);

    String checkoutReservation(Long reservationId, LocalDateTime checkoutTime);

    String cancelReservation(Long reservationId);

    String changeRoom(Long id, Long roomId);

    Integer getDailyCheckIns();

    String update(Long id, ReservationForm form);

    void updateContacts(Reservation reservation, List<ContactDto> contactDtos);

    String delete(Long id);

    String extendHours(Long id, ExtraHoursDto extraHoursDto);

    String extendDays(Long id, Integer days);

    PageResult<ReservationInfo> search(String query, int page, int size);
}
