package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.reservation.ReservationDetails;
import org.bytesync.hotelmanagement.dto.reservation.ReservationForm;
import org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo;
import org.bytesync.hotelmanagement.dto.reservation.ReservationInfo;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.scheduling.ScheduleMethods;

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
                .status(ScheduleMethods.checkDateTimeAndGetStatus(form.getCheckInTime(), null))
                .daysOfStay(0)
                .dailyVouchers(new ArrayList<>())
                .paymentList(new ArrayList<>())
                .notes(form.getNote())
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

    public static ReservationGuestInfo toReservationGuestInfo(Reservation reservation) {
        var guest = reservation.getGuest();
        return ReservationGuestInfo.builder()
                .reservationId(reservation.getId())
                .checkInTime(reservation.getCheckInTime())
                .guestId(guest.getId())
                .guestName(guest.getName())
                .guestPhoneList(guest.getPhoneList())
                .noOfGuests(reservation.getNoOfGuests())
                .daysOfStay(reservation.getDaysOfStay())
                .build();
    }
}
