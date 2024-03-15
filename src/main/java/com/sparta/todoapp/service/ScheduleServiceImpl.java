package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.Member;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.repository.ScheduleRepository;
import com.sparta.todoapp.service.port.ScheduleService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public ScheduleResponseDto createSchedule(Member member, ScheduleRequestDto requestDto) {

        Schedule schedule = Schedule.builder()
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .member(member)
            .build();

        return new ScheduleResponseDto(scheduleRepository.save(schedule));
    }

    @Override
    @Transactional
    public ScheduleResponseDto updateSchedule(Member member, ScheduleRequestDto requestDto,
        Long id, boolean isCompleted, boolean isPrivate) {
        Schedule schedule = getScheduleByMemberIdAndId(member.getId(), id);

        if (isCompleted) {
            schedule.changeIsCompleted(isCompleted);
        }

        if (isPrivate) {
            schedule.changeIsPrivate(isPrivate);
        }

        schedule.update(requestDto);
        return new ScheduleResponseDto(schedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Member member, Long id) {
        Schedule schedule = getScheduleByMemberIdAndId(member.getId(), id);

        scheduleRepository.delete(schedule);
    }

    @Override
    @Transactional
    public void completedSchedule(Member member, Long id, boolean isCompleted,
        boolean isPrivate) {
        Schedule schedule = getScheduleByMemberIdAndId(member.getId(), id);

        if (isCompleted) {
            schedule.changeIsCompleted(isCompleted);
        }

        if (isPrivate) {
            schedule.changeIsPrivate(isPrivate);
        }

        // 현재 한번 완료한 스케줄을 다시 취소할 수는 없는 상태

    }

    @Override
    public Schedule getScheduleByMemberIdAndId(Long memberId, Long id) {
        return scheduleRepository.findByIdAndMemberId(id, memberId)
            .orElseThrow(() -> new NoSuchElementException("일정이 존재하지 않습니다."));
    }
}
