package com.sparta.todoapp.service.port;

import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.Member;
import com.sparta.todoapp.entity.Schedule;

public interface ScheduleService {

    ScheduleResponseDto createSchedule(Member member, ScheduleRequestDto requestDto);
    ScheduleResponseDto updateSchedule(Member member, ScheduleRequestDto requestDto,
        Long id, boolean isCompleted, boolean isPrivate);
    void deleteSchedule(Member member, Long id);
    void completedSchedule(Member member, Long id, boolean isCompleted,
        boolean isPrivate);
    Schedule getScheduleByMemberIdAndId(Long memberId, Long id);
}
