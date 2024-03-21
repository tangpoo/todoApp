package com.sparta.todoapp.repository.port;

import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.Member;
import com.sparta.todoapp.util.RestPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryRepository {
    RestPage<ScheduleResponseDto> getSchedules (Member member, Pageable pageable);

    RestPage<ScheduleResponseDto> getSearchSchedule (Member member, String type, String keyword,
        Pageable pageable);
}
