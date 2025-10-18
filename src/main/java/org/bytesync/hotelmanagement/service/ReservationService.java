package org.bytesync.hotelmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.reservation.ReservationDetails;
import org.bytesync.hotelmanagement.dto.reservation.ReservationForm;
import org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo;
import org.bytesync.hotelmanagement.dto.reservation.ReservationInfo;
import org.bytesync.hotelmanagement.model.DailyVoucher;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.Room;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.bytesync.hotelmanagement.repository.GuestRepository;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.bytesync.hotelmanagement.repository.RoomRepository;
import org.bytesync.hotelmanagement.repository.specification.ReservationSpecification;
import org.bytesync.hotelmanagement.scheduling.ScheduleMethods;
import org.bytesync.hotelmanagement.util.mapper.GuestMapper;
import org.bytesync.hotelmanagement.util.mapper.ReservationMapper;
import org.bytesync.hotelmanagement.util.mapper.RoomMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

        ScheduleMethods.createDailyVoucher(reservation, form.getCheckInTime().toLocalDate());

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
        makeRoomAvailable(room);
        
        var timeString = timeFormat(checkoutTime);
        reservationRepository.save(reservation);

        return "Room-%s is checked out at %s".formatted(room.getRoomNo(), timeString);
    }

    public PageResult<ReservationInfo> getAll(boolean active, int page, int size) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC, "id");
        var spec = ReservationSpecification.filterByStatus(active);

        Page<Reservation> result = reservationRepository.findAll(spec, pageable);

        List<ReservationInfo> infos = result.stream().map(ReservationMapper::toReservationInfo).toList();

        return new PageResult<>(infos, result.getTotalElements(), page, size);
    }

    public String delete(Long reservationId) {
        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);
        makeRoomAvailable(reservation.getRoom());
        reservationRepository.delete(reservation);
        return "Reservation deleted successfully";
    }

    private void makeRoomAvailable(Room room) {
        room.setCurrentStatus(RoomStatus.AVAILABLE);
        room.setCurrentReservationId(null);
        roomRepository.save(room);
    }

    public ReservationDetails getDetailsById(long id) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);

        var resvDetails = ReservationMapper.toReservationDetails(reservation);
        var guestDto = GuestMapper.toDto(reservation.getGuest());
        var roomDto = RoomMapper.toDto(reservation.getRoom());
        var totalPrice = getTotalPriceInReservation(reservation);
        var paidPrice = getPaidPriceInReservation(reservation);
        var leftPrice = totalPrice - paidPrice;

        resvDetails.setGuestDetails(guestDto);
        resvDetails.setRoomDetails(roomDto);
        resvDetails.setTotalPrice(totalPrice);
        resvDetails.setPaidPrice(paidPrice);
        resvDetails.setLeftPrice(leftPrice);

        return resvDetails;
    }

    public Integer getTotalPriceInReservation(Reservation reservation) {
        return reservation.getDailyVouchers().stream().map(DailyVoucher::getPrice).reduce(0, Integer::sum);
    }

    public Integer getPaidPriceInReservation(Reservation reservation) {
        return reservation.getDailyVouchers().stream().filter(DailyVoucher::getIsPaid).map(DailyVoucher::getPrice).reduce(0, Integer::sum);
    }

    @Transactional
    public String changeRoom(long id, int roomId) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);
        var newRoom = safeCall(roomRepository.findById(roomId), "Room", id);
        var oldRoom = reservation.getRoom();
        makeRoomAvailable(oldRoom);

        reservation.setRoom(newRoom);
        newRoom.addReservation(reservation);
        roomRepository.save(newRoom);
        reservationRepository.save(reservation);
        return "Room-%s is changed with Room-%s".formatted(oldRoom.getRoomNo(), newRoom.getRoomNo());

    }
}
