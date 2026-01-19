package org.bytesync.hotelmanagement.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.guest.GuestDto;
import org.bytesync.hotelmanagement.dto.guest.GuestStatusDto;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.enums.GuestStatus;
import org.bytesync.hotelmanagement.service.interfaces.guest.IGuestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/guests/api")
public class GuestManagementApi {

    private final IGuestService guestService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage<Void>> register(@Valid @RequestBody GuestDto form) {
        var message = guestService.register(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage<>(HttpStatus.CREATED.value(), message, null));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseMessage<GuestDto>> getDetailsById(@PathVariable Long id) {
        var guest = guestService.getDetails(id);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "Guest Details", guest));

    }

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage<PageResult<GuestDto>>> getAllGuests(@RequestParam(required = false, defaultValue = "0") int page,
                                                                    @RequestParam(required = false, defaultValue = "10") int size) {
        var guestList = guestService.getAll(page, size);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "Guests List", guestList));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage<Void>> update(@PathVariable Long id, @RequestBody GuestDto form) {
        var message = guestService.update(id, form);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage<Void>> delete(@PathVariable Long id) {
        var message = guestService.delete(id);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseMessage<PageResult<GuestDto>>> search(@RequestParam(defaultValue = " ") String query,
                                                  @RequestParam(required = false, defaultValue = "0") int page,
                                                  @RequestParam(required = false, defaultValue = "10") int size) {
        var resultList = guestService.search(query, page, size);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", resultList));
    }

    @DeleteMapping("/delete/relation/{rsId}")
    public ResponseEntity<ResponseMessage<Void>> deleteRelation(@PathVariable Long rsId) {
        var message = guestService.deleteRelation(rsId);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }

    @PutMapping("/change-status/{id}/{status}")
    public ResponseEntity<ResponseMessage<Void>> changeStatus(@PathVariable Long id,  @PathVariable GuestStatus status) {
        var message = guestService.changeStatus(id, status);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }

    @PutMapping("/update-photo/{id}")
    public ResponseEntity<ResponseMessage<String>> updatePhotoUrl(@PathVariable Long id, @RequestParam String photo) {
        var message = guestService.updatePhotoUrl(id, photo);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }

    @PutMapping("/update-nrc/{id}")
    public ResponseEntity<ResponseMessage<String>> updateNrcUrl(@PathVariable Long id, @RequestParam String nrc) {
        var message = guestService.updateNrcUrl(id, nrc);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }

    @GetMapping("/check-guest-status")
    public ResponseEntity<ResponseMessage<GuestStatusDto>> checkGuestStatus(@RequestParam String name, @RequestParam String nrc) {
        var statusDto = guestService.checkGuestStatusByNameAndNrc(name, nrc);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", statusDto));
    }
}
