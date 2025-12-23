package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.reservation.ReservationDetails;
import org.bytesync.hotelmanagement.dto.reservation.ReservationForm;
import org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo;
import org.bytesync.hotelmanagement.dto.reservation.ReservationInfo;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.scheduling.ScheduleMethods;
import org.bytesync.hotelmanagement.util.EntityOperationUtils;

import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.getDaysBetween;

public class ReservationMapper {

    private ReservationMapper() {
    }

    public static Reservation toEntity(ReservationForm form) {

        var days = (form.getCheckOutDateTime() != null)
                    ? getDaysBetween(
                                form.getCheckInDateTime().toLocalDate(),
                                form.getCheckOutDateTime().toLocalDate())
                    : 0;


        return Reservation.builder()
                .noOfGuests(form.getNoOfGuests())
                .stayType(form.getStayType())
                .checkInDateTime(form.getCheckInDateTime())
                .checkOutDateTime(form.getCheckOutDateTime())
                .price(form.getPrice())
                .deposit(form.getDeposit())
                .discount(form.getDiscount())
                .status(ScheduleMethods.checkDateTimeAndGetStatus(form.getCheckInDateTime(), null))
                .daysOfStay(days)
                .voucherList(new ArrayList<>())
                .paymentList(new ArrayList<>())
                .contactList(new ArrayList<>())
                .refundList(new ArrayList<>())
                .notes(form.getNote())
                .build();
    }

    public static ReservationInfo toReservationInfo(Reservation reservation) {
        return ReservationInfo.builder()
                .id(reservation.getId())
                .checkInTime(reservation.getCheckInDateTime())
                .checkOutTime(reservation.getCheckOutDateTime())
                .stayType(reservation.getStayType())
                .status(reservation.getStatus())
                .daysOfStay(reservation.getDaysOfStay())
                .guestName(reservation.getGuest().getName())
                .guestPhone(reservation.getGuest().getPhoneList().stream().findFirst().orElse("Unknown number"))
                .roomNo(reservation.getRoom().getRoomNo())
                .build();
    }

    public static ReservationDetails toReservationDetails(Reservation reservation) {
        return ReservationDetails.builder()
                .id(reservation.getId())
                .checkInTime(reservation.getCheckInDateTime())
                .checkOutTime(reservation.getCheckOutDateTime())
                .daysOfStay(reservation.getDaysOfStay())
                .price(reservation.getPrice())
                .deposit(reservation.getDeposit())
                .discount(reservation.getDiscount())
                .stayType(reservation.getStayType())
                .status(reservation.getStatus())
                .registeredStaff(reservation.getCreatedBy())
                .noOfGuests(reservation.getNoOfGuests())
                .notes(reservation.getNotes())
                .build();
    }

    public static ReservationGuestInfo toReservationGuestInfo(Reservation reservation) {
        var guest = reservation.getGuest();
        return ReservationGuestInfo.builder()
                .reservationId(reservation.getId())
                .checkInTime(reservation.getCheckInDateTime())
                .guestId(guest.getId())
                .guestName(guest.getName())
                .guestPhoneList(guest.getPhoneList())
                .noOfGuests(reservation.getNoOfGuests())
                .daysOfStay(reservation.getDaysOfStay())
                .build();
    }

    public static void updateReservation(Reservation reservation, ReservationForm form) {
        reservation.setPrice(form.getPrice());
        reservation.setDeposit(form.getDeposit());
        reservation.setDiscount(form.getDiscount());
        reservation.setStayType(form.getStayType());
        reservation.setNoOfGuests(form.getNoOfGuests());
        reservation.setNotes(form.getNote());
    }
}
