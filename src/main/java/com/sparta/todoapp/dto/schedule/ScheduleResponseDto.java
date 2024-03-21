package com.sparta.todoapp.dto.schedule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sparta.todoapp.entity.Schedule;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponseDto {

    private Long todoId;
    private String title;
    private String content;
    private String author;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;
    @Builder.Default
    private Map<Long, String> replyList = new LinkedHashMap<>();
    private boolean isCompleted;
    private boolean isPrivate;

    public ScheduleResponseDto(Schedule schedule, Map<Long, String> replyList) {
        this.todoId = schedule.getId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.author = schedule.getMember().getUsername();
        this.date = schedule.getCreatedAt();
        this.replyList = replyList;
        this.isCompleted = schedule.isCompleted();
        this.isPrivate = schedule.isPrivate();
    }

    public ScheduleResponseDto(Schedule schedule) {
        this.todoId = schedule.getId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.author = schedule.getMember().getUsername();
        this.date = schedule.getCreatedAt();
        this.isCompleted = schedule.isCompleted();
        this.isPrivate = schedule.isPrivate();
    }
}
