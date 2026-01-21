package org.bytesync.hotelmanagement.service.interfaces.hotel;

import org.bytesync.hotelmanagement.dto.guest.ContactDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.reservation.*;
import org.bytesync.hotelmanagement.dto.room.RoomPricingRuleDto;
import org.bytesync.hotelmanagement.enums.Status;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface IReservationService {

    ReservationGuestInfo getReservationGuestInfoById(Long id);

    String create(ReservationForm form);

    PageResult<ReservationInfo> getAll(int page, int size, List<Status> statusList);

    ReservationDetails getDetailsById(Long id);

    String checkinReservation(Long reservationId);

    String checkoutReservation(Long reservationId);

    String cancelReservation(Long reservationId);

    Integer getActiveReservationCount();

    String delete(Long id);

    String extendHours(Long id, ExtraHoursDto extraHoursDto);

    String extendDays(Long id, ExtraDaysDto extraDaysDto);

    PageResult<ReservationInfo> search(String query, int page, int size, boolean status);

    String updateGuestNumber(Long id, Integer guests);

    String updateDeposit(Long id, Integer deposit);

    String updateReservation(Long id, ReservationForm form);
}
