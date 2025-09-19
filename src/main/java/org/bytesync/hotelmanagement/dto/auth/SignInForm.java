package org.bytesync.hotelmanagement.dto.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public record SignInForm(
        String email,
        String password
) {

    // while sign in, this generates the unauthenticated token using email and password.
//    then in the next step this token will be passed to AuthenticationManager
    public Authentication authToken() {
        return UsernamePasswordAuthenticationToken.unauthenticated(email, password);
    }
}
