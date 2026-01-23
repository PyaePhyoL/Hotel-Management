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
import org.bytesync.hotelmanagement.specification.ReservationSpecification;
import org.bytesync.hotelmanagement.service.impl.finance.VoucherService;
import org.bytesync.hotelmanagement.service.impl.guest.GuestRecordService;
import org.bytesync.hotelmanagement.service.impl.guest.GuestService;
import org.bytesync.hotelmanagement.service.interfaces.hotel.IReservationService;
import org.bytesync.hotelmanagement.util.mapper.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.bytesync.hotelmanagement.enums.GuestStatus.BLACKLIST;
import static org.bytesync.hotelmanagement.enums.IncomeType.ROOM_RENT;
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

        updateAssociation(reservation, room, guest, form.getContacts());

        return "Reservation created successfully";
    }

    @Transactional
    @Override
    public String checkinReservation(Long reservationId) {
        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);

        if(reservation.getStatus() == Status.BOOKING) {
            var checkinDateTime = getCurrentYangonZoneLocalDateTime();
            reservation.setCheckInDateTime(checkinDateTime);
            reservation.setStatus(Status.ACTIVE);

            reservation.getGuest().setIsStaying(true);

            reservationRepository.save(reservation);

            guestRecordService.createGuestRecord(reservation);
            voucherService.createVoucherFromReservation(reservation);

            return "Reservation checked in";
        } else {
            throw new IllegalStateException("Reservation is already started");
        }
    }

    @Override
    @Transactional
    public String checkoutReservation(Long reservationId) {
//        1st change the status in Reservation
        var reservation = safeCall(reservationRepository.findById(reservationId), "Reservation", reservationId);
        var checkoutDateTime = getCurrentYangonZoneLocalDateTime();

        reservation.setNewCheckOutDateTime(checkoutDateTime);
        reservation.setStatus(Status.FINISHED);

        var room = reservation.getRoom();
        var guest = reservation.getGuest();
//        2nd make the room and guest clear
        makeRoomInService(room);
        guestCheckout(guest);

//        3rd change the checkout time in guest record
        guestRecordService.updateGuestRecordWhenCheckout(reservationId, checkoutDateTime);
        
        var timeString = timeFormat(checkoutDateTime);
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
        reservation.setStatus(Status.CANCELED);
        reservationRepository.save(reservation);
        return "Reservation canceled successfully";
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

    @Override
    public Integer getActiveReservationCount() {
        return reservationRepository.countAllActive();
    }

    @Transactional
    @Override
    public String delete(Long id) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);
        var guestRecord = guestRecordRepository.findByReservationId(id).orElse(null);

        makeRoomAvailable(reservation.getRoom());
        guestCheckout(reservation.getGuest());
        if(guestRecord != null) guestRecordRepository.delete(guestRecord);
        reservationRepository.delete(reservation);

        return "Reservation deleted successfully";
    }

    @Override
    public String extendHours(Long id, ExtraHoursDto extraHoursDto) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);
        var newCheckoutTime = reservation.getCheckOutDateTime().plusHours(extraHoursDto.hour());
        var price = extraHoursDto.price() * extraHoursDto.hour();

        reservation.setNewCheckOutDateTime(newCheckoutTime);

        if(reservation.getStayType() != StayType.SECTION) {
            throw new IllegalArgumentException("This is not a section type");
        }

        if(reservation.getStatus() == Status.FINISHED) {
            throw new IllegalArgumentException("This is finished already");
        }
        var notes = String.format("Extend for %d hour(s)", extraHoursDto.hour());
        voucherService.createAdditionalVoucher(new VoucherCreatForm(id, ROOM_RENT, price, notes));

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
        voucherService.createAdditionalVoucher(new VoucherCreatForm(id, ROOM_RENT, price, notes));

        reservationRepository.save(reservation);

        return "New Checkout Date : " + dateTimeFormat(reservation.getCheckOutDateTime());
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

    @Override
    public String updateReservation(Long id, ReservationForm form) {
        var reservation = safeCall(reservationRepository.findById(id), "Reservation", id);

        if(reservation.getStatus() == Status.FINISHED || reservation.getStatus() == Status.CANCELED) {
            throw new IllegalStateException("Reservation is finished");
        }

        var guest = safeCall(guestRepository.findByNameAndNrc(form.getGuestName(), form.getGuestNrc()), "Guest", form.getGuestNrc());

        changePhoneIfAddNewPhone(form.getPhone(), guest);

        if(!reservation.getRoom().getRoomNo().equals(form.getRoomId())) {
            changeRoom(reservation, form.getRoomId());
        }

        updateReservationPrice(reservation, form);

        updateContacts(reservation, form.getContacts());

        ReservationMapper.updateReservation(reservation, form);

        reservationRepository.save(reservation);

        return "Reservation updated successfully";
    }

//    Private methods

    private void updateAssociation(Reservation reservation, Room room, Guest guest, List<ContactDto> contactDtos) {
        room.addReservation(reservation);
        room.setCurrentStatus(
                RoomMapper.getRoomCurrentStatusFromReservation(reservation.getStatus(), reservation.getStayType()));

        guest.addReservation(reservation);
        guest.setIsStaying(false);
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

                    if(guest.getCurrentReservationId() != null)
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

    private void updateReservationPrice(Reservation reservation, ReservationForm form) {
        reservation.setPrice(form.getPrice());
        if(form.getExtraPrice() != null && form.getExtraPrice() > 0) {
            voucherService.createAdditionalVoucher(new VoucherCreatForm(reservation.getId(), ROOM_RENT, form.getExtraPrice(), ""));
        }
    }

    private void changeRoom(Reservation reservation, Long roomId) {
        var newRoom = findRoom(roomId);
        var oldRoom = reservation.getRoom();
        makeRoomInService(oldRoom);

        reservation.setRoom(newRoom);
        newRoom.addReservation(reservation);
        newRoom.setCurrentStatus(
                RoomMapper.getRoomCurrentStatusFromReservation(reservation.getStatus(), reservation.getStayType()));

        roomRepository.save(newRoom);
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


    private void updateContacts(Reservation reservation, List<ContactDto> contactDtos) {
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
    }



}
