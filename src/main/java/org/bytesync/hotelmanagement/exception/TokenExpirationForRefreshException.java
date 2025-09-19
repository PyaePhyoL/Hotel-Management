package org.bytesync.hotelmanagement.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenExpirationForRefreshException extends AuthenticationException {
    public TokenExpirationForRefreshException() {
        super("You need to refresh token again.");
    }
}
