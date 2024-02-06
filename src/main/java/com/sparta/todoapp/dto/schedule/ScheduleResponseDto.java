package com.sparta.todoapp.dto.schedule;

import com.sparta.todoapp.entity.Reply;
import com.sparta.todoapp.entity.Schedule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ScheduleResponseDto {
    private Long todoId;
    private String title;
    private String content;
    private String author;
    private LocalDateTime date;
    private Map<Long, String> replyList = new LinkedHashMap<>();
    private boolean isCompleted;
    private boolean isPrivate;

    public ScheduleResponseDto(Schedule schedule, Map<Long, String> replyList) {
        this.todoId = schedule.getId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.author = schedule.getUser().getUsername();
        this.date = schedule.getCreatedAt();
        this.replyList = replyList;
        this.isCompleted = schedule.isCompleted();
        this.isPrivate = schedule.isPrivate();
    }

    public ScheduleResponseDto(Schedule schedule) {
        this.todoId = schedule.getId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.author = schedule.getUser().getUsername();
        this.date = schedule.getCreatedAt();
        this.isCompleted = schedule.isCompleted();
        this.isPrivate = schedule.isPrivate();
    }
}
