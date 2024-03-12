package com.sparta.todoapp.dto.schedule;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleListResponseDto {

    private String author;
    private List<ScheduleResponseDto> schedules;

    public ScheduleListResponseDto(String author, List<ScheduleResponseDto> schedules) {
        this.author = author;
        this.schedules = schedules.stream().toList();
    }
}
