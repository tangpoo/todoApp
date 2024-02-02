package com.sparta.todoapp.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleRequestDto {

    private String title;
    private String content;
    private LocalDateTime date;

    public ScheduleRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
        this.date = LocalDateTime.now();
    }
}
