package com.sparta.todoapp.facade;

import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleFacade {

    ScheduleResponseDto createSchedule(Member member, ScheduleRequestDto requestDto);

    ScheduleResponseDto getScheduleById(Member member, Long id);

    Page<ScheduleResponseDto> getSchedules(Member member, Pageable pageable);

    Page<ScheduleResponseDto> getSearchSchedule(Member member, String type,
        String keyword, Pageable pageable);

    ScheduleResponseDto updateSchedule(Member member, ScheduleRequestDto requestDto,
        Long id, boolean isCompleted, boolean isPrivate);

    void deleteSchedule(Member member, Long id);

    void completedSchedule(Member member, Long id, boolean isCompleted,
        boolean isPrivate);
}
