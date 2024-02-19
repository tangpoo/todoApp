package com.sparta.todoapp.dto.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 1024)
    private String content;
}
