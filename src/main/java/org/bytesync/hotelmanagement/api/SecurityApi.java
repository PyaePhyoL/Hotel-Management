package org.bytesync.hotelmanagement.api;

import lombok.RequiredArgsConstructor;
import org.bytesync.hotelmanagement.dto.auth.RefreshToken;
import org.bytesync.hotelmanagement.dto.auth.RegisterForm;
import org.bytesync.hotelmanagement.dto.auth.SignInForm;
import org.bytesync.hotelmanagement.dto.auth.UserInfo;
import org.bytesync.hotelmanagement.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SecurityApi {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterForm form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(form));
    }

    @PostMapping("/signin")
    public ResponseEntity<UserInfo> signin(@RequestBody SignInForm form) {
        return ResponseEntity.ok(authService.signIn(form));
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<UserInfo> refresh(@RequestBody RefreshToken token) {
        return ResponseEntity.ok(authService.refresh(token.refresh()));
    }

}
