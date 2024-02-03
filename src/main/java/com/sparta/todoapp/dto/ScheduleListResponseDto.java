package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleListResponseDto {
    private String author;
    private List<ScheduleResponseDto> schedules;

    public ScheduleListResponseDto(User user) {
        this.author = user.getUsername();
        this.schedules = user.getSchedules().stream()
                .map(ScheduleResponseDto::new)
                .toList();
    }

    public ScheduleListResponseDto(String author, List<ScheduleResponseDto> schedules) {
        this.author = author;
        this.schedules = schedules.stream().toList();
    }
}
