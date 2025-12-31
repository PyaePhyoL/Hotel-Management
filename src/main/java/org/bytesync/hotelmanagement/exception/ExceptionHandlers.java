package org.bytesync.hotelmanagement.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.bytesync.hotelmanagement.dto.output.ResponseMessage;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseMessage<Void> handle(EntityNotFoundException e) {
        return new ResponseMessage<>(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                null
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResponseMessage<Void> handle(AuthenticationException e) {
        var message = switch (e) {
            case UsernameNotFoundException ue -> "Please check your log in email";
            case BadCredentialsException be -> "Please check your credentials";
            case DisabledException de -> "Your account is disabled";
            default -> e.getMessage();
        };
        return new ResponseMessage<>(
                HttpStatus.UNAUTHORIZED.value(),
                message,
                null
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    ResponseMessage<Void> handle(UserAlreadyExistsException e) {
        return new ResponseMessage<>(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                null
        );
    }

    @ExceptionHandler @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseMessage<Void> handle(HttpMessageNotReadableException e) {
        return new ResponseMessage<>(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                null
        );
    }

    @ExceptionHandler @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseMessage<Void> handle(MethodArgumentNotValidException e) {
        var errors = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ResponseMessage<>(
                HttpStatus.BAD_REQUEST.value(),
                errors,
                null
        );
    }

    @ExceptionHandler @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseMessage<Void> handle(IllegalStateException e) {
        return new ResponseMessage<>(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                null
        );
    }

    @ExceptionHandler @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseMessage<Void> handle(IllegalArgumentException e) {
        return new ResponseMessage<>(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                null
        );
    }

    @ExceptionHandler(TokenExpirationForAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResponseMessage<Void> handle(TokenExpirationForAccessException e) {
        return new ResponseMessage<>(
                HttpStatus.FORBIDDEN.value(),
                e.getMessage(),
                null
        );
    }

    @ExceptionHandler @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseMessage<Void> handle(NotEnoughMoneyException e) {
        return new ResponseMessage<>(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                null
        );
    }

}
