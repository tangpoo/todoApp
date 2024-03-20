package com.sparta.todoapp.facade;

import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.Member;
import com.sparta.todoapp.entity.Reply;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.repository.port.QueryRepository;
import com.sparta.todoapp.service.port.ReplyService;
import com.sparta.todoapp.service.port.ScheduleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleFacadeImpl implements ScheduleFacade {

    private final ScheduleService scheduleService;
    private final ReplyService replyService;
    private final QueryRepository queryRepository;

    @Override
    public ScheduleResponseDto createSchedule(Member member, ScheduleRequestDto requestDto) {
        return scheduleService.createSchedule(member, requestDto);
    }

    public ScheduleResponseDto getScheduleById(Member member, Long id) {
        Schedule schedule = scheduleService.getScheduleByMemberIdAndId(member.getId(), id);
        List<Reply> replies = replyService.findByScheduleId(schedule.getId());

        if (!replies.isEmpty()) {
            return new ScheduleResponseDto(schedule, replyService.getReplyList(replies));
        }

        return new ScheduleResponseDto(schedule);
    }

    @Override
    public Page<ScheduleResponseDto> getSchedules(Member member, Pageable pageable) {
        return queryRepository.getSchedules(member, pageable);
    }

    @Override
    public Page<ScheduleResponseDto> getSearchSchedule(Member member, String type, String keyword,
        Pageable pageable) {
        return queryRepository.getSearchSchedule(member, type, keyword, pageable);
    }

    @Override
    public ScheduleResponseDto updateSchedule(Member member, ScheduleRequestDto requestDto, Long id,
        boolean isCompleted, boolean isPrivate) {
        return scheduleService.updateSchedule(member, requestDto, id, isCompleted, isPrivate);
    }

    @Override
    public void deleteSchedule(Member member, Long id) {
        scheduleService.deleteSchedule(member, id);
    }

    @Override
    public void completedSchedule(Member member, Long id, boolean isCompleted, boolean isPrivate) {
        scheduleService.completedSchedule(member, id, isCompleted, isPrivate);
    }
}
