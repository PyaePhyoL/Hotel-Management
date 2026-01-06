package org.bytesync.hotelmanagement.service.impl.hotel;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.finance.VoucherCreatForm;
import org.bytesync.hotelmanagement.dto.guest.ContactDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.reservation.*;
import org.bytesync.hotelmanagement.enums.*;
import org.bytesync.hotelmanagement.model.*;
import org.bytesync.hotelmanagement.repository.*;
import org.bytesync.hotelmanagement.repository.specification.ReservationSpecification;
import org.bytesync.hotelmanagement.service.impl.finance.VoucherService;
import org.bytesync.hotelmanagement.service.impl.guest.GuestRecordService;
import org.bytesync.hotelmanagement.service.impl.guest.GuestService;
import org.bytesync.hotelmanagement.service.interfaces.hotel.IReservationService;
import org.bytesync.hotelmanagement.util.mapper.GuestMapper;
import org.bytesync.hotelmanagement.util.mapper.ContactMapper;
import org.bytesync.hotelmanagement.util.mapper.ReservationMapper;
import org.bytesync.hotelmanagement.util.mapper.RoomMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.bytesync.hotelmanagement.enums.GuestStatus.BLACKLIST;
import static org.bytesync.hotelmanagement.enums.VoucherType.EXTEND;
import static org.bytesync.hotelmanagement.enums.VoucherType.getVoucherTypeFromStayType;
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
    private final GuestService guestService;

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

        voucherService.createVoucherFromReservation(reservation);

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
                    changePhoneIfAddNewPhone(form.getPhone(), guest);

                    if(guest.getIsStaying())
                        throw new IllegalStateException("This guest has another reservation");

                    if(guest.getStatus() == BLACKLIST)
                        throw new IllegalArgumentException("This guest is blacklisted");

                    return guest;
                }).orElseGet(() -> {
                    Guest guest = new Guest();
                    guest.setName(form.getGuestName());
                    guest.setNrc(form.getGuestNrc());
                    guest.setPhoneNumber(form.getPhone());
                    guest.setStatus(GuestStatus.GOOD);
                    guestService.checkGuestExists(guest);
                    return guestRepository.save(guest);
                });
    }

    private void changePhoneIfAddNewPhone(String phone, Guest guest) {
        if(phone != null && !phone.isBlank() && !guest.getPhoneNumber().equals(phone)) {
            guest.setPhoneNumber(phone);
            guestRepository.save(guest);
        }
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
        var leftPrice = getLeftPriceInReservation(reservation);
        var refundPrice = getTotalRefundInReservation(reservation);
        var contacts = reservation.getContactList().stream().map(ContactMapper::toDto).toList();

        resvDetails.setGuestDetails(guestDto);
        resvDetails.setRoomDetails(roomDto);
        resvDetails.setTotalPrice(totalPrice);
        resvDetails.setLeftPrice(leftPrice);
        resvDetails.setRefundPrice(refundPrice);
        resvDetails.setContacts(contacts);

        return resvDetails;
    }

    private Integer getLeftPriceInReservation(Reservation reservation) {
        return reservation.getVoucherList().stream().filter(v -> !v.getIsPaid()).map(Voucher::getPrice).reduce(0, Integer::sum);
    }

    private Integer getTotalPriceInReservation(Reservation reservation) {
        return reservation.getPaymentList().stream().map(Payment::getAmount).reduce(0, Integer::sum);
    }

    private Integer getPaidPriceInReservation(Reservation reservation) {
        return reservation.getVoucherList().stream().filter(Voucher::getIsPaid).map(Voucher::getPrice).reduce(0, Integer::sum);
    }

    private Integer getTotalRefundInReservation(Reservation reservation) {
        return reservation.getRefundList().stream().map(Refund::getAmount).reduce(0, Integer::sum);
    }

    @Override
    @Transactional
    public String changeRoom(Long reservationId, Long roomId, Integer extraPrice) {
        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);
        var newRoom = findRoom(roomId);
        var oldRoom = reservation.getRoom();
        makeRoomInService(oldRoom);

        reservation.setRoom(newRoom);
        newRoom.addReservation(reservation);
        newRoom.setCurrentStatus(
                RoomMapper.getRoomCurrentStatusFromReservation(reservation.getStatus(), reservation.getStayType()));

        if(extraPrice != null) {
            updateReservationPrice(reservationId, extraPrice);
        }

        roomRepository.save(newRoom);
        reservationRepository.save(reservation);
        return "Room-%s is changed with Room-%s".formatted(oldRoom.getRoomNo(), newRoom.getRoomNo());

    }

    @Override
    public String updateReservationPrice(Long reservationId, Integer price) {
        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);
        var stayType = reservation.getStayType();
        var voucherType = getVoucherTypeFromStayType(stayType);

        if(stayType == StayType.NORMAL) {
            if(price > 0) {
                voucherService.createAdditionalVoucher(new VoucherCreatForm(reservationId, voucherType, price, ""));
            }
        } else if(stayType == StayType.LONG) {
            reservation.setPrice(price);
        }

        reservationRepository.save(reservation);
        return "Price per night has been updated";
    }

    @Override
    public Integer getActiveReservationCount() {
        return reservationRepository.countAllActive();
    }


    @Override
    public String updateContacts(Long reservationId, List<ContactDto> contactDtos) {

        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);

        reservation.getContactList().clear();

        var newContacts = contactDtos.stream().filter(dto -> dto.id() == null).toList();

        addContactsToReservation(reservation, newContacts);

        var oldContacts = contactDtos.stream().filter(dto -> dto.id() != null).toList();

        oldContacts.forEach(dto -> {
            contactRepository.findById(dto.id()).ifPresent(contact -> {
                ContactMapper.updateContent(contact, dto);
                reservation.getContactList().add(contact);
            });
        });

        reservationRepository.save(reservation);

        return "Reservation's contacts have been updated";
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
        var notes = String.format("Extend for %d hour(s)", extraHoursDto.hour());
        voucherService.createAdditionalVoucher(new VoucherCreatForm(id, EXTEND, price, notes));

        reservationRepository.save(reservation);

        return "New Checkout time : " + timeFormat(reservation.getCheckOutDateTime());
    }

    @Override
    public String extendDays(Long id, ExtraDaysDto extraDaysDto) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);
        var newCheckoutTime = reservation.getCheckOutDateTime().plusDays(extraDaysDto.day());

        var price = extraDaysDto.price();

        reservation.setNewCheckOutDateTime(newCheckoutTime);

        var notes =  String.format("Extend for %d day(s)".formatted(extraDaysDto.day()));
        voucherService.createAdditionalVoucher(new VoucherCreatForm(id, EXTEND, price, notes));

        reservationRepository.save(reservation);

        return "New Checkout Date : " + dateFormat(reservation.getCheckOutDateTime());
    }

    @Override
    public PageResult<ReservationInfo> search(String query, int page, int size, boolean status) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.DESC, "checkInDateTime"));
        Specification<Reservation> spec = ReservationSpecification.search(query, status);
        Page<Reservation> reservations = reservationRepository.findAll(spec, pageable);
        List<ReservationInfo> infoList = reservations.getContent().stream().map(ReservationMapper::toReservationInfo).toList();
        return new PageResult<>(infoList, reservations.getTotalElements(), page, size);
    }

    @Override
    public String updateGuestNumber(Long id, Integer guests) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);

        reservation.setNoOfGuests(guests);
        reservationRepository.save(reservation);

        return "No. of guests updated successfully";
    }

    @Override
    public String updateDeposit(Long id, Integer deposit) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);
        reservation.setDeposit(deposit);
        reservationRepository.save(reservation);
        return "Deposit amount updated successfully";
    }
}
