package com.sparta.todoapp.exceptionHandler;

import java.util.NoSuchElementException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDto> AccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.badRequest()
            .body(new ExceptionDto(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionDto> NoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.badRequest()
            .body(new ExceptionDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND,
                e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDto> DataIntegrityViolationException(
        IllegalArgumentException e) {
        return ResponseEntity.badRequest()
            .body(new ExceptionDto(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT,
                e.getMessage()));
    }

}
