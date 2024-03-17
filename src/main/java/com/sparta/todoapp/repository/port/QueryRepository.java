package com.sparta.todoapp.repository.port;

import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryRepository {
    Page<ScheduleResponseDto> getSchedules (Member member, Pageable pageable);

    Page<ScheduleResponseDto> getSearchSchedule (Member member, String type, String keyword,
        Pageable pageable);
}
