package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.GuestDto;
import org.bytesync.hotelmanagement.dto.ResponseMessage;
import org.bytesync.hotelmanagement.service.GuestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guests/api")
public class GuestManagementApi {

    private final GuestService guestService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(@RequestBody GuestDto form) {
        var message = guestService.register(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage(HttpStatus.CREATED.value(), message, null));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseMessage> getDetailsById(@PathVariable int id) {
        var guest = guestService.getDetails(id);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "", guest));
    }
}
