package com.sparta.todoapp.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class ResponseDto<T> {
    private String message;
    private T data;

}
