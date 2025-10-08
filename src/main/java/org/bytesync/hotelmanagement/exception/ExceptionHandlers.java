package org.bytesync.hotelmanagement.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.bytesync.hotelmanagement.dto.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    ResponseMessage handle(EntityNotFoundException e) {
        return new ResponseMessage(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                null
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResponseMessage handle(AuthenticationException e) {
        var message = switch (e) {
            case UsernameNotFoundException ue -> "Please check your log in email";
            case BadCredentialsException be -> "Please check your credentials";
            case DisabledException de -> "Your account is disabled";
            default -> e.getMessage();
        };
        return new ResponseMessage(
                HttpStatus.UNAUTHORIZED.value(),
                message,
                null
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    ResponseMessage handle(UserAlreadyExistsException e) {
        return new ResponseMessage(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                null
        );
    }

    @ExceptionHandler @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseMessage handle(HttpMessageNotReadableException e) {
        return new ResponseMessage(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                null
        );
    }

}
