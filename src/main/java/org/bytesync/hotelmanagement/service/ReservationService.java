package org.bytesync.hotelmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.reservation.ReservationForm;
import org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo;
import org.bytesync.hotelmanagement.dto.reservation.ReservationInfo;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.Room;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.bytesync.hotelmanagement.repository.GuestRepository;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.bytesync.hotelmanagement.repository.RoomRepository;
import org.bytesync.hotelmanagement.repository.specification.ReservationSpecification;
import org.bytesync.hotelmanagement.util.EntityOperationUtils;
import org.bytesync.hotelmanagement.util.mapper.ReservationMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.timeFormat;

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

    @Transactional
    public String create(ReservationForm form) {
        var guest = guestRepository.findByNameAndNrc(form.getGuestName(), form.getGuestNrc())
                .orElseThrow(() -> new EntityNotFoundException("Guest not found"));

        var room = findRoom(form.getRoomId());

        var reservation = ReservationMapper.toEntity(form);
        reservation.setGuest(guest);
        reservation.setRoom(room);

        var savedReservation = reservationRepository.save(reservation);

        room.addReservation(savedReservation);
        guest.addReservation(savedReservation);
        roomRepository.save(room);
        guestRepository.save(guest);
        return "Reservation created successfully";
    }

    private Room findRoom(Integer roomNo) {
        var room = roomRepository.findById(roomNo)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        if(room.getCurrentStatus() != RoomStatus.AVAILABLE) {
            throw new IllegalStateException("Room is not available");
        } else {
            return room;
        }
    }


    @Transactional
    public String checkoutReservation(Long reservationId, LocalDateTime checkoutTime) {
        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);
        reservation.setCheckOutTime(checkoutTime);
        reservation.setIsActive(false);

        var room = reservation.getRoom();
        room.setCurrentReservationId(null);
        room.setCurrentStatus(RoomStatus.AVAILABLE);
        
        var timeString = timeFormat(checkoutTime);
        reservationRepository.save(reservation);
        roomRepository.save(room);

        return "%s is checked out at %s".formatted(room.getNo(), timeString);
    }

    public void createDailyVoucher() {

    }

    public PageResult<ReservationInfo> getAll(boolean active, int page, int size) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC, "id");
        var spec = ReservationSpecification.filterByStatus(active);

        Page<Reservation> result = reservationRepository.findAll(spec, pageable);

        List<ReservationInfo> infos = result.stream().map(ReservationMapper::toReservationInfo).toList();

        return new PageResult<>(infos, result.getTotalElements(), page, size);
    }

}
