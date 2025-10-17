package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.bytesync.hotelmanagement.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/rooms/api")
@RequiredArgsConstructor
public class RoomsManagementApi {

    private final RoomService roomService;

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> getRooms(@RequestParam(required = false) RoomStatus status){
        var rooms = roomService.getAllRoomsInGridView(status);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Rooms Overview", rooms));
    }

    @GetMapping("/available")
    public ResponseEntity<ResponseMessage> getAvailableRooms(){
        var rooms = roomService.getAllAvailableRooms();
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Available Rooms", rooms));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ResponseMessage> getRoomDetails(@PathVariable Integer id){
        var roomDetails = roomService.getRoomOverviewDetailsById(id);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Room Details", roomDetails));
    }

    @GetMapping("/select-list")
    public ResponseEntity<ResponseMessage> selectRooms(){
        var rooms = roomService.selectList();
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Rooms Select", rooms));
    }
}
