package org.bytesync.hotelmanagement.scheduling;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytesync.hotelmanagement.model.enums.Status;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.bytesync.hotelmanagement.repository.RoomRepository;
import org.bytesync.hotelmanagement.service.impl.finance.VoucherService;
import org.bytesync.hotelmanagement.util.mapper.RoomMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleMethods {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final VoucherService voucherService;

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void scheduleEveryDayCreatingVouchers() {

        log.info(" =============> Creating Voucher Schedule Task <=============");
        var reservations = reservationRepository.findAllActiveReservations();

        reservations.forEach(reservation -> {
            voucherService.createVoucher(reservation);
            reservation.incrementDaysOfStayByOne();
        });
    }


    @Scheduled(cron = "0 * * * * *")
    public void checkBookingReservationAndChangeStatus() {

        var reservations = reservationRepository.findAllBookingReservations();

        reservations.forEach(reservation -> {
            var room = reservation.getRoom();
            var status = checkDateTimeAndGetStatus(
                    reservation.getCheckInDateTime(),
                    reservation.getCheckOutDateTime());
            if(status != reservation.getStatus()) {
                reservation.setStatus(status);
                room.setCurrentStatus(RoomMapper.getRoomCurrentStatusFromReservation(reservation.getStatus(), reservation.getStayType()));
                roomRepository.save(room);
            }
        });

        reservationRepository.saveAll(reservations);
    }



    public static Status checkDateTimeAndGetStatus(LocalDateTime checkIn, LocalDateTime checkOut) {
        var now = LocalDateTime.now();

        if(now.isBefore(checkIn)) {
            return Status.BOOKING;
        } else if(now.isAfter(checkIn) || now.isEqual(checkIn)) {
            return Status.ACTIVE;
        } else if(null != checkOut && now.isAfter(checkOut)) {
            return Status.FINISHED;
        } else {
            return null;
        }
    }
}
