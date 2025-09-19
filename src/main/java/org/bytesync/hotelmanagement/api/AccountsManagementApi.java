package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.PageResult;
import org.bytesync.hotelmanagement.dto.auth.UserDto;
import org.bytesync.hotelmanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users/api")
public class AccountsManagementApi {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<PageResult<UserDto>> getAllUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.getAll(page, size));
    }

    @PatchMapping("/enable/{email}")
    public ResponseEntity<String>  enableUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.enable(email));
    }

    @PatchMapping("/disable/{email}")
    public ResponseEntity<String>  disableUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.disable(email));
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.delete(email));
    }
}
