package org.bytesync.hotelmanagement.scheduling;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytesync.hotelmanagement.model.DailyVoucher;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        var dailyVoucher = DailyVoucher.builder()
                .reservation(reservation)
                .date(date)
                .price(reservation.getPricePerNight())
                .isPaid(false)
                .guest(reservation.getGuest())
                .room(reservation.getRoom())
                .build();
        reservation.addDailyVoucher(dailyVoucher);
    }
}
