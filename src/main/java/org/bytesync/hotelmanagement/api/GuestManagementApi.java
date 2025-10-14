package org.bytesync.hotelmanagement.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.GuestDto;
import org.bytesync.hotelmanagement.dto.ResponseMessage;
import org.bytesync.hotelmanagement.service.GuestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/guests/api")
public class GuestManagementApi {

    private final GuestService guestService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(@Valid @RequestBody GuestDto form) {
        var message = guestService.register(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage(HttpStatus.CREATED.value(), message, null));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseMessage> getDetailsById(@PathVariable int id) {
        var guest = guestService.getDetails(id);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Guest Details", guest));
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllGuests(@RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size) {
        var guestList = guestService.getAll(page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Guests List", guestList));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> update(@PathVariable int id, @RequestBody GuestDto form) {
        var message = guestService.update(id, form);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), message, null));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> delete(@PathVariable int id) {
        var message = guestService.delete(id);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Delete Guest", message));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseMessage> search(@RequestParam(defaultValue = " ") String query,
                                                  @RequestParam(required = false, defaultValue = "0") int page,
                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        var resultList = guestService.search(query, page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Search Result", resultList));
    }
}
