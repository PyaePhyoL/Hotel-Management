package org.bytesync.hotelmanagement.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.output.PageResult;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.bytesync.hotelmanagement.dto.auth.*;
import org.bytesync.hotelmanagement.service.interfaces.hotel.IStaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/api")
public class StaffManagementApi {

    private final IStaffService staffService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage<Void>> register(@Valid @RequestBody StaffRegisterForm form) {
        var message = staffService.register(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage<>(HttpStatus.CREATED.value(), message, null));
    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseMessage<StaffInfo>> signin(@RequestBody SignInForm form) {
        var staffInfo = staffService.signIn(form);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", staffInfo));
    }

    @GetMapping("/detail/{userId}")
    public ResponseEntity<ResponseMessage<StaffDetailsDto>> getDetailsById(@PathVariable Long userId) {
        var userDetails = staffService.getDetails(userId);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", userDetails));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ResponseMessage<Void>> update(@PathVariable Long userId, @RequestBody StaffRegisterForm form) {
        var message = staffService.update(userId, form);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<ResponseMessage<StaffInfo>> refresh(@RequestBody RefreshToken token) {
        var userInfo = staffService.refresh(token.refresh());
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", userInfo));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage<PageResult<StaffDto>>> getAllStaffs(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        var contents = staffService.getAll(page, size);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", contents));
    }

    @PatchMapping("/enable/{userId}")
    public ResponseEntity<ResponseMessage<Void>>  enableStaff(@PathVariable Long userId) {
        var message = staffService.enable(userId);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }

    @PatchMapping("/disable/{userId}")
    public ResponseEntity<ResponseMessage<Void>>  disableStaff(@PathVariable Long userId) {
        var message = staffService.disable(userId);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ResponseMessage<Void>> deleteStaff(@PathVariable Long userId) {
        var message = staffService.delete(userId);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), message, null));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseMessage<PageResult<StaffDto>>> search(@RequestParam(defaultValue = " ") String query,
                                                        @RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size ){
        var contents = staffService.search(query, page, size);
        return ResponseEntity.ok(new ResponseMessage<>(HttpStatus.OK.value(), "", contents));
    }
}
