package com.sparta.todoapp.dto.schedule;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleListResponseDto {
    private String author;
    private List<ScheduleResponseDto> schedules;

    public ScheduleListResponseDto(List<ScheduleResponseDto> list) {
        this.schedules = list;
    }

    public ScheduleListResponseDto(String author, List<ScheduleResponseDto> schedules) {
        this.author = author;
        this.schedules = schedules.stream().toList();
    }
}
