package org.bytesync.hotelmanagement.scheduling;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytesync.hotelmanagement.model.DailyVoucher;
import org.bytesync.hotelmanagement.model.Payment;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.enums.PaymentMethod;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    public static void createDailyVoucher(Reservation reservation, LocalDate date) {
        var paid = processDailyPayment(reservation);

        var dailyVoucher = DailyVoucher.builder()
                .reservation(reservation)
                .date(date)
                .price(reservation.getPricePerNight())
                .isPaid(paid)
                .guest(reservation.getGuest())
                .room(reservation.getRoom())
                .build();
        reservation.addDailyVoucher(dailyVoucher);
    }

    private static boolean processDailyPayment(Reservation reservation) {
        var deposit = reservation.getDepositAmount();
        var pricePerNight = reservation.getPricePerNight();
        var paid = false;
        if(deposit >= pricePerNight) {
            deposit = deposit - pricePerNight;
            paid = true;
            reservation.setDepositAmount(deposit);
            createPayment(reservation);
        }
        return paid;
    }

    private static void createPayment(Reservation reservation) {
        var payment = Payment.builder()
                .paymentDate(LocalDate.now())
                .amount(reservation.getPricePerNight())
                .paymentMethod(DEPOSIT)
                .notes("Automatic paid from deposit")
                .guest(reservation.getGuest())
                .roomNo(reservation.getRoom().getRoomNo())
                .build();
        payment.setReservation(reservation);
    }
}
