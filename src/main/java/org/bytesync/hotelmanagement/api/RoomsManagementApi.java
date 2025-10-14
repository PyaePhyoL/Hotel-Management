package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.ResponseMessage;
import org.bytesync.hotelmanagement.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/rooms/api")
@RequiredArgsConstructor
public class RoomsManagementApi {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<ResponseMessage> getRooms(){
        var rooms = roomService.getAllRoomsInGridView();
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Rooms Overview", rooms));
    }
}
