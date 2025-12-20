package org.bytesync.hotelmanagement.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.guest.GuestDto;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.enums.GuestStatus;
import org.bytesync.hotelmanagement.service.impl.guest.GuestService;
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
    public ResponseEntity<ResponseMessage> getDetailsById(@PathVariable Long id) {
        var guest = guestService.getDetails(id);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Guest Details", guest));

    }

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> getAllGuests(@RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size) {
        var guestList = guestService.getAll(page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Guests List", guestList));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> update(@PathVariable Long id, @RequestBody GuestDto form) {
        var message = guestService.update(id, form);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), message, null));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> delete(@PathVariable Long id) {
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

    @DeleteMapping("/delete/relation/{rsId}")
    public ResponseEntity<ResponseMessage> deleteRelation(@PathVariable Long rsId) {
        var message = guestService.deleteRelation(rsId);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "", message));
    }

    @PutMapping("/change-status/{id}/{status}")
    public ResponseEntity<ResponseMessage> changeStatus(@PathVariable Long id,  @PathVariable GuestStatus status) {
        var message = guestService.changeStatus(id, status);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Change Guest Status", message));
    }
}
