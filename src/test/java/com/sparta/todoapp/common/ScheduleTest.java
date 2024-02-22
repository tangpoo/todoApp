package com.sparta.todoapp.common;

import com.sparta.todoapp.dto.schedule.ScheduleListResponseDto;
import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.Schedule;

import java.util.LinkedList;
import java.util.List;

public interface ScheduleTest extends UserTest {
    Long TEST_SCHEDULE_ID = 1L;
    String TEST_SCHEDULE_TITLE = "title";
    String TEST_SCHEDULE_CONTENT = "content";
    List<ScheduleResponseDto> TEST_SCHEDULE_LIST = new LinkedList<>();

    ScheduleRequestDto TEST_SCHEDULE_REQUEST_DTO = ScheduleRequestDto.builder()
            .title(TEST_SCHEDULE_TITLE)
            .content(TEST_SCHEDULE_CONTENT)
            .build();

    ScheduleResponseDto TEST_SCHEDULE_RESPONSE_DTO = ScheduleResponseDto.builder()
            .title(TEST_SCHEDULE_TITLE)
            .content(TEST_SCHEDULE_CONTENT)
            .build();

    ScheduleResponseDto TEST_ANOTHER_SCHEDULE_RESPONSE_DTO = ScheduleResponseDto.builder()
            .title(ANOTHER_PREFIX + TEST_SCHEDULE_TITLE)
            .content(ANOTHER_PREFIX + TEST_SCHEDULE_CONTENT)
            .build();

    Schedule TEST_SCHEDULE = Schedule.builder()
            .title(TEST_SCHEDULE_TITLE)
            .content(TEST_SCHEDULE_CONTENT)
            .build();

    Schedule TEST_ANOTHER_SCHEDULE = Schedule.builder()
            .title(ANOTHER_PREFIX + TEST_SCHEDULE_TITLE)
            .content(ANOTHER_PREFIX + TEST_SCHEDULE_CONTENT)
            .build();
}
