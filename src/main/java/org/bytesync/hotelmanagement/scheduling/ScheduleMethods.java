package org.bytesync.hotelmanagement.scheduling;

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

    @Scheduled(fixedRate = 1000 * 60)
    public void scheduleEveryDayCreatingVouchers() {

        log.info(" =============> Creating Voucher Schedule Task <=============");

        var date = LocalDate.now();
        var reservations = reservationRepository.findAllActiveReservations();

        reservations.forEach(reservation -> createDailyVoucher(reservation, date));

        reservationRepository.saveAll(reservations);
    }

    private void createDailyVoucher(Reservation reservation, LocalDate date) {
        var dailyVoucher = DailyVoucher.builder()
                .reservation(reservation)
                .date(date)
                .price(reservation.getPricePerNight())
                .isPaid(false)
                .build();
        reservation.addDailyVoucher(dailyVoucher);
    }
}
