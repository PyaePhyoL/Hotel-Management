package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.PageResult;
import org.bytesync.hotelmanagement.dto.ResponseMessage;
import org.bytesync.hotelmanagement.dto.auth.*;
import org.bytesync.hotelmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/api")
public class AccountsManagementApi {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(@RequestBody RegisterForm form) {
        var message = userService.register(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage(HttpStatus.CREATED.value(), message, null));
    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseMessage> signin(@RequestBody SignInForm form) {
        var message = "User signed in successfully";
        var userInfo = userService.signIn(form);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), message, userInfo));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ResponseMessage> update(@PathVariable long userId, @RequestBody RegisterForm form) {
        var message = userService.update(userId, form);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), message, null));
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<ResponseMessage> refresh(@RequestBody RefreshToken token) {
        var userInfo = userService.refresh(token.refresh());
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Token refreshed", userInfo));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> getAllUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        var contents = userService.getAll(page, size);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "", contents));
    }

    @PatchMapping("/enable/{userId}")
    public ResponseEntity<ResponseMessage>  enableUser(@PathVariable long userId) {
        var message = userService.enable(userId);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), message, null));
    }

    @PatchMapping("/disable/{userId}")
    public ResponseEntity<ResponseMessage>  disableUser(@PathVariable long userId) {
        var message = userService.disable(userId);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), message, null));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ResponseMessage> deleteUser(@PathVariable long userId) {
        var message = userService.delete(userId);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), message, null));
    }
}
