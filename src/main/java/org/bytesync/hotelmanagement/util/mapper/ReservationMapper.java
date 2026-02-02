package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.reservation.ReservationDetails;
import org.bytesync.hotelmanagement.dto.reservation.ReservationForm;
import org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo;
import org.bytesync.hotelmanagement.dto.reservation.ReservationInfo;
import org.bytesync.hotelmanagement.enums.Status;
import org.bytesync.hotelmanagement.model.Reservation;

import java.time.LocalDateTime;
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
                .depositType(form.getDepositType())
                .deposit(form.getDeposit())
                .discount(form.getDiscount())
                .status(Status.BOOKING)
                .daysOfStay(days)
                .voucherList(new ArrayList<>())
                .paymentList(new ArrayList<>())
                .contactList(new ArrayList<>())
                .refundList(new ArrayList<>())
                .notes(form.getNote())
                .build();
    }

    public static void updateReservation(Reservation reservation, ReservationForm form) {

        if(!reservation.getCheckInDateTime().equals(form.getCheckInDateTime())) {
            updateCheckInDateTimeOnlyReservationIsNotStarted(reservation, form.getCheckInDateTime());
        }

        var days = (form.getCheckOutDateTime() != null)
                ? getDaysBetween(
                form.getCheckInDateTime().toLocalDate(),
                form.getCheckOutDateTime().toLocalDate())
                : 0;
        reservation.setCheckOutDateTime(form.getCheckOutDateTime());
        reservation.setDaysOfStay(days);
        reservation.setDeposit(form.getDeposit());
        reservation.setDiscount(form.getDiscount());
        reservation.setNoOfGuests(form.getNoOfGuests());
        reservation.setNotes(form.getNote());
    }

    private static void updateCheckInDateTimeOnlyReservationIsNotStarted(Reservation reservation, LocalDateTime newCheckInDateTime) {
        if(reservation.getStatus() == Status.BOOKING) {
            var isCheckInDateTimeChanged = !reservation.getCheckInDateTime().equals(newCheckInDateTime);
            if(isCheckInDateTimeChanged) {
                reservation.setCheckInDateTime(newCheckInDateTime);
            }
        } else {
            throw new IllegalStateException("Cannot change the check in time after reservation is started");
        }
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
                .guestPhone(reservation.getGuest().getPhoneNumber())
                .roomNo(reservation.getRoom().getRoomNo())
                .build();
    }

    public static ReservationDetails toReservationDetails(Reservation reservation) {
        return ReservationDetails.builder()
                .id(reservation.getId())
                .checkInDateTime(reservation.getCheckInDateTime())
                .checkOutDateTime(reservation.getCheckOutDateTime())
                .daysOfStay(reservation.getDaysOfStay())
                .price(reservation.getPrice())
                .depositType(reservation.getDepositType())
                .deposit(reservation.getDeposit())
                .discount(reservation.getDiscount())
                .stayType(reservation.getStayType())
                .status(reservation.getStatus())
                .noOfGuests(reservation.getNoOfGuests())
                .notes(reservation.getNotes())
                .registerStaff(reservation.getRegisterStaff())
                .checkInStaff(reservation.getCheckInStaff())
                .checkOutStaff(reservation.getCheckOutStaff())
                .build();
    }

    public static ReservationGuestInfo toReservationGuestInfo(Reservation reservation) {
        var guest = reservation.getGuest();
        return ReservationGuestInfo.builder()
                .reservationId(reservation.getId())
                .checkInTime(reservation.getCheckInDateTime())
                .guestId(guest.getId())
                .guestName(guest.getName())
                .phone(guest.getPhoneNumber())
                .noOfGuests(reservation.getNoOfGuests())
                .daysOfStay(reservation.getDaysOfStay())
                .build();
    }


}
