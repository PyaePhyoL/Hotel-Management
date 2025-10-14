package org.bytesync.hotelmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.reservation.ReservationForm;
import org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.repository.GuestRepository;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.bytesync.hotelmanagement.repository.RoomRepository;
import org.bytesync.hotelmanagement.util.mapper.ReservationMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;

    public ReservationGuestInfo getReservationGuestInfoById(Long id ){
        if(id != null && id != 0) {
            return reservationRepository.findReservationGuestInfoById(id).orElse(null);
        } else {
            return null;
        }

    }

    public String create(ReservationForm form) {
        var guest = guestRepository.findByNameAndNrc(form.getGuestName(), form.getGuestNrc())
                .orElseThrow(() -> new EntityNotFoundException("Guest not found"));
        var room = roomRepository.findById(form.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));

        var reservation = ReservationMapper.toEntity(form);
        reservation.setGuest(guest);
        reservation.setRoom(room);
    }
}
