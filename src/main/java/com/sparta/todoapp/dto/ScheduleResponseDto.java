package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Schedule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleResponseDto {
    private Long todoId;
    private String title;
    private String content;
    private String author;
    private LocalDateTime date;
    private boolean isCompleted;
    private boolean isPrivate;

    public ScheduleResponseDto(Schedule schedule) {
        this.todoId = schedule.getId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.author = schedule.getUser().getUsername();
        this.date = schedule.getDate();
        this.isCompleted = schedule.isCompleted();
        this.isPrivate = schedule.isPrivate();
    }
}
