package com.sparta.todoapp.facade;

import static com.querydsl.core.types.Projections.fields;
import static com.sparta.todoapp.entity.QSchedule.schedule;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.Member;
import com.sparta.todoapp.entity.QSchedule;
import com.sparta.todoapp.entity.Reply;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.exceptionHandler.NotFindFilterException;
import com.sparta.todoapp.service.ReplyServiceImpl;
import com.sparta.todoapp.service.port.ScheduleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleFacadeImpl implements ScheduleFacade {

    private final ScheduleService scheduleService;
    private final ReplyServiceImpl replyServiceImpl;
    private final JPAQueryFactory queryFactory;

    @Override
    public ScheduleResponseDto createSchedule(Member member, ScheduleRequestDto requestDto) {
        return scheduleService.createSchedule(member, requestDto);
    }

    public ScheduleResponseDto getScheduleById(Member member, Long id) {
        Schedule schedule = scheduleService.getScheduleByMemberIdAndId(member.getId(), id);
        List<Reply> replies = replyServiceImpl.findByScheduleId(schedule.getId());

        if (!replies.isEmpty()) {
            return new ScheduleResponseDto(schedule, replyServiceImpl.getReplyList(replies));
        }

        return new ScheduleResponseDto(schedule);
    }

    @Override
    public Page<ScheduleResponseDto> getSchedules(Member member, Pageable pageable) {
        QSchedule qSchedule = schedule;

        List<ScheduleResponseDto> schedules =
            queryFactory.select(fields(ScheduleResponseDto.class,
                    qSchedule.id.as("todoId"),
                    qSchedule.title,
                    qSchedule.content,
                    Expressions.asString(member.getUsername()).as("author"),
                    qSchedule.isCompleted,
                    qSchedule.isPrivate,
                    qSchedule.createdAt.as("date")))
                .from(qSchedule)
                .where(qSchedule.member.eq(member))
                .orderBy(qSchedule.isCompleted.asc(), qSchedule.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

//        Long total = (long) schedules.size();

        Long total = queryFactory
            .select(qSchedule.count())
            .from(qSchedule)
            .where(qSchedule.member.id.eq(member.getId()))
            .fetchOne();

        // 유저 객체를 가지고 schedule 을 isComplete 가 false 인 것, 작성일이 빠른 순으로 정렬
//        List<Schedule> schedules = scheduleRepository.findAllByUserIdOrderByIsCompletedAscModifiedAtDesc(user.getId()).orElseThrow();

//        ScheduleListResponseDto responseDto = new ScheduleListResponseDto(user.getUsername(),
//            schedules.stream().map(ScheduleResponseDto::new).toList());

        return new PageImpl<>(schedules, pageable, total);
    }

    @Override
    public Page<ScheduleResponseDto> getSearchSchedule(Member member, String type, String keyword,
        Pageable pageable) {
        QSchedule qSchedule = schedule;

        List<ScheduleResponseDto> schedules;
        Long total;
        try {
            schedules = queryFactory
                .select(fields(ScheduleResponseDto.class,
                    qSchedule.id.as("todoId"),
                    qSchedule.title,
                    qSchedule.content,
                    Expressions.asString(member.getUsername()).as("author"),
                    qSchedule.isCompleted,
                    qSchedule.isPrivate,
                    qSchedule.createdAt.as("date")))
                .from(qSchedule)
                .where(qSchedule.member.id.eq(member.getId()), eqType(type, keyword))
                .orderBy(qSchedule.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

            total = queryFactory
                .select(qSchedule.count())
                .from(qSchedule)
                .where(qSchedule.member.id.eq(member.getId()), eqType(type, keyword))
                .fetchOne();
        } catch (ClassCastException e) {
            throw new NotFindFilterException();
        }

        return new PageImpl<>(schedules, pageable, total);
    }

    private BooleanExpression eqType(String type, String keyword) {

        // 하나가 null 일 경우 다른 하나로만 검색
        // 둘다 null 일 경우 모든 게시글 조회

        if (type.isEmpty() || keyword.isEmpty()) {
            return Expressions.asBoolean(false);
        }

        if (type.equals("title")) {
            return schedule.title.contains(keyword);
        }

        if (type.equals("content")) {
            return schedule.content.contains(keyword);
        }

        return null;
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
