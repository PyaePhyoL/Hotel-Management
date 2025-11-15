package org.bytesync.hotelmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.reservation.ReservationGuestInfo;
import org.bytesync.hotelmanagement.dto.room.RoomDto;
import org.bytesync.hotelmanagement.dto.room.RoomGridView;
import org.bytesync.hotelmanagement.dto.room.RoomOverviewDetails;
import org.bytesync.hotelmanagement.dto.room.RoomSelectList;
import org.bytesync.hotelmanagement.model.enums.Floor;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.bytesync.hotelmanagement.model.enums.RoomType;
import org.bytesync.hotelmanagement.repository.ReservationRepository;
import org.bytesync.hotelmanagement.repository.RoomRepository;
import org.bytesync.hotelmanagement.repository.specification.RoomSpecification;
import org.bytesync.hotelmanagement.util.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    public RoomGridView getAllRoomsInGridView(RoomStatus roomStatus) {
        var specification = RoomSpecification.roomStatusEquals(roomStatus);
        var rooms = roomRepository.findAll(specification).stream().map(RoomMapper::toDto).toList();

        var fourth = rooms.stream().filter(room -> room.floor().equals(Floor.FOURTH)).toList();
        var fifth = rooms.stream().filter(room -> room.floor().equals(Floor.FIFTH)).toList();
        var seventh = rooms.stream().filter(room -> room.floor().equals(Floor.SEVENTH)).toList();
        var eighth = rooms.stream().filter(room -> room.floor().equals(Floor.EIGHTH)).toList();

        return RoomGridView.builder()
                .fourth(fourth)
                .fifth(fifth)
                .seventh(seventh)
                .eighth(eighth)
                .build();
    }

    public List<RoomDto> getAllAvailableRooms() {
        return roomRepository.findAllRoomDtosByStatus(RoomStatus.AVAILABLE);
    }

    public RoomOverviewDetails getRoomOverviewDetailsById(Integer id) {
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
}
