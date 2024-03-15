package com.sparta.todoapp.service;

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
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.MemberRepository;
import com.sparta.todoapp.repository.ReplyRepository;
import com.sparta.todoapp.repository.ScheduleRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;
    private final JwtUtil jwtUtil;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public ScheduleResponseDto createSchedule(Member member, ScheduleRequestDto requestDto) {

        Schedule schedule = Schedule.builder()
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .member(member)
            .build();

        return new ScheduleResponseDto(scheduleRepository.save(schedule));
    }

    public ScheduleResponseDto getScheduleById(Member member, Long id) {
        Schedule schedule = getScheduleByTokenAndId(member.getId(), id);
        List<Reply> replies = replyRepository.findAllByScheduleId(schedule.getId());

        if (replies.size() > 0) {
            return new ScheduleResponseDto(schedule, getReplyList(replies));
        }

        return new ScheduleResponseDto(schedule);
    }

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

    public Page<ScheduleResponseDto> getSearchSchedule(Member member, String type,
        String keyword, Pageable pageable) {

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

    @Transactional
    public ScheduleResponseDto updateSchedule(Member member, ScheduleRequestDto requestDto,
        Long id, boolean isCompleted, boolean isPrivate) {
        Schedule schedule = getScheduleByTokenAndId(member.getId(), id);

        if (isCompleted) {
            schedule.changeIsCompleted(isCompleted);
        }

        if (isPrivate) {
            schedule.changeIsPrivate(isPrivate);
        }

        schedule.update(requestDto);
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    public void deleteSchedule(Member member, Long id) {
        Schedule schedule = getScheduleByTokenAndId(member.getId(), id);

        scheduleRepository.delete(schedule);
    }

    @Transactional
    public void completedSchedule(Member member, Long id, boolean isCompleted,
        boolean isPrivate) {
        Schedule schedule = getScheduleByTokenAndId(member.getId(), id);

        if (isCompleted) {
            schedule.changeIsCompleted(isCompleted);
        }

        if (isPrivate) {
            schedule.changeIsPrivate(isPrivate);
        }

        // 현재 한번 완료한 스케줄을 다시 취소할 수는 없는 상태

    }

    private Schedule getScheduleByTokenAndId(Long memberId, Long id) {
        return scheduleRepository.findByIdAndMemberId(id, memberId)
            .orElseThrow(() -> new NoSuchElementException("일정이 존재하지 않습니다."));
    }

    private Map<Long, String> getReplyList(List<Reply> replies) {
        Map<Long, String> replyList = new LinkedHashMap<>();
        for (Reply reply : replies) {
            replyList.put(reply.getId(), reply.getContent());
        }

        return replyList;
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
}
