package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.reservation.ReservationForm;
import org.bytesync.hotelmanagement.model.Reservation;

public class ReservationMapper {

    private ReservationMapper() {
    }

    public static Reservation toEntity(ReservationForm form) {
        return Reservation.builder()
                .noOfGuests(form.getNoOfGuests())
                .stayType(form.getStayType())
                .checkInTime(form.getCheckInTime())
                .pricePerNight(form.getPricePerNight())
                .depositAmount(form.getDepositAmount())
                .registeredStaff(form.getStaffName())
                .daysOfStay()
                .build();
    }
}
