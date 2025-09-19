package org.bytesync.hotelmanagement.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    List<String> handle(EntityNotFoundException e) {
        return List.of(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    List<String> handle(AuthenticationException e) {
        return List.of(switch (e) {
            case UsernameNotFoundException ue -> "Please check your log in email";
            case BadCredentialsException be -> "Please check your credentials";
            case DisabledException de -> "Your account is disabled";
            default -> e.getMessage();
        });
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    List<String> handle(UserAlreadyExistsException e) {
        return List.of(e.getMessage());
    }

}
