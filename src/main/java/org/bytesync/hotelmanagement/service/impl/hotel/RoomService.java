package org.bytesync.hotelmanagement.service.impl.hotel;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo;
import org.bytesync.hotelmanagement.dto.room.RoomDto;
import org.bytesync.hotelmanagement.dto.room.RoomDashboardView;
import org.bytesync.hotelmanagement.dto.room.RoomOverviewDetails;
import org.bytesync.hotelmanagement.dto.room.RoomSelectList;
import org.bytesync.hotelmanagement.model.Room;
import org.bytesync.hotelmanagement.model.enums.Floor;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.bytesync.hotelmanagement.model.enums.RoomType;
import org.bytesync.hotelmanagement.repository.RoomRepository;
import org.bytesync.hotelmanagement.repository.specification.RoomSpecification;
import org.bytesync.hotelmanagement.util.mapper.RoomMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.bytesync.hotelmanagement.util.EntityOperationUtils.safeCall;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ReservationService reservationService;

    public RoomDashboardView getAllRoomsInGridView(RoomStatus roomStatus) {
        var specification = RoomSpecification.roomStatusEquals(roomStatus);
        var rooms = roomRepository.findAll(specification).stream().map(RoomMapper::toDto).toList();

        var fourth = rooms.stream().filter(room -> room.floor().equals(Floor.FOURTH)).toList();
        var fifth = rooms.stream().filter(room -> room.floor().equals(Floor.FIFTH)).toList();
        var seventh = rooms.stream().filter(room -> room.floor().equals(Floor.SEVENTH)).toList();
        var eighth = rooms.stream().filter(room -> room.floor().equals(Floor.EIGHTH)).toList();

        var availableRooms = rooms.stream().filter(room -> room.currentStatus().equals(RoomStatus.AVAILABLE)).toList().size();
        var bookingRooms = rooms.stream().filter(room -> room.currentStatus().equals(RoomStatus.BOOKING)).toList().size();
        var longStayRooms = rooms.stream().filter(room -> room.currentStatus().equals(RoomStatus.LONG_STAY)).toList().size();
        var normalRooms = rooms.stream().filter(room -> room.currentStatus().equals(RoomStatus.NORMAL_STAY)).toList().size();
        var sectionRooms = rooms.stream().filter(room -> room.currentStatus().equals(RoomStatus.SECTION_STAY)).toList().size();
        var serviceRooms = rooms.stream().filter(room -> room.currentStatus().equals(RoomStatus.IN_SERVICE)).toList().size();
        return RoomDashboardView.builder()
                .availableRooms(availableRooms)
                .bookingRooms(bookingRooms)
                .longStayRooms(longStayRooms)
                .normalStayRooms(normalRooms)
                .sectionRooms(sectionRooms)
                .serviceRooms(serviceRooms)
                .fourth(fourth)
                .fifth(fifth)
                .seventh(seventh)
                .eighth(eighth)
                .build();
    }

    public List<RoomDto> getAllAvailableRooms() {
        return roomRepository.findAllRoomDtosByStatus(RoomStatus.AVAILABLE);
    }

    public RoomOverviewDetails getRoomOverviewDetailsById(Long id) {
        var room = roomRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Room Not Found"));
        ReservationGuestInfo guestInfo = null;
        if(room.getCurrentReservationId() != null) {
            guestInfo = reservationService.getReservationGuestInfoById(room.getCurrentReservationId());
        }

        var roomOverviewDetails = RoomMapper.toRoomOverDetails(room);
        roomOverviewDetails.setGuestInfo(guestInfo);

        return roomOverviewDetails;
    }

    public List<RoomSelectList> selectAvailableRoomList() {
        var roomList = roomRepository.findAllRoomForSelectList();
        return roomList.stream().map(RoomMapper::toRoomSelectList).toList();
    }

    public List<RoomSelectList> selectAvailableDoubleRoomList() {
        var roomList = roomRepository.findAllRoomForSelectList().stream()
                .filter(room -> room.getRoomType().equals(RoomType.DOUBLE)).toList();
        return roomList.stream().map(RoomMapper::toRoomSelectList).toList();
    }

    public String changeRoomStatus(Long id, RoomStatus status) {
        var room = safeCall(roomRepository.findById(id), "Room", id);
        room.setCurrentStatus(status);
        roomRepository.save(room);
        return "Room Status Changed.";
    }

    public String changeRoomService(Long id, Integer price) {
        var room = safeCall(roomRepository.findById(id), "Room", id);
        room.setBasePrice(price);
        roomRepository.save(room);
        return "Room Price Changed.";
    }

    public PageResult<RoomDto> getList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.Direction.ASC, "roomNo");
        Page<Room> rooms = roomRepository.findAll(pageable);
        var dtos = rooms.getContent().stream().map(RoomMapper::toDto).toList();
        return new PageResult<>(dtos, rooms.getTotalElements(), page, size);
    }
}
