package com.sparta.todoapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
public class ResponsePageDto<T> {
    private String message;
    private Page<T> data;
}
