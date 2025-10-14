package org.bytesync.hotelmanagement.service;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.RoomGridView;
import org.bytesync.hotelmanagement.model.enums.Floor;
import org.bytesync.hotelmanagement.repository.RoomRepository;
import org.bytesync.hotelmanagement.util.mapper.RoomMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomGridView getAllRoomsInGridView() {
        var rooms = roomRepository.findAll().stream().map(RoomMapper::toDto).toList();

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
}
