package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.model.enums.RoomStatus;
import org.bytesync.hotelmanagement.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
    public ResponseEntity<ResponseMessage> selectAvailableRooms(){
        var rooms = roomService.selectAvailableRoomList();
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Rooms Select", rooms));
    }

    @GetMapping("/select-list/double")
    public ResponseEntity<ResponseMessage> selectAvailableDoubleRooms(){
        var rooms = roomService.selectAvailableDoubleRoomList();
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Rooms Select", rooms));
    }

    @PatchMapping("/change-status/{id}/{status}")
    public ResponseEntity<ResponseMessage> changeRoomStatus(@PathVariable Integer id,
                                                            @PathVariable RoomStatus status){
        var message = roomService.changeRoomStatus(id, status);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Room Status change", message));
    }

    @PatchMapping("/change-price/{id}")
    public ResponseEntity<ResponseMessage> changeRoomPrice(@PathVariable Integer id, @RequestParam Integer price){
        var message = roomService.changeRoomService(id, price);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Room Price change", message));
    }
}
