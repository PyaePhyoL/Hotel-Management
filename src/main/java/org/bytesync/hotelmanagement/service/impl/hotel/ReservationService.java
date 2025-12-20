package org.bytesync.hotelmanagement.service.impl.hotel;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.guest.ContactDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.reservation.*;
import org.bytesync.hotelmanagement.model.*;
import org.bytesync.hotelmanagement.enums.GuestStatus;
import org.bytesync.hotelmanagement.enums.RoomStatus;
import org.bytesync.hotelmanagement.enums.Status;
import org.bytesync.hotelmanagement.enums.StayType;
import org.bytesync.hotelmanagement.repository.*;
import org.bytesync.hotelmanagement.repository.specification.ReservationSpecification;
import org.bytesync.hotelmanagement.service.impl.finance.VoucherService;
import org.bytesync.hotelmanagement.service.impl.guest.GuestRecordService;
import org.bytesync.hotelmanagement.service.interfaces.hotel.IReservationService;
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

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.*;

@Service
@RequiredArgsConstructor
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRecordService guestRecordService;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final ContactRepository contactRepository;
    private final VoucherService voucherService;
    private final GuestRecordRepository guestRecordRepository;

    @Override
    public ReservationGuestInfo getReservationGuestInfoById(Long id ){
        if (id == null || id <= 0) {
            return null;
        }

        return reservationRepository.findById(id)
                .map(ReservationMapper::toReservationGuestInfo)
                .orElse(null);
    }

    @Override
    @Transactional
    public String create(ReservationForm form) {
        var guest = findOrCreateGuest(form);
        var room = findRoom(form.getRoomId());
        var reservation = createReservation(form, guest, room);

        voucherService.createVoucher(reservation);

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

    private Room findRoom(Long roomNo) {
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

    @Override
    @Transactional
    public String checkoutReservation(Long reservationId, LocalDateTime checkoutTime) {
//        1st change the status in Reservation
        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);
        reservation.setNewCheckOutDateTime(checkoutTime);
        reservation.setStatus(Status.FINISHED);

        var room = reservation.getRoom();
        var guest = reservation.getGuest();
//        2nd make the room and guest clear
        makeRoomInService(room);
        guestCheckout(guest);

//        3rd change the checkout time in guest record
        guestRecordService.updateGuestRecordWhenCheckout(reservationId, checkoutTime);
        
        var timeString = timeFormat(checkoutTime);
        reservationRepository.save(reservation);

        return "Room-%s is checked out at %s".formatted(room.getRoomNo(), timeString);
    }

    @Override
    public PageResult<ReservationInfo> getAll(int page, int size, List<Status> statusList) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.DESC, "id");
        var spec = ReservationSpecification.filterByStatus(statusList);
        Page<Reservation> result = reservationRepository.findAll(spec, pageable);

        List<ReservationInfo> infos = result.stream().map(ReservationMapper::toReservationInfo).toList();

        return new PageResult<>(infos, result.getTotalElements(), page, size);
    }

    @Transactional
    @Override
    public String cancelReservation(Long reservationId) {
        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);
        if(reservation.getStatus() == Status.ACTIVE) {
            throw new IllegalStateException("Reservation is active");
        }

        makeRoomAvailable(reservation.getRoom());
        guestCheckout(reservation.getGuest());

        var guestRecord = safeCall(guestRecordRepository.findByReservationId(reservationId), "Guest Record's reservation", reservationId);
        reservation.setStatus(Status.CANCELED);

        guestRecordRepository.delete(guestRecord);
        reservationRepository.save(reservation);
        return "Reservation canceled successfully";
    }

    private void makeRoomAvailable(Room room) {
        room.setCurrentStatus(RoomStatus.AVAILABLE);
        room.setCurrentReservationId(null);
        roomRepository.save(room);
    }

    private void makeRoomInService(Room room) {
        room.setCurrentStatus(RoomStatus.IN_SERVICE);
        room.setCurrentReservationId(null);
        roomRepository.save(room);
    }

    private void guestCheckout(Guest guest) {
        guest.setCurrentReservationId(null);
        guest.setIsStaying(false);
        guestRepository.save(guest);
    }

    @Override
    public ReservationDetails getDetailsById(Long id) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);

        var resvDetails = ReservationMapper.toReservationDetails(reservation);
        var guestDto = GuestMapper.toDto(reservation.getGuest());
        var roomDto = RoomMapper.toDto(reservation.getRoom());
        var totalPrice = getTotalPriceInReservation(reservation);
        var paidPrice = getPaidPriceInReservation(reservation);
        var leftPrice = totalPrice - paidPrice;
        var refundPrice = getTotalRefundInReservation(reservation);
        var contacts = reservation.getContactList().stream().map(ContactMapper::toDto).toList();

        resvDetails.setGuestDetails(guestDto);
        resvDetails.setRoomDetails(roomDto);
        resvDetails.setTotalPrice(totalPrice);
        resvDetails.setPaidPrice(paidPrice);
        resvDetails.setLeftPrice(leftPrice);
        resvDetails.setRefundPrice(refundPrice);
        resvDetails.setContacts(contacts);

        return resvDetails;
    }

    private Integer getTotalPriceInReservation(Reservation reservation) {
        return reservation.getVoucherList().stream().map(Voucher::getPrice).reduce(0, Integer::sum);
    }

    private Integer getPaidPriceInReservation(Reservation reservation) {
        return reservation.getVoucherList().stream().filter(Voucher::getIsPaid).map(Voucher::getPrice).reduce(0, Integer::sum);
    }

    private Integer getTotalRefundInReservation(Reservation reservation) {
        return reservation.getRefundList().stream().map(Refund::getAmount).reduce(0, Integer::sum);
    }

    @Override
    @Transactional
    public String changeRoom(Long id, Long roomId) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);
        var newRoom = safeCall(roomRepository.findById(roomId), "Room", id);
        var oldRoom = reservation.getRoom();
        makeRoomInService(oldRoom);

        reservation.setRoom(newRoom);
        newRoom.addReservation(reservation);
        roomRepository.save(newRoom);
        reservationRepository.save(reservation);
        return "Room-%s is changed with Room-%s".formatted(oldRoom.getRoomNo(), newRoom.getRoomNo());

    }

    @Override
    public Integer getDailyCheckIns() {
        var today =  LocalDate.now();
        var reservations = reservationRepository.findByCheckInDate(today);
        return reservations.size();
    }

    @Override
    public String update(Long id, ReservationForm form) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);
        ReservationMapper.updateReservation(reservation, form);

        updateContacts(reservation, form.getContacts());
        
        reservationRepository.save(reservation);

        return "Reservation updated successfully : " + id;
    }

    @Override
    public void updateContacts(Reservation reservation, List<ContactDto> contactDtos) {

        var newContacts = contactDtos.stream().filter(dto -> dto.id() == null).toList();

        addContactsToReservation(reservation, newContacts);

        var oldContacts = contactDtos.stream().filter(dto -> dto.id() != null).toList();

        oldContacts.forEach(dto -> {
            contactRepository.findById(dto.id()).ifPresent(c -> ContactMapper.updateContent(c, dto));
        });
    }

    @Transactional
    @Override
    public String delete(Long id) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);
        var guestRecord = safeCall(guestRecordRepository.findByReservationId(id), "Guest's record reservation", id);

        makeRoomAvailable(reservation.getRoom());
        guestCheckout(reservation.getGuest());
        guestRecordRepository.delete(guestRecord);
        reservationRepository.delete(reservation);

        return "Reservation deleted successfully";
    }

    @Override
    public String extendHours(Long id, ExtraHoursDto extraHoursDto) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);
        var newCheckoutTime = reservation.getCheckOutDateTime().plusHours(extraHoursDto.hour());
        var price = extraHoursDto.price() * extraHoursDto.hour();

        reservation.setNewCheckOutDateTime(newCheckoutTime);
        reservation.increasePrice(price);

        if(reservation.getStayType() != StayType.SECTION) {
            throw new IllegalArgumentException("This is not a section type");
        }

        if(reservation.getStatus() == Status.FINISHED) {
            throw new IllegalArgumentException("This is finished already");
        }

        voucherService.createExtendVoucher(reservation, price);

        reservationRepository.save(reservation);

        return "New Checkout time : " + timeFormat(reservation.getCheckOutDateTime());
    }

    @Override
    public String extendDays(Long id, Integer days) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);
        var newCheckoutTime = reservation.getCheckOutDateTime().plusDays(days);

        var price = days * reservation.getPrice();

        reservation.setNewCheckOutDateTime(newCheckoutTime);

        voucherService.createExtendVoucher(reservation, price);

        reservationRepository.save(reservation);

        return "New Checkout time : " + timeFormat(reservation.getCheckOutDateTime());
    }
}
