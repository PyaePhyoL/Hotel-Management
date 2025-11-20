package org.bytesync.hotelmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.guest.ContactDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.reservation.ReservationDetails;
import org.bytesync.hotelmanagement.dto.reservation.ReservationForm;
import org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo;
import org.bytesync.hotelmanagement.dto.reservation.ReservationInfo;
import org.bytesync.hotelmanagement.model.DailyVoucher;
import org.bytesync.hotelmanagement.model.Guest;
import org.bytesync.hotelmanagement.model.Reservation;
import org.bytesync.hotelmanagement.model.Room;
import org.bytesync.hotelmanagement.model.enums.GuestStatus;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.bytesync.hotelmanagement.model.enums.Status;
import org.bytesync.hotelmanagement.repository.ContactRepository;
import org.bytesync.hotelmanagement.repository.GuestRepository;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.bytesync.hotelmanagement.repository.RoomRepository;
import org.bytesync.hotelmanagement.repository.specification.ReservationSpecification;
import org.bytesync.hotelmanagement.util.mapper.GuestMapper;
import org.bytesync.hotelmanagement.util.mapper.ContactMapper;
import org.bytesync.hotelmanagement.util.mapper.ReservationMapper;
import org.bytesync.hotelmanagement.util.mapper.RoomMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;
import static org.bytesync.hotelmanagement.util.EntityOperationUtils.timeFormat;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRecordService guestRecordService;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final ContactRepository contactRepository;

    public ReservationGuestInfo getReservationGuestInfoById(Long id ){
        if (id == null || id <= 0) {
            return null;
        }

        return reservationRepository.findById(id)
                .map(ReservationMapper::toReservationGuestInfo)
                .orElse(null);
    }

    @Transactional
    public String create(ReservationForm form) {
        var guest = findOrCreateGuest(form);
        var room = findRoom(form.getRoomId());
        var reservation = createReservation(form, guest, room);

        guestRecordService.createGuestRecord(reservation);

        updateAssociation(reservation, room, guest, form.getContacts());
        return "Reservation created successfully";
    }

    private void updateAssociation(Reservation reservation, Room room, Guest guest, List<ContactDto> contactDtos) {
        room.addReservation(reservation);
        room.setCurrentStatus(
                RoomMapper.getRoomCurrentStatusFromReservation(reservation.getStatus(), reservation.getStayType()));

        guest.addReservation(reservation);
        addContactsToReservation(reservation, contactDtos);

        roomRepository.save(room);
        guestRepository.save(guest);
    }

    private void addContactsToReservation(Reservation reservation, List<ContactDto> contactDtos) {
        contactDtos.stream()
                .filter(rs -> !rs.name().isBlank() || !rs.phone().isBlank())
                .forEach(dto -> {
            reservation.addRelation(ContactMapper.toEntity(dto));
        });
    }

    private Reservation createReservation(ReservationForm form, Guest guest, Room room) {
        var reservation = ReservationMapper.toEntity(form);

        reservation.setGuest(guest);
        reservation.setRoom(room);
        return reservationRepository.save(reservation);
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

    private Guest findOrCreateGuest(ReservationForm form) {
        return guestRepository.findByNameAndNrc(form.getGuestName(), form.getGuestNrc())
                .map(guest -> {
                    // may be phone number might be new
                    guest.addPhone(form.getPhone());
                    return guest;
                }).orElseGet(() -> {
                    Guest guest = new Guest();
                    guest.setName(form.getGuestName());
                    guest.setNrc(form.getGuestNrc());
                    guest.addPhone(form.getPhone());
                    guest.setStatus(GuestStatus.GOOD);
                    return guestRepository.save(guest);
                });
    }


    @Transactional
    public String checkoutReservation(Long reservationId, LocalDateTime checkoutTime) {
//        1st change the status in Reservation
        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);
        reservation.setCheckOutTime(checkoutTime);
        reservation.setStatus(Status.PAST);

        var room = reservation.getRoom();
        var guest = reservation.getGuest();
//        2nd make the room and guest clear
        makeRoomAvailable(room);
        guestCheckout(guest);

//        3rd change the checkout time in guest record
        guestRecordService.updateGuestRecordWhenCheckout(guest.getId(), room.getRoomNo(), checkoutTime);
        
        var timeString = timeFormat(checkoutTime);
        reservationRepository.save(reservation);

        return "Room-%s is checked out at %s".formatted(room.getRoomNo(), timeString);
    }


    public PageResult<ReservationInfo> getAll(int page, int size, Status status) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC, "id");
        var spec = ReservationSpecification.filterByStatus(Status.ACTIVE);
        Page<Reservation> result = reservationRepository.findAll(pageable);

        List<ReservationInfo> infos = result.stream().map(ReservationMapper::toReservationInfo).toList();

        return new PageResult<>(infos, result.getTotalElements(), page, size);
    }

    public String delete(Long reservationId) {
        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);
        makeRoomAvailable(reservation.getRoom());
        guestCheckout(reservation.getGuest());
        reservationRepository.delete(reservation);
        return "Reservation deleted successfully";
    }

    private void makeRoomAvailable(Room room) {
        room.setCurrentStatus(RoomStatus.AVAILABLE);
        room.setCurrentReservationId(null);
        roomRepository.save(room);
    }

    private void guestCheckout(Guest guest) {
        guest.setCurrentReservationId(null);
        guest.setIsStaying(false);
        guestRepository.save(guest);
    }

    public ReservationDetails getDetailsById(long id) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);

        var resvDetails = ReservationMapper.toReservationDetails(reservation);
        var guestDto = GuestMapper.toDto(reservation.getGuest());
        var roomDto = RoomMapper.toDto(reservation.getRoom());
        var totalPrice = getTotalPriceInReservation(reservation);
        var paidPrice = getPaidPriceInReservation(reservation);
        var leftPrice = totalPrice - paidPrice;
        var contacts = reservation.getContacts().stream().map(ContactMapper::toDto).toList();

        resvDetails.setGuestDetails(guestDto);
        resvDetails.setRoomDetails(roomDto);
        resvDetails.setTotalPrice(totalPrice);
        resvDetails.setPaidPrice(paidPrice);
        resvDetails.setLeftPrice(leftPrice);
        resvDetails.setContacts(contacts);

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

    public Integer getDailyCheckIns() {
        var today =  LocalDate.now();
        var reservations = reservationRepository.findByCheckInDate(today);
        return reservations.size();
    }


    public String update(long id, ReservationForm form) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);
        ReservationMapper.updateReservation(reservation, form);

        updateContacts(reservation, form.getContacts());
        
        reservationRepository.save(reservation);

        return "Reservation updated successfully : " + id;
    }

    public void updateContacts(Reservation reservation, List<ContactDto> contactDtos) {

        var newContacts = contactDtos.stream().filter(dto -> dto.id() == null).toList();

        addContactsToReservation(reservation, newContacts);

        var oldContacts = contactDtos.stream().filter(dto -> dto.id() != null).toList();

        oldContacts.forEach(dto -> {
            contactRepository.findById(dto.id()).ifPresent(c -> ContactMapper.updateContent(c, dto));
        });
    }

}
