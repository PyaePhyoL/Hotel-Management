package org.bytesync.hotelmanagement.service;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    
}
