package org.bytesync.hotelmanagement.scheduling;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytesync.hotelmanagement.model.DailyVoucher;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.enums.Status;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.bytesync.hotelmanagement.model.enums.PaymentMethod.DEPOSIT;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleMethods {

    private final ReservationRepository reservationRepository;

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void scheduleEveryDayCreatingVouchers() {

        log.info(" =============> Creating Voucher Schedule Task <=============");

        var date = LocalDate.now();
        var reservations = reservationRepository.findAllActiveReservations();

        reservations.forEach(reservation -> {
            createDailyVoucher(reservation, date);
            reservation.incrementDaysOfStayByOne();
        });

        reservationRepository.saveAll(reservations);
    }


    @Scheduled(cron = "0 * * * * *")
    public void checkBookingReservationAndChangeStatus() {

        var reservations = reservationRepository.findAllBookingReservations();

        reservations.forEach(reservation -> {
            var status = checkDateTimeAndGetStatus(
                    reservation.getCheckInTime(),
                    reservation.getCheckOutTime());
            reservation.setStatus(status);
        });

        reservationRepository.saveAll(reservations);
    }

    public static void createDailyVoucher(Reservation reservation, LocalDate date) {
        var dailyVoucher = DailyVoucher.builder()
                .reservation(reservation)
                .date(date)
                .price(reservation.getPricePerNight())
                .guestName(reservation.getGuest().getName())
                .roomNo(reservation.getRoom().getRoomNo())
                .build();
        dailyVoucher.setIsPaid(processDailyPayment(reservation, dailyVoucher));
        reservation.addDailyVoucher(dailyVoucher);
    }

    private static boolean processDailyPayment(Reservation reservation, DailyVoucher voucher) {
        var deposit = reservation.getDepositAmount();
        var pricePerNight = reservation.getPricePerNight();
        var paid = false;
        if(deposit >= pricePerNight) {
            deposit = deposit - pricePerNight;
            paid = true;
            reservation.setDepositAmount(deposit);
            createPayment(reservation, voucher);
        }
        return paid;
    }

    private static void createPayment(Reservation reservation, DailyVoucher voucher) {
        var payment = Payment.builder()
                .paymentDate(LocalDate.now())
                .amount(reservation.getPricePerNight())
                .paymentMethod(DEPOSIT)
                .notes("Automatic paid from deposit")
                .dailyVouchers(new ArrayList<>())
                .build();
        payment.setReservation(reservation);
        payment.addDailyVoucher(voucher);
    }

    public static Status checkDateTimeAndGetStatus(LocalDateTime checkIn, LocalDateTime checkOut) {
        var now = LocalDateTime.now();

        if(now.isBefore(checkIn)) {
            return Status.BOOKING;
        } else if(now.isAfter(checkIn) || now.isEqual(checkIn)) {
            return Status.ACTIVE;
        } else if(null != checkOut && now.isAfter(checkOut)) {
            return Status.PAST;
        } else {
            return null;
        }
    }
}
