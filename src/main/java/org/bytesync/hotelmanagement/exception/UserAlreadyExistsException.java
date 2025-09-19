package org.bytesync.hotelmanagement.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String e) {
        super(e);
    }
}
