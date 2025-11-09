package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.reservation.ReservationDetails;
import org.bytesync.hotelmanagement.dto.reservation.ReservationForm;
import org.bytesync.hotelmanagement.dto.reservation.ReservationInfo;
import org.bytesync.hotelmanagement.model.Reservation;

import java.util.ArrayList;

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
                .isActive(true)
                .daysOfStay(0)
                .dailyVouchers(new ArrayList<>())
                .paymentList(new ArrayList<>())
                .build();
    }

    public static ReservationInfo toReservationInfo(Reservation reservation) {
        return ReservationInfo.builder()
                .id(reservation.getId())
                .checkInTime(reservation.getCheckInTime())
                .checkOutTime(reservation.getCheckOutTime())
                .daysOfStay(reservation.getDaysOfStay())
                .guestName(reservation.getGuest().getName())
                .roomNo(reservation.getRoom().getRoomNo())
                .build();
    }

    public static ReservationDetails toReservationDetails(Reservation reservation) {
        return ReservationDetails.builder()
                .id(reservation.getId())
                .checkInTime(reservation.getCheckInTime())
                .checkOutTime(reservation.getCheckOutTime())
                .daysOfStay(reservation.getDaysOfStay())
                .pricePerNight(reservation.getPricePerNight())
                .depositAmount(reservation.getDepositAmount())
                .stayType(reservation.getStayType())
                .registeredStaff(reservation.getRegisteredStaff())
                .noOfGuests(reservation.getNoOfGuests())
                .build();
    }
}
