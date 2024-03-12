package com.sparta.todoapp.service;

import static com.querydsl.core.types.Projections.bean;
import static com.querydsl.core.types.Projections.fields;

import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.todoapp.dto.schedule.ScheduleListResponseDto;
import com.sparta.todoapp.dto.schedule.ScheduleRequestDto;
import com.sparta.todoapp.dto.schedule.ScheduleResponseDto;
import com.sparta.todoapp.entity.QSchedule;
import com.sparta.todoapp.entity.Reply;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.jwt.JwtUtil;
import com.sparta.todoapp.repository.ReplyRepository;
import com.sparta.todoapp.repository.ScheduleRepository;
import com.sparta.todoapp.repository.UserRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final JwtUtil jwtUtil;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public ScheduleResponseDto createSchedule(String accessToken, ScheduleRequestDto requestDto) {
        String author = jwtUtil.getUserInfoFromToken(accessToken);
        User user = userRepository.findByUsername(author).orElseThrow();
        Schedule schedule = Schedule.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(user)
                .build();

        return new ScheduleResponseDto(scheduleRepository.save(schedule));
    }

    public ScheduleResponseDto getScheduleById(String accessToken, Long id) {
        Schedule schedule = getScheduleByTokenAndId(accessToken, id);
        List<Reply> replies = replyRepository.findAllByScheduleId(schedule.getId());

        if (replies.size() > 0) {
            return new ScheduleResponseDto(schedule, getReplyList(replies));
        }

        return new ScheduleResponseDto(schedule);
    }

    private Map<Long, String> getReplyList(List<Reply> replies) {
        Map<Long, String> replyList = new LinkedHashMap<>();
        for (Reply reply : replies) {
            replyList.put(reply.getId(), reply.getContent());
        }

        return replyList;
    }

    public Page<ScheduleResponseDto> getSchedules(String accessToken, Pageable pageable) {
        String author = jwtUtil.getUserInfoFromToken(accessToken);
        User user = userRepository.findByUsername(author).orElseThrow();;

        QSchedule qSchedule = new QSchedule("s");

        List<ScheduleResponseDto> schedules =
            queryFactory.select(fields(ScheduleResponseDto.class,
                    qSchedule.id.as("todoId"),
                    qSchedule.title,
                    qSchedule.content,
                    Expressions.asString(user.getUsername()).as("author"),
                    qSchedule.isCompleted,
                    qSchedule.isPrivate,
                    qSchedule.createdAt.as("date")))
                .from(qSchedule)
                .where(qSchedule.user.eq(user))
                .orderBy(qSchedule.isCompleted.asc(), qSchedule.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

//        Long total = (long) schedules.size();

        Long total = queryFactory
            .select(qSchedule.count())
            .from(qSchedule)
            .where(qSchedule.user.id.eq(user.getId()))
            .fetchOne();

        // 유저 객체를 가지고 schedule 을 isComplete 가 false 인 것, 작성일이 빠른 순으로 정렬
//        List<Schedule> schedules = scheduleRepository.findAllByUserIdOrderByIsCompletedAscModifiedAtDesc(user.getId()).orElseThrow();

//        ScheduleListResponseDto responseDto = new ScheduleListResponseDto(user.getUsername(),
//            schedules.stream().map(ScheduleResponseDto::new).toList());

        return new PageImpl<>(schedules, pageable, total);
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(String accessToken, ScheduleRequestDto requestDto, Long id, boolean isCompleted, boolean isPrivate) {
        Schedule schedule = getScheduleByTokenAndId(accessToken, id);

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
    public void deleteSchedule(String accessToken, Long id) {
        Schedule schedule = getScheduleByTokenAndId(accessToken, id);

        scheduleRepository.delete(schedule);
    }

    @Transactional
    public void completedSchedule(String accessToken, Long id, boolean isCompleted, boolean isPrivate) {
        Schedule schedule = getScheduleByTokenAndId(accessToken, id);

        if (isCompleted) {
            schedule.changeIsCompleted(isCompleted);
        }

        if (isPrivate) {
            schedule.changeIsPrivate(isPrivate);
        }
        
        // 현재 한번 완료한 스케줄을 다시 취소할 수는 없는 상태

    }

    private Schedule getScheduleByTokenAndId(String accessToken, Long id) {
        String author = jwtUtil.getUserInfoFromToken(accessToken);
        User user = userRepository.findByUsername(author).orElseThrow();

        return scheduleRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new NoSuchElementException("일정이 존재하지 않습니다."));
    }

}
