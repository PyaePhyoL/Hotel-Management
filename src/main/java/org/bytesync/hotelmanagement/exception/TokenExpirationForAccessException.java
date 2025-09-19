package org.bytesync.hotelmanagement.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenExpirationForAccessException extends AuthenticationException {
    public TokenExpirationForAccessException() {
        super("You need to refresh access token again.");
    }
}
