package com.sparta.todoapp.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionDto {

    private int statusCode;
    private HttpStatus state;
    private String message;
}
