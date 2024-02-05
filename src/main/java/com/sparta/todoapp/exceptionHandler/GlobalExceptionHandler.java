package com.sparta.todoapp.exceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDto> AccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.badRequest()
                .body(new ExceptionDto(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                        e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionDto> NoSuchElementException(NoSuchElementException e){
        return ResponseEntity.badRequest()
                .body(new ExceptionDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND,
                        e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> EntityNotFoundException(EntityNotFoundException e){
        return ResponseEntity.badRequest()
                .body(new ExceptionDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND,
                        e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> IllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity.badRequest()
                .body(new ExceptionDto(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT,
                        e.getMessage()));
    }

}
