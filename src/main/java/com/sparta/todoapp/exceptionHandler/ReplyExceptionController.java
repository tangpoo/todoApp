package com.sparta.todoapp.exceptionHandler;

import com.sparta.todoapp.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class ReplyExceptionController {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDto> AccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.badRequest()
                .body(new ExceptionDto(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
                        e.getMessage()));
    }

}
