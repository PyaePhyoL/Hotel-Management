package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.dto.room.RoomDto;
import org.bytesync.hotelmanagement.dto.room.RoomOverviewDetails;
import org.bytesync.hotelmanagement.dto.room.RoomSelectList;
import org.bytesync.hotelmanagement.enums.RoomStatus;
import org.bytesync.hotelmanagement.service.impl.hotel.RoomService;
import org.bytesync.hotelmanagement.service.interfaces.hotel.IRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rooms/api")
@RequiredArgsConstructor
public class RoomsManagementApi {

    private final IRoomService roomService;

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage<PageResult<RoomDto>>> getRooms(@RequestParam(required = false, defaultValue = "0") int page,
                                                                         @RequestParam(required = false, defaultValue = "10") int size) {
        var rooms = roomService.getList(page, size);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", rooms));
    }

    @GetMapping("/available")
    public ResponseEntity<ResponseMessage<List<RoomDto>>> getAvailableRooms(){
        var rooms = roomService.getAllAvailableRooms();
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", rooms));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ResponseMessage<RoomOverviewDetails>> getRoomDetails(@PathVariable Long id){
        var roomDetails = roomService.getRoomOverviewDetailsById(id);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", roomDetails));
    }

    @GetMapping("/select-list")
    public ResponseEntity<ResponseMessage<List<RoomSelectList>>> selectAvailableRooms(){
        var rooms = roomService.selectAvailableRoomList();
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", rooms));
    }

    @GetMapping("/select-list/double")
    public ResponseEntity<ResponseMessage<List<RoomSelectList>>> selectAvailableDoubleRooms(){
        var rooms = roomService.selectAvailableDoubleRoomList();
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", rooms));
    }

    @PatchMapping("/change-status/{id}/{status}")
    public ResponseEntity<ResponseMessage<Void>> changeRoomStatus(@PathVariable Long id,
                                                            @PathVariable RoomStatus status){
        var message = roomService.changeRoomStatus(id, status);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }

    @PatchMapping("/change-price/{id}")
    public ResponseEntity<ResponseMessage<Void>> changeRoomPrice(@PathVariable Long id, @RequestParam Integer price){
        var message = roomService.changeRoomPrice(id, price);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }
}
