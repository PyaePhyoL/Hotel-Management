package org.bytesync.hotelmanagement.service;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationGuestInfo getReservationGuestInfoById(Long id ){
        if(id != null && id != 0) {
            return reservationRepository.findReservationGuestInfoById(id).orElse(null);
        } else {
            return null;
        }

    }
}
